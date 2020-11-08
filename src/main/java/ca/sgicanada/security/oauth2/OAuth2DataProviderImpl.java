package ca.sgicanada.security.oauth2 ;

import java.util.ArrayList ;
import java.util.Collections ;
import java.util.HashMap ;
import java.util.LinkedList ;
import java.util.List ;
import java.util.Map ;
import java.util.stream.Collectors ;

import org.apache.cxf.rs.security.jose.jwt.JwtClaims ;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration ;
import org.apache.cxf.rs.security.oauth2.common.AuthenticationMethod ;
import org.apache.cxf.rs.security.oauth2.common.Client ;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission ;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken ;
import org.apache.cxf.rs.security.oauth2.common.UserSubject ;
import org.apache.cxf.rs.security.oauth2.grants.code.AbstractCodeDataProvider ;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider ;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration ;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant ;
import org.apache.cxf.rs.security.oauth2.provider.AbstractOAuthDataProvider ;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException ;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import ca.sgicanada.security.constant.OAuth2Constants ;
import ca.sgicanada.security.dao.OAuth2DAO ;
import ca.sgicanada.util.LDAPUtils ;
import ca.sgicanada.util.LDAPUtils.LDAPUser ;

public class OAuth2DataProviderImpl extends AbstractOAuthDataProvider implements AuthorizationCodeDataProvider
{

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(OAuth2DAO.class) ;

  private OAuth2DAO oauth2DAO ;

  private LDAPUtils ldapUtils ;

  private long codeLifetime = 600 ;

  @Override
  public void checkRequestedScopes(Client client, List<String> requestedScopes)
  {
    String grantType = super.getCurrentRequestedGrantType() ;
    /*
     * If required scope is not included in approved scopes for access token
     * request then do not generate access token.
     */
    if (grantType != null && !requestedScopes.contains(OAuth2Constants.API_ACCESS_SCOPE))
    {
      System.out.println("throw new OAuthServiceException(\"Required scopes are missing\")") ;
      // throw new OAuthServiceException("Required scopes are missing") ;
    }
  }

  @Override
  public String getCurrentRequestedGrantType()
  {
    return super.getCurrentRequestedGrantType() ;
  }

  /**
   * Authenticate client credentials and return Client object.
   */
  // @Override
  // public Client doGetClient(final String clientId) throws
  // OAuthServiceException
  // {
  // Client client = null ;
  // String pwd = getCurrentClientSecret() ;
  // LDAPUser user = null ;
  // try
  // {
  // if (pwd != null)
  // user = ldapUtils.authenticate(clientId, pwd) ;
  // else
  // user = ldapUtils.getUser(clientId) ;
  //
  // if (user != null)
  // {
  // client = toClient(user) ;
  // }
  // }
  // catch (Exception e)
  // {
  // System.out.println(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(e),
  // "\n")) ;
  // throw new
  // OAuthServiceException("Could not found client due to unavailablity of identity server.")
  // ;
  // }
  // return client ;
  // }

  @Override
  public Client doGetClient(final String clientId) throws OAuthServiceException
  {
    System.out.println("dataProvider doGetClient clientId " + clientId) ;
    Client client = oauth2DAO.selectClient(clientId) ;
    if (client == null)
    {
      System.out.println("getDummyClient") ;
      client = getDummyClient(clientId) ;
    }
    if (client.getRedirectUris().size() == 0)
    {
      System.out.println("client getRedirectUris size 0 ") ;

      List<String> uris = new ArrayList<>() ;
      uris.add("http://localhost:7070/oauthServer.jsp") ;
      uris.add("http://localhost:7070/login.jsp") ;
      uris.add("http://localhost:7070/") ;

      client.setRedirectUris(uris) ;

    }

    return client ;
  }

  @Override
  public List<OAuthPermission> convertScopeToPermissions(Client client, List<String> requestedScopes)
  {
    System.out.println("convertScopeToPermissions") ;
    // TODO Auto-generated method stub
    return super.convertScopeToPermissions(client, requestedScopes) ;
  }

  public Client getDummyClient(String clientId)
  {

    UserSubject userSubject = new UserSubject() ;
    userSubject.setId(clientId) ;

    List<String> uris = new ArrayList<>() ;
    uris.add("http://localhost:7070/oauthServer.jsp") ;
    uris.add("http://localhost:7070/login.jsp") ;
    uris.add("http://localhost:7070/") ;

    Client registeredClient = new Client() ;
    registeredClient.setClientId(clientId) ;
    registeredClient.setClientSecret(clientId) ;
    registeredClient.setSubject(userSubject) ;
    registeredClient.setRedirectUris(uris) ;
    registeredClient.setConfidential(true) ;
    registeredClient.setApplicationName("Broker Application") ;
    registeredClient.setApplicationWebUri("http://broker.com") ;

    return registeredClient ;
  }

  @Override
  public String getCurrentClientSecret()
  {
    return super.getCurrentClientSecret() ;
  }

