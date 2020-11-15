package ca.sgicanada.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.novell.ldapchai.ChaiConstant;
import com.novell.ldapchai.ChaiEntry;
import com.novell.ldapchai.ChaiFactory;
import com.novell.ldapchai.ChaiGroup;
import com.novell.ldapchai.ChaiUser;
import com.novell.ldapchai.exception.ChaiOperationException;
import com.novell.ldapchai.exception.ChaiPasswordPolicyException;
import com.novell.ldapchai.exception.ChaiUnavailableException;
import com.novell.ldapchai.provider.ChaiConfiguration;
import com.novell.ldapchai.provider.ChaiProvider;
import com.novell.ldapchai.provider.ChaiProviderFactory;
import com.novell.ldapchai.provider.ChaiSetting;
import com.novell.ldapchai.util.SearchHelper;

/**
 * Creating LDAP as singleton utility class so we can save connection and does
 * not need to do connection every time. LDAP chai is watch dog enabled so it
 * will detect connection failure automatically.
 * */
public class LDAPUtils
{
    /*
     * Variable Declarations: ldapURL - will contain the ldap server url
     * ldapAdminDN - will contain the distinguished name of the admin id
     * ldapAdminPW - will contain the password of the admin id provider - will
     * contain a connection to the LDAP server
     */
    private static String ldapURL = null;
    private static String ldapAdminDN = null;
    private static String ldapAdminPW = null;
    private static String ldapRoot = null;
    private static String ldapContainer = null;
    private static String regionGroup = null;
    private ChaiProvider provider = null;
    private static Logger log = Logger.getLogger(LDAPUtils.class);

    /* extra properties defined in LDAP attributes */
    private static final String LOGIN_EXPIRATION_TIME = "loginExpirationTime";
    private static final String LOGIN_DISABLED = "loginDisabled";
    private static final String BROKER_OFFICE = "BrokerOffice";

    /* constant messages for logging exceptions. */

    private static final String CHAI_UNAVAILABLE_EXCEPTION = "LDAP Chai is unavailable.";
    private static final String CHAI_OPERATION_EXCEPTION = "LDAP Chai operation could not be performed.";
    private static final String CHAI_ILLEGAL_STATE_EXCEPTION = "LDAP chai underlying connection is not in an available state.";

    /*
     * Constructor - Initialize LDAP settings from the ldaputils.properties file
     */
    public LDAPUtils() throws LDAPNotAvailableException
    {
        loadProperties();
        if (connect() == -1)
            throw new LDAPNotAvailableException("LDAP is not available.");
    }

