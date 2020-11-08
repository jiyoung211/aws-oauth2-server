package ca.sgicanada.security.dao ;

import java.sql.SQLException ;
import java.util.ArrayList ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;
import java.util.stream.Collectors ;

import org.apache.commons.lang3.StringUtils ;
import org.apache.commons.lang3.exception.ExceptionUtils ;
import org.apache.cxf.rs.security.oauth2.common.AuthenticationMethod ;
import org.apache.cxf.rs.security.oauth2.common.Client ;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission ;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken ;
import org.apache.cxf.rs.security.oauth2.common.UserSubject ;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant ;
import org.apache.cxf.rs.security.oauth2.tokens.bearer.BearerAccessToken ;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import ca.sgicanada.dao.StoredProcedureDAO ;
import ca.sgicanada.security.constant.OAuth2Constants ;

public class OAuth2DAO
{
  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OAuth2DAO.class) ;
  /**
   * JNDI name of OAuth2 database.
   */
  private static final String OAUTH2_DB_JNDI_NAME = "java:comp/env/jdbc/oauth" ;
  // "java:jdbc/oauth" ;
  // "java:comp/env/jdbc/oauth" ;
  // "java:jboss/jdbc/GISAuthServer"
  // ;
  /**
   * DAO object used for s (select) stored procedure calls to GIS.
   */

  private final StoredProcedureDAO spSelectDAO = new CallTableManyRows(OAUTH2_DB_JNDI_NAME, true) ;
  /**
   * DAO object used for i/u/d/o (insert/update/delete/other) stored procedure
   * calls to GIS.
   */
  private final StoredProcedureDAO spUpdateDAO = new CallTableUpdate(OAUTH2_DB_JNDI_NAME, true) ;

  /**
   * Persist OAuth permissions
   * 
   * @param oauthPermission
   *          individual permission
   */
  public void insertOAuthPermissions(OAuthPermission oauthPermission)
  {
    System.out.println("Inserting oauth permission " + oauthPermission.getPermission()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_permission", oauthPermission.getPermission(), oauthPermission.isDefaultPermission(), oauthPermission.getDescription(), oauthPermission.isInvisibleToClient()) ;
      int[] idx = { 0 } ;
      oauthPermission.getUris().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_permission_uris", oauthPermission.getPermission(), e, idx[0]++) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting oauth permission uris details", e1) ;
        }
      }) ;
      int[] idx1 = { 0 } ;
      oauthPermission.getHttpVerbs().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_permission_httpverbs", oauthPermission.getPermission(), e, idx1[0]++) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting oauth permission http verbs details", e1) ;
        }
      }) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting access token details", e) ;
    }
  }

  /**
   * Fetch oauth permission
   * 
   * @param permission
   *          for which details needs to be fetched.
   * @return OAuthPermission with available details
   */
  public OAuthPermission selectOAuthPermission(String permission)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    OAuthPermission oauthPermission = null ;
    System.out.println("Selecting data for permission: " + permission) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_permission", permission) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        oauthPermission = new OAuthPermission((String) hm.get("permission"), (String) hm.get("description")) ;
        oauthPermission.setInvisibleToClient((Boolean) hm.get("invisibletoclient")) ;
        oauthPermission.setDefaultPermission((Boolean) hm.get("defaultpermission")) ;

        try
        {
          final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_permission_httpverbs", oauthPermission.getPermission()) ;
          if (resultSet1 != null && !resultSet1.isEmpty())
          {
            List<String> httpVerbs = new ArrayList<>(resultSet1.size()) ;
            resultSet1.forEach(hmap -> httpVerbs.add((String) hmap.get("httpverbs"))) ;
            oauthPermission.setHttpVerbs(httpVerbs) ;
          }
        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
          LOG.error("SQLException occured while selecting oauthpermission http verbs", e) ;
        }

        try
        {
          final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_permission_uris", oauthPermission.getPermission()) ;
          if (resultSet1 != null && !resultSet1.isEmpty())
          {
            List<String> uris = new ArrayList<>(resultSet1.size()) ;
            resultSet1.forEach(hmap -> uris.add((String) hmap.get("uris"))) ;
            oauthPermission.setHttpVerbs(uris) ;
          }
        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
          LOG.error("SQLException occured while selecting oauthpermission uris", e) ;
        }

      }

      System.out.println("Oauth Persmission: " + oauthPermission) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      oauthPermission = null ;
    }
    return oauthPermission ;
  }

  /**
   * Persist end user login/username and authentication method details.
   * 
   * @param userSubject
   *          consist of user login details.
   */
  public void insertUserSubject(UserSubject userSubject)
  {
    System.out.println("Inserting user subject " + userSubject.getId()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_usersubject", userSubject.getId(), userSubject.getAuthenticationMethod() != null ? userSubject.getAuthenticationMethod().ordinal() : 0, userSubject.getLogin()) ;
      int[] idx = { 0 } ;
      userSubject.getRoles().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_usersubject_roles", userSubject.getId(), e, idx[0]++) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting user subject roles details", e1) ;
        }
      }) ;
      userSubject.getProperties().entrySet().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_usersubject_prop", userSubject.getId(), e.getKey(), e.getValue()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting user subject properties details", e1) ;
        }
      }) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting user subject details", e) ;
    }
  }

  /**
   * Fetch end user subject details.
   * 
   * @param subjectId
   *          login/username details
   * @return UserSubject object with available details.
   */
  public UserSubject selectUserSubject(String subjectId, String login)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    UserSubject userSubject = null ;
    System.out.println("Selecting data for user subject: " + subjectId + " login " + login) ;
    System.out.println("Selecting data for user subject: " + subjectId + " login " + login) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_usersubject", subjectId, login) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        userSubject = new UserSubject((String) hm.get("login"), (String) hm.get("id")) ;
        userSubject.setAuthenticationMethod(AuthenticationMethod.PASSWORD) ;
        System.out.println((String) hm.get("login") + ", " + (String) hm.get("id")) ;
        try
        {
          final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_usersubject_prop", userSubject.getId()) ;
          if (resultSet1 != null && !resultSet1.isEmpty())
          {
            Map<String, String> properties = new HashMap<>(resultSet1.size()) ;
            resultSet1.forEach(hmap -> properties.put((String) hmap.get("name"), (String) hmap.get("properties"))) ;
            userSubject.setProperties(properties) ;
          }
        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
          LOG.error("SQLException occured while selecting oauthpermission http verbs", e) ;
        }

        try
        {
          final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_usersubject_roles", userSubject.getId()) ;
          if (resultSet1 != null && !resultSet1.isEmpty())
          {
            List<String> roles = new ArrayList<>(resultSet1.size()) ;
            resultSet1.forEach(hmap -> roles.add((String) hmap.get("uris"))) ;
            userSubject.setRoles(roles) ;
          }
        }
        catch (SQLException e)
        {
          e.printStackTrace() ;
          LOG.error("SQLException occured while selecting oauthpermission uris", e) ;
        }

      }
      System.out.println("User Subject: " + userSubject) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      userSubject = null ;
    }
    return userSubject ;
  }

  /**
   * Persist access token and other related details.
   * 
   * @param serverAccessToken
   *          access token details.
   */
  public void insertAccessToken(ServerAccessToken serverAccessToken)
  {
    System.out.println("Inserting access token " + serverAccessToken.getTokenKey()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_accesstoken", serverAccessToken.getTokenKey(), serverAccessToken.getExpiresIn(), serverAccessToken.getIssuedAt(), serverAccessToken.getIssuer(), serverAccessToken.getRefreshToken(), serverAccessToken.getTokenType(), serverAccessToken.getClientCodeVerifier(), serverAccessToken.getGrantCode(), serverAccessToken.getGrantType(), serverAccessToken.getNonce(), serverAccessToken.getResponseType(), serverAccessToken.getClient() != null ? serverAccessToken.getClient().getClientId() : null, serverAccessToken.getSubject() != null ? serverAccessToken.getSubject().getId() : null) ;

      serverAccessToken.getParameters().entrySet().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_accesstoken_parm", serverAccessToken.getTokenKey(), e.getKey(), e.getValue()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting access token param details", e1) ;
        }
      }) ;

      serverAccessToken.getExtraProperties().entrySet().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_accesstoken_extraprop", serverAccessToken.getTokenKey(), e.getKey(), e.getValue()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting access token extra prop details", e1) ;
        }
      }) ;
      int[] idx = { 0 } ;
      serverAccessToken.getAudiences().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_accesstoken_audiences", serverAccessToken.getTokenKey(), idx[0]++, e) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting access token Audiences details", e1) ;
        }
      }) ;

      serverAccessToken.getScopes().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_accesstoken_oauthperm", serverAccessToken.getTokenKey(), e.getPermission()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting access token scopes/permissions details", e1) ;
        }
      }) ;

    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting access token details", e) ;
    }
    catch (Exception e)
    {
      e.printStackTrace() ;
      LOG.error("Exception occured while inserting access token details", e) ;
    }
  }

  /**
   * Fetch access token details
   * 
   * @param accessTokenKey
   *          key based on which details will be retrieved.
   * @return ServerAccessToken with available details.
   */
  public ServerAccessToken selectAccessTokenInfo(String accessTokenKey)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    ServerAccessToken serverAccessToken = null ;
    System.out.println("Selecting data for access token: " + accessTokenKey) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_accesstoken", accessTokenKey, null, null) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        serverAccessToken = new BearerAccessToken(null, (String) hm.get("tokenkey"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
        serverAccessToken.setIssuer((String) hm.get("issuer")) ;
        serverAccessToken.setGrantCode((String) hm.get("grantcode")) ;
        serverAccessToken.setGrantType((String) hm.get("granttype")) ;
        serverAccessToken.setNonce((String) hm.get("nonce")) ;
        serverAccessToken.setRefreshToken((String) hm.get("refreshtoken")) ;
        serverAccessToken.setResponseType((String) hm.get("responsetype")) ;
        serverAccessToken.setTokenType((String) hm.get("tokentype")) ;
        serverAccessToken.setClient(new Client((String) hm.get("client_clientid"), null, true)) ;
        serverAccessToken.setSubject(selectUserSubject((String) hm.get("subject_id"), null)) ;
        List<ServerAccessToken> lstServerAccessTokens = new ArrayList<>() ;
        lstServerAccessTokens.add(serverAccessToken) ;
        selectAccessTokensOtherDetails(lstServerAccessTokens) ;
      }
      else
      {
        System.out.println("No access toekn data returned for access toke key: " + accessTokenKey) ;
      }
      System.out.println("Access token info retrieved from DB for token key: " + accessTokenKey) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      serverAccessToken = null ;
    }
    return serverAccessToken ;
  }

  /**
   * Fetch access token details.
   * 
   * @param c
   *          client's client id based details will be retrieved.
   * @param resourceOwnerSubject
   *          user subject's login based details will be retrieved. This is
   *          optional input.
   * @return List of ServerAccessTokens with available details.
   */
  public List<ServerAccessToken> selectAccessTokens(Client c, UserSubject resourceOwnerSubject)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    List<ServerAccessToken> lstServerAccessTokens = null ;
    System.out.println("Selecting data for access token: " + c.getClientId()) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_accesstoken", null, resourceOwnerSubject != null ? resourceOwnerSubject.getLogin() : null, c.getClientId()) ;
      System.out.println("resultSet size : " + resultSet.size()) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        lstServerAccessTokens = resultSet.stream().map(hm -> {

          ServerAccessToken serverAccessToken = new BearerAccessToken(c, (String) hm.get("tokentype"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
          serverAccessToken.setIssuer((String) hm.get("issuer")) ;
          serverAccessToken.setGrantCode((String) hm.get("grantcode")) ;
          serverAccessToken.setGrantType((String) hm.get("granttype")) ;
          serverAccessToken.setNonce((String) hm.get("nonce")) ;
          serverAccessToken.setRefreshToken((String) hm.get("refreshtoken")) ;
          serverAccessToken.setResponseType((String) hm.get("responsetype")) ;
          serverAccessToken.setTokenType((String) hm.get("tokentype")) ;
          serverAccessToken.setClient(new Client((String) hm.get("client_clientid"), null, true)) ;
          serverAccessToken.setSubject(selectUserSubject((String) hm.get("subject_id"), null)) ;
          return serverAccessToken ;
        }).collect(Collectors.toList()) ;
        selectAccessTokensOtherDetails(lstServerAccessTokens) ;
      }
      else
      {
        System.out.println("No access toekn data returned for client id: " + c.getClientId()) ;
        System.out.println("No access toekn data returned for client id: " + c.getClientId()) ;
      }
      System.out.println("Access token info retrieved from DB for token key: " + c.getClientId() + " - " + lstServerAccessTokens) ;
      System.out.println("Access token info retrieved from DB for token key: " + c.getClientId() + " - " + lstServerAccessTokens) ;
    }
    catch (SQLException e)
    {
      System.out.println("SQLException occured while fetching access token details") ;
      System.out.println(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e), "\n")) ;
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      lstServerAccessTokens = null ;
    }
    return lstServerAccessTokens ;
  }

  /**
   * Fetch access token extra details.
   * 
   * @param lstServerAccessTokens
   *          list of access tokens with tokenkey.
   */
  private void selectAccessTokensOtherDetails(List<ServerAccessToken> lstServerAccessTokens)
  {
    lstServerAccessTokens.forEach(serverAccessToken -> {
      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_accesstoken_audiences", serverAccessToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<String> audiences = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> audiences.add((String) hm.get("audiences"))) ;
          serverAccessToken.setAudiences(audiences) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting access token audiences", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_accesstoken_extraprop", serverAccessToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          Map<String, String> extraProperties = new HashMap<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> extraProperties.put((String) hm.get("extrapropname"), (String) hm.get("extraproperties"))) ;
          serverAccessToken.setExtraProperties(extraProperties) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting access token extra props", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_accesstoken_oauthperm", serverAccessToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<OAuthPermission> scopes = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> {
            OAuthPermission oauthPermission = new OAuthPermission() ;
            oauthPermission.setPermission((String) hm.get("scopes_permission")) ;
            if (oauthPermission.getPermission().equalsIgnoreCase(OAuth2Constants.API_ACCESS_SCOPE))
              oauthPermission.setDefaultPermission(true) ;
            else
              oauthPermission.setDefaultPermission(false) ;

            if (oauthPermission.getPermission().equalsIgnoreCase(OAuth2Constants.REFRESH_TOKEN_SCOPE))
              oauthPermission.setInvisibleToClient(true) ;
            else
              oauthPermission.setDefaultPermission(false) ;

            scopes.add(oauthPermission) ;
          }) ;
          serverAccessToken.setScopes(scopes) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting access token permission/scopes", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_accesstoken_parm", serverAccessToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          Map<String, String> parameters = new HashMap<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> parameters.put((String) hm.get("PROPNAME"), (String) hm.get("PARAMETERS"))) ;
          serverAccessToken.setParameters(parameters) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting access token parameters", e) ;
      }
    }) ;
  }

  /**
   * Delete access token from persistence store. It will also delete all extra
   * details.
   * 
   * @param accessTokenKey
   *          token key which needs to be deleted.
   */
  public void deleteAccessToken(String accessTokenKey)
  {
    System.out.println("Deleting access token " + accessTokenKey) ;
    try
    {
      spUpdateDAO.callUpdateProc("d_oauth_beareraccesstoken", accessTokenKey) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while deleting access token details", e) ;
    }
  }

  /**
   * Persist access token and other related details.
   * 
   * @param refreshToken
   *          refresh token details.
   */
  public void insertRefreshToken(RefreshToken refreshToken)
  {
    System.out.println("Inserting refresh token " + refreshToken.getTokenKey()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_refreshtoken", refreshToken.getTokenKey(), refreshToken.getExpiresIn(), refreshToken.getIssuedAt(), refreshToken.getIssuer(), refreshToken.getRefreshToken(), refreshToken.getTokenType(), refreshToken.getClientCodeVerifier(), refreshToken.getGrantCode(), refreshToken.getGrantType(), refreshToken.getNonce(), refreshToken.getResponseType(), refreshToken.getClient().getClientId(), refreshToken.getSubject() != null ? refreshToken.getSubject().getId() : null) ;

      refreshToken.getParameters().entrySet().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_refreshtoken_parm", refreshToken.getTokenKey(), e.getKey(), e.getValue()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting refresh token param details", e1) ;
        }
      }) ;

      refreshToken.getExtraProperties().entrySet().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_refreshtoken_extraprop", refreshToken.getTokenKey(), e.getKey(), e.getValue()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting refresh token extra prop details", e1) ;
        }
      }) ;
      int[] idx = { 0 } ;
      refreshToken.getAudiences().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_refreshtoken_audiences", refreshToken.getTokenKey(), e, idx[0]++) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting refresh token Audiences details", e1) ;
        }
      }) ;

      refreshToken.getScopes().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_refreshtoken_oauthperm", refreshToken.getTokenKey(), e.getPermission()) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting refresh token scopes/permissions details", e1) ;
        }
      }) ;

      int[] idx1 = { 0 } ;
      refreshToken.getAccessTokens().forEach(e -> {
        try
        {
          spUpdateDAO.callUpdateProc("i_oauth_refreshtoken_access", refreshToken.getTokenKey(), e, idx1[0]++) ;
        }
        catch (SQLException e1)
        {
          e1.printStackTrace() ;
          LOG.error("SQLException occured while inserting refresh token access token details", e1) ;
        }
      }) ;

    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting refresh token details", e) ;
    }
  }

  public Client selectClient(String clientId)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    Client client = null ;
    // System.out.println("Selecting data for client: " + clientId) ;
    // try
    // {
    // resultSet = spSelectDAO.callProc("s_oauth_client", clientId) ;
    // if (resultSet != null && !resultSet.isEmpty())
    // {
    // HashMap<String, Object> hm = resultSet.get(0) ;
    // client = new Client() ;
    // client.setClientId((String) hm.get("clientid")) ;
    // client.setClientSecret((String) hm.get("clientsecret")) ;
    // client.setSubject(new UserSubject((String) hm.get("clientid"), (String)
    // hm.get("subject_id"))) ;
    //
    // List<String> uris = new ArrayList<>() ;
    // uris.add("http://localhost:7070/oauthServer.jsp") ;
    // uris.add("http://localhost:7070/loginTest.jsp") ;
    // System.out.println("## uri " + uris.get(0)) ;
    //
    // client.setRedirectUris(null) ;
    // if (StringUtils.equals((String) hm.get("confidential"), "1"))
    // {
    // client.setConfidential(true) ;
    // }
    // else
    // {
    // client.setConfidential(false) ;
    // }
    // client.setApplicationName((String) hm.get("applicationname")) ;
    // client.setApplicationWebUri((String) hm.get("applicationweburi")) ;
    // }
    // else
    // {
    // System.out.println("No client data returned for client id : " + clientId)
    // ;
    // }
    //
    // }
    // catch (SQLException e)
    // {
    // e.printStackTrace() ;
    // LOG.error("SQLException occured while fetching ", e) ;
    // client = null ;
    // }

    return client ;

  }

  public void insertClient(Client client)
  {
    System.out.println("insertClient") ;
    // System.out.println("Inserting server auth code grant " +
    // serverAuthorizationCodeGrant.getCode()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_client", client.getClientId(), client.getClientSecret(), client.getSubject().getId(), client.isConfidential() == true ? "1" : "0", client.getApplicationName(), client.getApplicationWebUri()) ;
      // client.getRedirectUris(uris) ;

    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting", e) ;
    }
  }

  /**
   * Fetch refresh token details
   * 
   * @param refreshTokenKey
   *          key based on which details will be retrieved.
   * @return RefreshToken with available details.
   */
  public RefreshToken selectRefreshTokenInfo(String refreshTokenKey)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    RefreshToken refreshToken = null ;
    System.out.println("Selecting data for refresh token: " + refreshTokenKey) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_refreshtoken", refreshTokenKey, null, null) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        refreshToken = new RefreshToken(null, (String) hm.get("tokenkey"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
        refreshToken.setIssuer((String) hm.get("issuer")) ;
        refreshToken.setGrantCode((String) hm.get("grantcode")) ;
        refreshToken.setGrantType((String) hm.get("granttype")) ;
        refreshToken.setNonce((String) hm.get("nonce")) ;
        refreshToken.setRefreshToken((String) hm.get("refreshtoken")) ;
        refreshToken.setResponseType((String) hm.get("responsetype")) ;
        refreshToken.setTokenType((String) hm.get("tokentype")) ;
        refreshToken.setClient(new Client((String) hm.get("client_clientid"), null, true)) ;
        refreshToken.setSubject(selectUserSubject((String) hm.get("subject_id"), null)) ;
        List<RefreshToken> lstRefreshTokens = new ArrayList<>() ;
        lstRefreshTokens.add(refreshToken) ;
        selectRefreshTokensOtherDetails(lstRefreshTokens) ;
        System.out.println("Refresh token info retrieved from DB for token key: " + refreshTokenKey) ;
      }
      else
      {
        System.out.println("No refresh token data returned for refresh token key: " + refreshTokenKey) ;
      }

    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      refreshToken = null ;
    }

    return refreshToken ;

  }

  /**
   * Fetch refresh token details
   * 
   * @param c
   *          client's client id based details will be retrieved.
   * @param resourceOwnerSubject
   *          user subject's login based details will be retrieved. This is
   *          optional input.
   * @return list of RefreshTokens with available details.
   */
  public List<RefreshToken> selectRefreshTokens(Client c, UserSubject resourceOwnerSubject)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    List<RefreshToken> lstRefreshTokens = null ;
    System.out.println("Selecting data for refresh token: " + c.getClientId()) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_refreshtoken", null, resourceOwnerSubject != null ? resourceOwnerSubject.getLogin() : null, c.getClientId()) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        lstRefreshTokens = resultSet.stream().map(hm -> {
          RefreshToken refreshToken = new RefreshToken(c, (String) hm.get("tokenkey"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
          refreshToken.setIssuer((String) hm.get("issuer")) ;
          refreshToken.setGrantCode((String) hm.get("grantcode")) ;
          refreshToken.setGrantType((String) hm.get("granttype")) ;
          refreshToken.setNonce((String) hm.get("nonce")) ;
          refreshToken.setResponseType((String) hm.get("responsetype")) ;
          refreshToken.setTokenType((String) hm.get("tokentype")) ;
          refreshToken.setClient(new Client((String) hm.get("client_clientid"), null, true)) ;
          refreshToken.setSubject(selectUserSubject((String) hm.get("subject_id"), null)) ;
          return refreshToken ;
        }).collect(Collectors.toList()) ;
        selectRefreshTokensOtherDetails(lstRefreshTokens) ;
      }
      else
      {
        System.out.println("No refresh token data returned for client id: " + c.getClientId()) ;
      }
      System.out.println("Refresh token info retrieved from DB for token key: " + c.getClientId() + " - " + lstRefreshTokens) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      lstRefreshTokens = null ;
    }
    return lstRefreshTokens ;
  }

  /**
   * Fetch refresh tokens other details.
   * 
   * @param lstRefreshTokens
   *          list of access tokens with token key.
   */
  private void selectRefreshTokensOtherDetails(List<RefreshToken> lstRefreshTokens)
  {
    lstRefreshTokens.forEach(refreshToken -> {

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_refreshtoken_access", refreshToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<String> accessTokens = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> accessTokens.add((String) hm.get("accesstokens"))) ;
          refreshToken.setAccessTokens(accessTokens) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting refresh token access tokens", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_refreshtoken_audiences", refreshToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<String> audiences = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> audiences.add((String) hm.get("audiences"))) ;
          refreshToken.setAudiences(audiences) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting refresh token audiences", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_refreshtoken_extraprop", refreshToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          Map<String, String> extraProperties = new HashMap<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> extraProperties.put((String) hm.get("extrapropname"), (String) hm.get("extraproperties"))) ;
          refreshToken.setExtraProperties(extraProperties) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting refresh token extra props", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_refreshtoken_oauthperm", refreshToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<OAuthPermission> scopes = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> scopes.add(selectOAuthPermission((String) hm.get("scopes_permission")))) ;
          refreshToken.setScopes(scopes) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting refresh token permission/scopes", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_refreshtoken_parm", refreshToken.getTokenKey()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          Map<String, String> parameters = new HashMap<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> parameters.put((String) hm.get("propname"), (String) hm.get("parameters"))) ;
          refreshToken.setParameters(parameters) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting refresh token parameters", e) ;
      }
    }) ;
  }

  /**
   * Delete refresh token with other details.
   * 
   * @param refreshTokenKey
   *          token key to be deleted.
   */
  public void deleteRefreshToken(String refreshTokenKey)
  {
    System.out.println("Deleting refresh token " + refreshTokenKey) ;
    try
    {
      spUpdateDAO.callUpdateProc("d_oauth_refreshtoken", refreshTokenKey) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while deleting refresh token details", e) ;
    }
  }

  /**
   * Binds access token and refresh token. update refreshtoken key in
   * accesstoken.
   * 
   * @param refreshTokenKey
   *          refresh token key
   * @param accessTokenKey
   *          access token key
   */
  public void updateRefreshTokenInAccessToken(String refreshTokenKey, String accessTokenKey)
  {
    System.out.println("updating access token " + accessTokenKey + " with refresh token: " + refreshTokenKey) ;
    try
    {
      spUpdateDAO.callUpdateProc("u_oauth_accesstoken", refreshTokenKey, accessTokenKey) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while updating access token details", e) ;
    }
  }

  /**
   * Persist Server code grant
   * 
   * @param serverAuthorizationCodeGrant
   *          individual permission
   */
  public void insertServerAuthCodeGrants(ServerAuthorizationCodeGrant serverAuthorizationCodeGrant)
  {
    System.out.println("insertServerAuthCodeGrants") ;
    // System.out.println("Inserting server auth code grant " +
    // serverAuthorizationCodeGrant.getCode()) ;
    try
    {
      spUpdateDAO.callUpdateProc("i_oauth_serverauthcodegrant", serverAuthorizationCodeGrant.getCode(), serverAuthorizationCodeGrant.getCodeVerifier(), serverAuthorizationCodeGrant.getRedirectUri(), serverAuthorizationCodeGrant.getAudience(), serverAuthorizationCodeGrant.getClientCodeChallenge(), serverAuthorizationCodeGrant.getExpiresIn(), serverAuthorizationCodeGrant.getIssuedAt(), serverAuthorizationCodeGrant.getNonce(), serverAuthorizationCodeGrant.isPreauthorizedTokenAvailable(), serverAuthorizationCodeGrant.getResponseType(), serverAuthorizationCodeGrant.getClient().getClientId(), serverAuthorizationCodeGrant.getSubject() != null ? serverAuthorizationCodeGrant.getSubject().getId() : null) ;
      // int[] idx = { 0 } ;
      // serverAuthorizationCodeGrant.getApprovedScopes().forEach(e -> {
      // try
      // {
      // spUpdateDAO.callUpdateProc("i_oauth_serverauthcodegrant_approvedscopes",
      // serverAuthorizationCodeGrant.getCode(), e, idx[0]++) ;
      // }
      // catch (SQLException e1)
      // {
      // e1.printStackTrace() ;
      // LOG.error("SQLException occured while inserting server auth code grant approved scopes details",
      // e1) ;
      // }
      // }) ;
      // int[] idx1 = { 0 } ;
      // serverAuthorizationCodeGrant.getRequestedScopes().forEach(e -> {
      // try
      // {
      // spUpdateDAO.callUpdateProc("i_oauth_serverauthcodegrant_req_scopes",
      // serverAuthorizationCodeGrant.getCode(), e, idx1[0]++) ;
      // }
      // catch (SQLException e1)
      // {
      // e1.printStackTrace() ;
      // LOG.error("SQLException occured while inserting server auth code grant req scopes details",
      // e1) ;
      // }
      // }) ;
      // serverAuthorizationCodeGrant.getExtraProperties().entrySet().forEach(e
      // -> {
      // try
      // {
      // spUpdateDAO.callUpdateProc("i_oauth_serverauthcodegrant_extraprop",
      // serverAuthorizationCodeGrant.getCode(), e.getKey(), e.getValue()) ;
      // }
      // catch (SQLException e1)
      // {
      // e1.printStackTrace() ;
      // LOG.error("SQLException occured while inserting server auth code grant extra prop details",
      // e1) ;
      // }
      // }) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting access token details", e) ;
    }
  }

  public ServerAuthorizationCodeGrant selectServerAuthorizationCodeGrantInfo(String code)
  {
    ArrayList<HashMap<String, Object>> resultSet = null ;
    ServerAuthorizationCodeGrant serverAuthorizationCodeGrant = null ;
    System.out.println("Selecting data for server auth code grant: " + code) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_serverauthcodegrant", code, null, null) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        serverAuthorizationCodeGrant = new ServerAuthorizationCodeGrant(null, (String) hm.get("code"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
        serverAuthorizationCodeGrant.setAudience((String) hm.get("audience")) ;
        serverAuthorizationCodeGrant.setClientCodeChallenge((String) hm.get("audclientcodechallengeience")) ;
        serverAuthorizationCodeGrant.setCodeVerifier((String) hm.get("codeverifier")) ;
        serverAuthorizationCodeGrant.setNonce((String) hm.get("nonce")) ;
        if (StringUtils.equals((String) hm.get("preauthorizedtokenavailable"), "1"))
        {
          serverAuthorizationCodeGrant.setPreauthorizedTokenAvailable(true) ;
        }
        else
        {
          serverAuthorizationCodeGrant.setPreauthorizedTokenAvailable(false) ;
        }
        serverAuthorizationCodeGrant.setRedirectUri((String) hm.get("redirecturi")) ;
        serverAuthorizationCodeGrant.setResponseType((String) hm.get("responsetype")) ;
        serverAuthorizationCodeGrant.setClient(new Client((String) hm.get("client_clientid"), null, true)) ;
        serverAuthorizationCodeGrant.setSubject(selectUserSubject((String) hm.get("subject_id"), null)) ;
        List<ServerAuthorizationCodeGrant> lstServerAuthorizationCodeGrants = new ArrayList<>() ;
        lstServerAuthorizationCodeGrants.add(serverAuthorizationCodeGrant) ;
        // selectServerCodeGrantsOtherDetails(lstServerAuthorizationCodeGrants)
        // ;
      }
      else
      {
        System.out.println("No server auth code grant data returned for access toke key: " + code) ;
      }
      System.out.println("Server auth code grant info retrieved from DB for token key: " + code) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching server auth code grant details", e) ;
      serverAuthorizationCodeGrant = null ;
    }
    return serverAuthorizationCodeGrant ;
  }

  /**
   * Fetch server code grants
   * 
   * @param c
   *          client's client id based details will be retrieved.
   * @param resourceOwnerSubject
   *          user subject's login based details will be retrieved. This is
   *          optional input.
   * @return List of ServerAuthorizationCodeGrant with available details
   */
  public List<ServerAuthorizationCodeGrant> selectServerAuthCodeGrants(Client c, UserSubject resourceOwnerSubject)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    List<ServerAuthorizationCodeGrant> lstServerAuthorizationCodeGrants = null ;
    System.out.println("Selecting data for code grant: " + c.getClientId()) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_serverauthcodegrant", c.getClientId(), resourceOwnerSubject != null ? resourceOwnerSubject.getId() : null) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        lstServerAuthorizationCodeGrants = resultSet.stream().map(hm -> {
          ServerAuthorizationCodeGrant serverAuthorizationCodeGrant = new ServerAuthorizationCodeGrant(c, (String) hm.get("code"), (long) hm.get("expiresin"), (long) hm.get("issuedat")) ;
          serverAuthorizationCodeGrant.setAudience((String) hm.get("audience")) ;
          serverAuthorizationCodeGrant.setClientCodeChallenge((String) hm.get("audclientcodechallengeience")) ;
          serverAuthorizationCodeGrant.setCodeVerifier((String) hm.get("codeverifier")) ;
          serverAuthorizationCodeGrant.setNonce((String) hm.get("nonce")) ;
          // serverAuthorizationCodeGrant.setPreauthorizedTokenAvailable((Boolean)
          // hm.get("preauthorizedtokenavailable")) ;
            if (StringUtils.equals((String) hm.get("preauthorizedtokenavailable"), "1"))
            {
              serverAuthorizationCodeGrant.setPreauthorizedTokenAvailable(true) ;
            }
            else
            {
              serverAuthorizationCodeGrant.setPreauthorizedTokenAvailable(false) ;
            }
            serverAuthorizationCodeGrant.setRedirectUri((String) hm.get("redirecturi")) ;
            serverAuthorizationCodeGrant.setResponseType((String) hm.get("responsetype")) ;
            serverAuthorizationCodeGrant.setClient(c) ;
            serverAuthorizationCodeGrant.setSubject(resourceOwnerSubject) ;
            return serverAuthorizationCodeGrant ;
          }).collect(Collectors.toList()) ;
        selectServerCodeGrantsOtherDetails(lstServerAuthorizationCodeGrants) ;
      }
      System.out.println("Server Auth code grants : " + lstServerAuthorizationCodeGrants) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching server auth code grants", e) ;
      lstServerAuthorizationCodeGrants = null ;
    }
    return lstServerAuthorizationCodeGrants ;
  }

  /**
   * Fetch server auth code grant other details.
   * 
   * @param lstServerAuthorizationCodeGrants
   *          list of server auth code grants with token key.
   */
  private void selectServerCodeGrantsOtherDetails(List<ServerAuthorizationCodeGrant> lstServerAuthorizationCodeGrants)
  {
    lstServerAuthorizationCodeGrants.forEach(serverAuthorizationCodeGrant -> {

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_serverauthcodegrant_aprv_scopes", serverAuthorizationCodeGrant.getCode()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<String> approvedScopes = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> approvedScopes.add((String) hm.get("approvedscopes"))) ;
          serverAuthorizationCodeGrant.setApprovedScopes(approvedScopes) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting approved scopes for server auth code grants", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_serverauthcodegrant_req_scopes", serverAuthorizationCodeGrant.getCode()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          List<String> requestedScopes = new ArrayList<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> requestedScopes.add((String) hm.get("requestedscopes"))) ;
          serverAuthorizationCodeGrant.setRequestedScopes(requestedScopes) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting requested scopes for server auth code grants", e) ;
      }

      try
      {
        final ArrayList<HashMap<String, Object>> resultSet1 = spSelectDAO.callProc("s_oauth_serverauthcodegrant_extr_prop", serverAuthorizationCodeGrant.getCode()) ;
        if (resultSet1 != null && !resultSet1.isEmpty())
        {
          Map<String, String> extraProperties = new HashMap<>(resultSet1.size()) ;
          resultSet1.forEach(hm -> extraProperties.put((String) hm.get("extrapropname"), (String) hm.get("extraproperties"))) ;
          serverAuthorizationCodeGrant.setExtraProperties(extraProperties) ;
        }
      }
      catch (SQLException e)
      {
        e.printStackTrace() ;
        LOG.error("SQLException occured while selecting extra props for server auth code grants", e) ;
      }
    }) ;
  }

  /**
   * Delete refresh token with other details.
   * 
   * @param refreshTokenKey
   *          token key to be deleted.
   */
  public void deleteServerAuthorizationCodeGrant(String codeGrant)
  {
    System.out.println("Deleting server auth code grant " + codeGrant) ;
    try
    {
      spUpdateDAO.callUpdateProc("d_oauth_serverauth", codeGrant) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while deleting server auth code grant details", e) ;
    }
  }

  /**
   * Persist Sesssion Authenticity token
   * 
   * @param sessionAuthToken
   *          individual permission
   */
  public void insertSessionAuthToken(String sessionAuthToken, long expiresin, String clientId, String userSubjectId)
  {
    System.out.println("Inserting session Auth Token " + sessionAuthToken) ;
    try
    {
      System.out.println("sessionAuthToken " + sessionAuthToken) ;
      System.out.println("expiresin " + expiresin) ;
      System.out.println("clientId " + clientId) ;
      System.out.println("userSubjectId " + userSubjectId) ;

      spUpdateDAO.callUpdateProc("i_oauth_sessionauthenticitytoken", sessionAuthToken, expiresin, clientId, userSubjectId) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while inserting session auth token details", e) ;
    }
  }

  /**
   * Fetch Session Authenticity Token
   * 
   * @param sessionAuthToken
   *          for which details needs to be fetched.
   * @return OAuthPermission with available details
   */
  public String selectSessionAuthToken(String sessionAuthToken)
  {

    ArrayList<HashMap<String, Object>> resultSet = null ;
    String rtSessionAuthToken = null ;
    System.out.println("Selecting data for permission: " + sessionAuthToken) ;
    try
    {
      resultSet = spSelectDAO.callProc("s_oauth_sessionauthenticitytoken", sessionAuthToken) ;
      if (resultSet != null && !resultSet.isEmpty())
      {
        HashMap<String, Object> hm = resultSet.get(0) ;
        rtSessionAuthToken = (String) hm.get("sessionauthtoken") ;
      }
      System.out.println("Session Auth Token: " + rtSessionAuthToken) ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while fetching access token details", e) ;
      rtSessionAuthToken = null ;
    }
    return rtSessionAuthToken ;
  }

  /**
   * Delete session authenticity token details.
   * 
   * @param sessionAuthToken
   *          token key to be deleted.
   */
  public String deleteSessionAuthToken(String sessionAuthToken)
  {
    System.out.println("Deleting Session Token " + sessionAuthToken) ;
    try
    {
      if (selectSessionAuthToken(sessionAuthToken) != null)
        spUpdateDAO.callUpdateProc("d_oauth_sessionauthenticitytoken", sessionAuthToken, null, null) ;
      else
        return null ;
    }
    catch (SQLException e)
    {
      e.printStackTrace() ;
      LOG.error("SQLException occured while deleting session token details", e) ;
      return null ;
    }
    return sessionAuthToken ;
  }

}