  public void setClient(final Client client)
  {
    /*
     * Do nothing as we are using LDAP for third party client and their staff so
     * no need to remove in any oauth2 flow.
     */
  }

  // ldap 사용 x -> 임시
  public void createClient(final Client client)
  {
    oauth2DAO.insertClient(client) ;
  }

  @Override
  public List<Client> getClients(final UserSubject resourceOwner)
  {
    /*
     * Do nothing as we are using LDAP for third party client and their staff so
     * no need to remove in any oauth2 flow.
     */
    return Collections.emptyList() ;
  }

  @Override
  public List<ServerAccessToken> getAccessTokens(final Client c, final UserSubject sub)
  {
    return oauth2DAO.selectAccessTokens(c, sub) ;
  }

  @Override
  public List<RefreshToken> getRefreshTokens(final Client c, final UserSubject sub)
  {
    return oauth2DAO.selectRefreshTokens(c, sub) ;
  }

  @Override
  public ServerAccessToken getAccessToken(final String accessToken) throws OAuthServiceException
  {
    return oauth2DAO.selectAccessTokenInfo(accessToken) ;
  }

  @Override
  protected void doRevokeAccessToken(final ServerAccessToken at)
  {
    oauth2DAO.deleteAccessToken(at.getTokenKey()) ;
  }

  @Override
  protected void linkRefreshTokenToAccessToken(final RefreshToken rt, final ServerAccessToken at)
  {
    super.linkRefreshTokenToAccessToken(rt, at) ;
    oauth2DAO.updateRefreshTokenInAccessToken(rt.getTokenKey(), at.getTokenKey()) ;
  }

  @Override
  protected RefreshToken getRefreshToken(final String refreshTokenKey)
  {
    return oauth2DAO.selectRefreshTokenInfo(refreshTokenKey) ;
  }

  @Override
  protected void doRevokeRefreshToken(final RefreshToken rt)
  {
    oauth2DAO.deleteAccessToken(rt.getTokenKey()) ;
  }

  @Override
  protected ServerAccessToken doCreateAccessToken(AccessTokenRegistration atReg)
  {
    System.out.println("doCreateAccessToken") ;
    ServerAccessToken at = super.doCreateAccessToken(atReg) ;
    // we override this in order to get rid of elementCollections directly
    // injected
    // from another entity
    // this can be the case when using multiple cmt dataProvider operation in a
    // single entityManager
    // lifespan
    System.out.println("at : " + at) ;
    if (at.getAudiences() != null)
    {
      at.setAudiences(new ArrayList<>(at.getAudiences())) ;
    }
    if (at.getExtraProperties() != null)
    {
      at.setExtraProperties(new HashMap<String, String>(at.getExtraProperties())) ;
    }
    if (at.getScopes() != null)
    {
      at.setScopes(new ArrayList<>(at.getScopes())) ;
    }
    if (at.getParameters() != null)
    {
      at.setParameters(new HashMap<String, String>(at.getParameters())) ;
    }
    return at ;
  }

  protected void saveAccessToken(final ServerAccessToken serverToken)
  {
    System.out.println("saveAccessToken") ;
    List<OAuthPermission> perms = new LinkedList<OAuthPermission>() ;
    for (OAuthPermission perm : serverToken.getScopes())
    {
      OAuthPermission permSaved = oauth2DAO.selectOAuthPermission(perm.getPermission()) ;
      if (permSaved != null)
      {
        perms.add(permSaved) ;
      }
      else
      {
        oauth2DAO.insertOAuthPermissions(perm) ;
        perms.add(perm) ;
      }
    }
    serverToken.setScopes(perms) ;

    if (serverToken.getSubject() != null)
    {
      UserSubject sub = oauth2DAO.selectUserSubject(serverToken.getSubject().getId(), null) ;
      if (sub == null)
      {
        oauth2DAO.insertUserSubject(serverToken.getSubject()) ;
      }
      else
      {
        serverToken.setSubject(sub) ;
      }
    }

    if (serverToken.getClient() != null)
    {
      serverToken.setClient(doGetClient(serverToken.getClient().getClientId())) ;
    }
    oauth2DAO.insertAccessToken(serverToken) ;
  }

  @Override
  protected void saveRefreshToken(RefreshToken refreshToken)
  {
    oauth2DAO.insertRefreshToken(refreshToken) ;
  }

  protected ServerAccessToken doRefreshAccessToken(Client client, RefreshToken oldRefreshToken, List<String> restrictedScopes)
  {
    ServerAccessToken at = super.doRefreshAccessToken(client, oldRefreshToken, restrictedScopes) ;

    if (null != client.getProperties())
    {
      at.getExtraProperties().putAll(client.getProperties()) ;
    }

    if (isUseJwtFormatForAccessTokens())
    {
      JwtClaims claims = createJwtAccessToken(at) ;
      String jose = processJwtAccessToken(claims) ;
      at.setTokenKey(jose) ;
    }

    return at ;
  }