    /*
     * connect - Connect to the LDAP server - If an error occurs this method
     * returns -1
     */
    private int connect()
    {
        try
        {
            /* Create LDAP Chai config settings */
            ChaiConfiguration chaiConfig = new ChaiConfiguration(ldapURL,
                    ldapAdminDN, ldapAdminPW);

            /* Set the chai config to blind trust SSL certificate */
            chaiConfig.setSetting(ChaiSetting.PROMISCUOUS_SSL, "true");

            /* Create LDAP Chai provider using the config */
            provider = ChaiProviderFactory.createProvider(chaiConfig);

        }
        /* If the value of one of the settings is not syntactically correct */
        catch (IllegalArgumentException iae)
        {
            log.error(
                    "LDAP configuration is not correct. Please check its connection details.",
                    iae);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * disconnect - Disconnect from the LDAP server
     */
    public void disconnect()
    {
        provider.close();
    }

    /**
     * Authenticate user with its credentials and also check is validity,
     * availability.
     * 
     * @param nickName
     * @param password
     * @return
     */
    public LDAPUser authenticate(String nickName, String password)
    {
        boolean bAuthenticated = false;
        LDAPUser ldapuser = new LDAPUser(nickName);
        ChaiUser user = searchUser(nickName);
        if (user != null)
        {
            try
            {
                /*
                 * User is found so lets assume he is valid and then check all
                 * other conditions.
                 */
                bAuthenticated = true;
                Date loginExpirationTime = user
                        .readDateAttribute(LOGIN_EXPIRATION_TIME);
                /* Check various account validation scenario. */
                if (user.readBooleanAttribute(LOGIN_DISABLED))
                {
                    log.warn(nickName + " account login has been disabled.");
                    ldapuser.setLoginDisabled(true);
                    bAuthenticated = false;
                } else if (loginExpirationTime != null
                        && Calendar.getInstance().getTime()
                                .after(loginExpirationTime))
                {
                    log.warn(nickName + " account is expired");
                    ldapuser.setLoginExpirationTime(loginExpirationTime);
                    ldapuser.setPasswordExpired(true);
                    bAuthenticated = false;
                } else if (user.isPasswordLocked())
                {
                    log.warn(nickName + " account is locked.");
                    ldapuser.setPasswordLocked(true);
                    bAuthenticated = false;
                } else if (user.isPasswordExpired())
                {
                    log.warn(nickName + " password is expired.");
                    ldapuser.setPasswordExpired(true);
                    bAuthenticated = false;
                } else if (null != password && !user.testPassword(password))
                {
                    log.warn(nickName + " has provided the wrong password");
                    bAuthenticated = false;
                } else if (null == user.readStringAttribute(BROKER_OFFICE))
                {
                    log.warn(nickName + " does not have broker no associated.");
                    bAuthenticated = false;
                } else
                {
                    ldapuser.setLoginExpirationTime(loginExpirationTime);
                    ldapuser.setPassword(password);
                    ldapuser.setBrokerNumber(user
                            .readStringAttribute(BROKER_OFFICE));
                }
            } catch (ChaiUnavailableException cue)
            {
                log.error("LDAP Chai is unavailable for user operations.", cue);
                bAuthenticated = false;
            } catch (ChaiPasswordPolicyException cppe)
            {
                log.error("LDAP Chai password policy not met for user.", cppe);
                bAuthenticated = false;
            } catch (ChaiOperationException coe)
            {
                log.error(
                        "LDAP Chai operation could notbe performed on user object.",
                        coe);
                bAuthenticated = false;
            }
        }

        if (bAuthenticated)
            return ldapuser;
        else
            return null;
    }

    /**
     * Find user with its nickName/username and also check is required
     * parameter.
     * 
     * @param nickName
     * @return
     */
    public LDAPUser getUser(String nickName)
    {
        boolean bFound = false;
        LDAPUser ldapuser = new LDAPUser(nickName);
        ChaiUser user = searchUser(nickName);
        if (user != null)
        {
            try
            {
                bFound = true;
                if (null == user.readStringAttribute(BROKER_OFFICE))
                {
                    log.warn(nickName + " does not have broker no associated.");
                    bFound = false;
                } else
                {
                    ldapuser.setBrokerNumber(user
                            .readStringAttribute(BROKER_OFFICE));
                    ldapuser.setPassword(nickName);
                }
            } catch (ChaiUnavailableException cue)
            {
                log.error("LDAP Chai is unavailable for user operations.", cue);
                bFound = false;
            } catch (ChaiPasswordPolicyException cppe)
            {
                log.error("LDAP Chai password policy not met for user.", cppe);
                bFound = false;
            } catch (ChaiOperationException coe)
            {
                log.error(
                        "LDAP Chai operation could notbe performed on user object.",
                        coe);
                bFound = false;
            }
        }

        if (bFound)
            return ldapuser;
        else
            return null;
    }

    /**
     * Search the user in configured tree.
     * 
     * @param userCN
     * @return
     */
    public ChaiUser searchUser(String userCN)
    {
        boolean bFound = false;
        String userDN = "cn=" + userCN + ",ou=" + ldapContainer + ",o="
                + ldapRoot;
        ChaiUser user = null;
        try
        {
            /* Get a reference to the LDAP user object */
            user = ChaiFactory.createChaiUser(userDN, provider);
            /*
             * Need to try to get some attribute to find out whether user info
             * retrieved correctly or not.
             */
            user.isPasswordLocked();
            bFound = true;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            bFound = false;
        } catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            bFound = false;
        }

        if (!bFound)
        {
            /*
             * If user is not valid it falls into here to search on the
             * authNickName.
             */
            String filter = "(authNickName=" + userCN + ")";
            log.info("Looking up user using the search filter : " + filter);
            SearchHelper searchHelper = new SearchHelper();
            searchHelper.setFilter(filter);
            try
            {
                /* Note: empty string searches whole ldap namespace */
                Map<String, Map<String, String>> searchResult = provider
                        .search("ou=" + ldapContainer + ",o=" + ldapRoot,
                                searchHelper);
                if (searchResult.size() > 0)
                {
                    Set<String> keys = searchResult.keySet();
                    userDN = keys.iterator().next();
                    log.info("User found, DN looks like : " + userDN);
                } else
                {
                    // The search didn't find the userID supplied
                    log.warn("Couldn't find nickname " + userCN + "  in LDAP");
                }
                user = ChaiFactory.createChaiUser(userDN, provider);
                /*
                 * Need to try to get some attribute to find out whether user
                 * info retrieved correctly or not.
                 */
                user.isPasswordLocked();
            }/* If an error is encountered during the operation */
            catch (ChaiOperationException coe)
            {
                log.error(CHAI_OPERATION_EXCEPTION, coe);
            } catch (ChaiUnavailableException cue)
            {
                log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            }
        }

        return user;
    }

    /*
     * createUser - Attempt to create a new LDAP user object - No check is done
     * to ensure the user doesn't already exist, this method will fail (return
     * -1) if it already exists
     * 
     * @param userSN The Surname (SN) is required because it must be set when
     * the object is created
     * 
     * @return If an error occurs this method returns -1
     */
    public int createUser(String userCN, String userSN)
    {
        /*
         * Create a map object so that we can set certain required fields when
         * creating the new object
         */
        HashMap<String, String> props = new HashMap<String, String>();
        String userDN;

        userDN = "cn=" + userCN + ",ou=" + ldapContainer + ",o=" + ldapRoot;

        props.put(ChaiConstant.ATTR_LDAP_SURNAME, userSN);
        props.put(ChaiConstant.ATTR_LDAP_COMMON_NAME, userCN);

        try
        {
            /* Attempt to create the user in the LDAP directory */
            provider.createEntry(userDN,
                    ChaiConstant.OBJECTCLASS_BASE_LDAP_USER, props);
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }
        /* If the underlying connection is not in an available state */
        catch (IllegalStateException ise)
        {
            log.error(CHAI_ILLEGAL_STATE_EXCEPTION, ise);
            return -1;
        }

        return 0;
    }

    /*
     * deleteUser - Attempt to delete an existing LDAP user object - No check is
     * done to ensure the user already exists, this method will be sucessful
     * (return 0) even if the object never eixsted - The distinguished name (DN)
     * of the user to delete is all that is required - If an error occurs this
     * method returns -1
     */
    public int deleteUser(String userDN)
    {
        try
        {
            /* Attempt to delete the specified LDAP entry */
            provider.deleteEntry(userDN);
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }
        /* If the underlying connection is not in an available state */
        catch (IllegalStateException ise)
        {
            log.error(CHAI_ILLEGAL_STATE_EXCEPTION, ise);
            return -1;
        }

        return 0;
    }

    /*
     * addGroup - Attempts to add an LDAP user into an existing LDAP group - No
     * check is done to ensure the user or group is valid - If the user is
     * already in the group, this method will not attempt to add the user to the
     * group, and will return 0 - The distinguished name (DN) of both the user
     * and the group are required - If an error occurs this method returns -1
     */
    public int addGroup(String userDN, String groupDN)
    {

        try
        {

            /* Get a reference to the LDAP user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            /* Get a reference to the LDAP group object */
            ChaiGroup group = ChaiFactory.createChaiGroup(groupDN, provider);

            /* Check to see if the user is already in the group */
            ChaiGroup[] groupArray =
            {};

            groupArray = user.getGroups().toArray(groupArray);

            for (int i = 0; i < groupArray.length; i++)
            {
                if (groupArray[i].toString().equalsIgnoreCase(
                        (group.toString())))
                {
                    return 0;
                }
            }

            user.addGroupMembership(group);
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * removeGroup - Attempts to remove an LDAP user from an existing LDAP group
     * - No check is done to ensure the user or group is valid - If the user is
     * not already in the group, this method will not attempt to remove the user
     * from the group, and will return 0 - The distinguished name (DN) of both
     * the user and the group are required - If an error occurs this method
     * returns -1
     */
    public int removeGroup(String userDN, String groupDN)
    {

        try
        {

            /* Get a reference to the LDAP user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            /* Get a reference to the LDAP group object */
            ChaiGroup group = ChaiFactory.createChaiGroup(groupDN, provider);

            /* Check to see if the user is already in the group */
            ChaiGroup[] groupArray =
            {};

            groupArray = user.getGroups().toArray(groupArray);

            for (int i = 0; i < groupArray.length; i++)
            {
                if (groupArray[i].toString().equalsIgnoreCase(
                        (group.toString())))
                {
                    user.removeGroupMembership(group);
                    return 0;
                }
            }
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * addObjectClass - Used when initially creating a user to add object
     * classes to the user
     */
    public int addObjectClass(String userDN, String objClassName)
    {

        try
        {
            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            user.addAttribute(ChaiConstant.ATTR_LDAP_OBJECTCLASS, objClassName);
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * setAttribute - Calls setAttribute but defaults the "multiValueInd" to "N"
     * - This is merely a convenience method to save the user from having to
     * specify the multiValueInd
     */
    public int setAttribute(String userDN, String attrName, String attrValue)
    {
        return setAttribute(userDN, attrName, attrValue, "N");
    }

    /*
     * setAttribute - Attempt to add an attribute to an LDAP user - This method
     * can be used when creating AND updating the user - No check is done to
     * ensure the user is valid - If the attribute doesn't already exist, the
     * method will create it and set its value - If the attribute already
     * exists, this method will merely update its value - If this is a
     * multi-valued attribute, and you are attempting to append values to the
     * attribute, the multiValueInd should be set to "Y" - If an error occurs
     * this method returns -1
     */
    public int setAttribute(String userDN, String attrName, String attrValue,
            String multiValueInd)
    {

        try
        {

            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            String checkAttrValue = user.readStringAttribute(attrName);

            /*
             * A null in the attribute means that it does not exist, and needs
             * to be added
             */
            if (checkAttrValue == null || multiValueInd == "Y")
            {
                /*
                 * If you try to create an attribute with a null value, the
                 * update will fail
                 */
                if (attrValue != null)
                {
                    user.addAttribute(attrName, attrValue);
                }
            } else
            {
                user.writeStringAttribute(attrName, attrValue);
            }
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * getDnFromCn - given a Common Name (ie. user id), find the Distinguished
     * name corresponding to this object - if no matching object is found, this
     * method returns null - if an LDAP error occurrs, this method returns null
     */
    public String getDnFromCn(String userCN)
    {
        String userDN = "";
        String searchRootDN = "ou=" + ldapContainer + ",o=" + ldapRoot;

        try
        {

            ChaiEntry searchRoot = ChaiFactory.createChaiEntry(searchRootDN,
                    provider);

            ChaiEntry[] resultArray =
            {};

            resultArray = searchRoot.search("cn=" + userCN)
                    .toArray(resultArray);

            if (resultArray.length == 0)
            {
                return null;
            }

            userDN = resultArray[0].getEntryDN();
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return null;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return null;
        }

        return userDN;
    }

    /*
     * countMatchingEntries - given a base DN (empty string if the entire tree
     * is to be searched) and a valid LDAP search filter, will return a value
     * indicating whether or not the search found any results - if no error
     * occurs, the number of matches is returned - if an error occurs, -1 is
     * returned
     */
    public int countMatchingEntries(String baseDN, String filter)
    {
        try
        {
            SearchHelper searchHelper = new SearchHelper();
            searchHelper.setFilter(filter);
            return provider.search(baseDN, searchHelper).size();
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }
        /* If the underlying connection is not in an available state */
        catch (IllegalStateException ise)
        {
            log.error(CHAI_ILLEGAL_STATE_EXCEPTION, ise);
            return -1;
        }
    }

    /*
     * getLdapContainer - function to return the value in the ldapContainer
     * variable
     */
    public String getLdapContainer()
    {
        return ldapContainer;
    }

    /*
     * getLdapRoot - function to return the value in the ldapRoot variable
     */
    public String getLdapRoot()
    {
        return ldapRoot;
    }

    /*
     * getLdapURL - function to return the value in the ldapURL variable
     */
    public String getLdapURL()
    {
        return ldapURL;
    }

    /*
     * getRegionGroup - function to return the value in the regionGroup variable
     * regionGroup may contain an LDAP group that a new user id needs to be
     * added to for that region, i.e. volume, training, prop support. Some
     * regions won't have a group, i.e. dev, acceptance, production
     */
    public String getRegionGroup()
    {
        return regionGroup;
    }

    /*
     * generateRandomPassword - generates a random password based on the passed
     * in passwordMask - the mask should contain characters where characters are
     * required and digits where digits are required eg. "aaaaaaa0" will
     * generate 7 random characters followed by 1 random digit
     */
    public String generateRandomPassword(String passwordMask)
    {
        StringBuilder password = new StringBuilder();
        SecureRandom random;

        try
        {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException nsae)
        {
            log.error(
                    "Alogorithm is not available to generate random password.",
                    nsae);
            return null;
        }

        for (int i = 0; i < passwordMask.length(); i++)
        {
            // the next character in the mask is a number
            if (Character.isDigit(passwordMask.charAt(i)))
            {
                // will append a number between 0 and 9
                password.append(random.nextInt(9));
            }
            // the next character in the mask is not a number
            else
            {
                // 97 is the ascii character for lower case a
                // will append a letter between a and z
                password.append((char) (97 + random.nextInt(25)));
            }
        }

        return password.toString();
    }

    /*
     * setPassword - sets the passed in user's password to the passed in value -
     * if an LDAP error occurs -1 will be returned - if the password does not
     * conform to the password policy -1 will be returned
     */
    public int setPassword(String userDN, String newPassword)
    {

        try
        {
            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            user.setPassword(newPassword);
        }
        /* If the new password does not meet the user's password policy */
        catch (ChaiPasswordPolicyException cppe)
        {
            log.info("LDAP passworld policy does not met.", cppe);
            return -1;
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * expirePassword - puts the user's password into an expired state - if an
     * LDAP error occurs -1 will be returned
     */
    public int expirePassword(String userDN)
    {

        try
        {
            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            user.expirePassword();
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    /*
     * isLocked - checks to see if the user's account is currently locked
     * (usually due to intruder detection) - if user is locked, returns a 1 - if
     * user is not locked, returns a 0 - if an LDAP error occurs -1 will be
     * returned
     */
    public int isLocked(String userDN)
    {
        int isLocked = 0;

        try
        {

            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            if (user.isPasswordLocked())
            {
                isLocked = 1;
            } else
            {
                isLocked = 0;
            }
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return isLocked;
    }

    /*
     * unlockUser - attempts to unlock a user who is currently in a locked state
     * - if an LDAP error occurs -1 will be returned
     */
    public int unlockUser(String userDN)
    {
        try
        {
            /* Get a reference to the user object */
            ChaiUser user = ChaiFactory.createChaiUser(userDN, provider);

            user.unlockPassword();
        }
        /* If an error is encountered during the operation */
        catch (ChaiOperationException coe)
        {
            log.error(CHAI_OPERATION_EXCEPTION, coe);
            return -1;
        }
        /* If no directory servers are reachable */
        catch (ChaiUnavailableException cue)
        {
            log.error(CHAI_UNAVAILABLE_EXCEPTION, cue);
            return -1;
        }

        return 0;
    }

    public String loadProperty(String prop)
    {
        String value = "";
        String propertiesPath = "";

        try
        {
            Context initCtx = new InitialContext();
            propertiesPath = (String) initCtx
                    .lookup("java:comp/env/propertiesPath");
        } catch (NamingException ne)
        {
            log.fatal(
                    "Naming exception occured while retriving properties path.",
                    ne);
        }

        try
        {
            String propertiesFileName = "GISAuthServer.properties";
            Properties props = new Properties();
            String path = propertiesPath != null ? (propertiesPath + propertiesFileName)
                    : propertiesFileName;

            props.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path));
            value = props.getProperty(prop);
        } catch (Exception e)
        {
            log.fatal("Exception occured while loading LDAP property.", e);
        }

        return value;
    }

    private static String loadProperties()
    {
        String value = "";
        String propertiesPath = "";

        try
        {
            Context initCtx = new InitialContext();
            propertiesPath = (String) initCtx
                    .lookup("java:comp/env/propertiesPath");
        } catch (NamingException ne)
        {
            log.fatal(
                    "Naming exception occured while retriving properties path.",
                    ne);
        }

        try
        {
            String propertiesFileName = "GISAuthServer.properties";
            Properties props = new Properties();
            String path = propertiesPath != null ? (propertiesPath + propertiesFileName)
                    : propertiesFileName;

            InputStream is = Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream(path);
            if (is == null)
            {
                is = new FileInputStream(path);
            }
            props.load(is);

            ldapURL = props.getProperty("ldapURL");
            ldapAdminDN = props.getProperty("ldapAdminDN");
            ldapAdminPW = props.getProperty("ldapAdminPW");
            ldapRoot = props.getProperty("ldapRoot");
            ldapContainer = props.getProperty("ldapContainer");
            regionGroup = props.getProperty("regionGroup");
            if (regionGroup == null)
            {
                regionGroup = "";
            }
        } catch (Exception e)
        {
            log.fatal("Exception occured while loading LDAP properties.", e);
        }

        return value;
    }

    public class LDAPUser
    {
        private String username;
        private String passwd;
        private boolean loginDisabled = false;
        private boolean passwordLocked = false;
        private boolean passwordExpired = false;
        private Date loginExpirationTime;
        private String brokerNumber;

        public LDAPUser(String username)
        {
            this.username = username;
        }

        public String getUsername()
        {
            return username;
        }

        public String getPassword()
        {
            return passwd;
        }

        public void setPassword(String passwd)
        {
            this.passwd = passwd;
        }

        public boolean isLoginDisabled()
        {
            return loginDisabled;
        }

        public void setLoginDisabled(boolean loginDisabled)
        {
            this.loginDisabled = loginDisabled;
        }

        public boolean isPasswordLocked()
        {
            return passwordLocked;
        }

        public void setPasswordLocked(boolean passwordLocked)
        {
            this.passwordLocked = passwordLocked;
        }

        public boolean isPasswordExpired()
        {
            return passwordExpired;
        }

        public void setPasswordExpired(boolean passwordExpired)
        {
            this.passwordExpired = passwordExpired;
        }

        public Date getLoginExpirationTime()
        {
            return loginExpirationTime;
        }

        public void setLoginExpirationTime(Date loginExpirationTime)
        {
            this.loginExpirationTime = loginExpirationTime;
        }

        public String getBrokerNumber()
        {
            return brokerNumber;
        }

        public void setBrokerNumber(String brokerNumber)
        {
            this.brokerNumber = brokerNumber;
        }

    }

    public class LDAPNotAvailableException extends Exception
    {

        private static final long serialVersionUID = 1L;

        public LDAPNotAvailableException()
        {

        }

        public LDAPNotAvailableException(String strMessage)
        {
            super(strMessage);
        }
    }
}