  @Override
  public ServerAuthorizationCodeGrant createCodeGrant(AuthorizationCodeRegistration reg) throws OAuthServiceException
  {
    ServerAuthorizationCodeGrant grant = doCreateCodeGrant(reg) ;
    saveCodeGrant(grant) ;
    return grant ;
  }

  protected ServerAuthorizationCodeGrant doCreateCodeGrant(AuthorizationCodeRegistration reg) throws OAuthServiceException
  {
    return AbstractCodeDataProvider.initCodeGrant(reg, codeLifetime) ;
  }

  protected void saveCodeGrant(final ServerAuthorizationCodeGrant grant)
  {
    System.out.println("saveCodeGrant") ;
    /* Server code grant is currently not supported. */
    if (grant.getSubject() != null)
    {
      UserSubject sub = oauth2DAO.selectUserSubject(grant.getSubject().getId(), null) ;
      if (sub == null)
      {
        oauth2DAO.insertUserSubject(grant.getSubject()) ;
      }
    }

    if (grant.getClient() != null)
    {
      grant.setClient(doGetClient(grant.getClient().getClientId())) ;
    }
    oauth2DAO.insertServerAuthCodeGrants(grant) ;

  }

  @Override
  protected void doRemoveClient(final Client c)
  {
    removeClientCodeGrants(c) ;
    /*
     * Do nothing as we are using LDAP for third party client and their staff so
     * no need to remove in any oauth2 flow.
     */
  }

  protected void removeClientCodeGrants(final Client c)
  {
    for (ServerAuthorizationCodeGrant grant : getCodeGrants(c, null))
    {
      removeCodeGrant(grant.getCode()) ;
    }
  }

  @Override
  public ServerAuthorizationCodeGrant removeCodeGrant(final String code) throws OAuthServiceException
  {
    /* Server code grant is currently not supported. */
    ServerAuthorizationCodeGrant serverAuthorizationCodeGrant = oauth2DAO.selectServerAuthorizationCodeGrantInfo(code) ;
    System.out.println("##1 serverAuthorizationCodeGrant " + serverAuthorizationCodeGrant) ;
    if (serverAuthorizationCodeGrant != null)
      oauth2DAO.deleteServerAuthorizationCodeGrant(code) ;
    System.out.println("##2 serverAuthorizationCodeGrant " + serverAuthorizationCodeGrant) ;

    return serverAuthorizationCodeGrant ;
  }

  @Override
  public List<ServerAuthorizationCodeGrant> getCodeGrants(final Client c, final UserSubject subject) throws OAuthServiceException
  {
    /* Server code grant is currently not supported. */
    return oauth2DAO.selectServerAuthCodeGrants(c, subject) ;
  }

  public void setOauth2DAO(OAuth2DAO oauth2dao)
  {
    oauth2DAO = oauth2dao ;
  }

  public void setCodeLifetime(long codeLifetime)
  {
    this.codeLifetime = codeLifetime ;
  }

  private Client toClient(LDAPUser user)
  {
    List<String> registeredScopes = getPermissionMap().entrySet().stream().map(e -> e.getValue().getPermission()).collect(Collectors.toList()) ;

    List<String> allowedGrantTypes = new ArrayList<>() ;
    allowedGrantTypes.add(OAuthConstants.AUTHORIZATION_CODE_GRANT) ;
    allowedGrantTypes.add(OAuthConstants.IMPLICIT_GRANT) ;
    allowedGrantTypes.add(OAuthConstants.RESOURCE_OWNER_GRANT) ;
    allowedGrantTypes.add(OAuthConstants.CLIENT_CREDENTIALS_GRANT) ;
    allowedGrantTypes.add(OAuthConstants.REFRESH_TOKEN_GRANT) ;

    Map<String, String> extraProperties = new HashMap<>() ;
    extraProperties.put(OAuth2Constants.BROKER_NUMBER, user.getBrokerNumber()) ;

    UserSubject clientUserSubject = new UserSubject(user.getUsername(), user.getUsername()) ;
    clientUserSubject.setAuthenticationMethod(AuthenticationMethod.PASSWORD) ;

    /*
     * List<String> redirectURIs = new ArrayList<>(); redirectURIs.add(
     * "http://localhost:13080/broker-server-1.0-SNAPSHOT/view-quote");
     */

    Client client = new Client() ;
    client.setClientId(user.getUsername()) ;
    client.setClientSecret(user.getPassword()) ;
    client.setApplicationName(user.getUsername()) ;
    client.setConfidential(true) ;
    client.setSubject(clientUserSubject) ;
    client.setRegisteredDynamically(false) ;
    client.setRegisteredScopes(registeredScopes) ;
    client.setAllowedGrantTypes(allowedGrantTypes) ;
    client.setProperties(extraProperties) ;
    // client.setRedirectUris(redirectURIs);

    return client ;
  }

  public void setLdapUtils(LDAPUtils ldapUtils)
  {
    this.ldapUtils = ldapUtils ;
  }

}
