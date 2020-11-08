package ca.sgicanada.security.service ;

import java.net.URI ;
import java.net.URISyntaxException ;
import java.util.ArrayList ;
import java.util.Collection ;
import java.util.Collections ;
import java.util.Comparator ;
import java.util.HashMap ;
import java.util.HashSet ;
import java.util.List ;
import java.util.Map ;
import java.util.Set ;
import java.util.TreeSet ;

import javax.ws.rs.Consumes ;
import javax.ws.rs.FormParam ;
import javax.ws.rs.GET ;
import javax.ws.rs.POST ;
import javax.ws.rs.Path ;
import javax.ws.rs.PathParam ;
import javax.ws.rs.Produces ;
import javax.ws.rs.core.Context ;
import javax.ws.rs.core.MediaType ;
import javax.ws.rs.core.SecurityContext ;

import org.apache.commons.lang3.StringUtils ;
import org.apache.commons.validator.routines.UrlValidator ;
import org.apache.cxf.common.util.Base64UrlUtility ;
import org.apache.cxf.jaxrs.ext.MessageContext ;
import org.apache.cxf.rs.security.oauth2.common.Client ;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken ;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider ;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant ;
import org.apache.cxf.rs.security.oauth2.provider.ClientRegistrationProvider ;
import org.apache.cxf.rs.security.oauth2.provider.OAuthDataProvider ;
import org.apache.cxf.rs.security.oauth2.tokens.refresh.RefreshToken ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants ;
import org.apache.cxf.rt.security.crypto.CryptoUtils ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;

import ca.sgicanada.security.oauth2.ClientCodeGrants ;
import ca.sgicanada.security.oauth2.ClientTokens ;

@Path("/")
public class ClientService
{

  /**
   * Logger instance.
   */
  private static final Logger LOG = LoggerFactory.getLogger(ClientService.class) ;

  private Map<String, Collection<Client>> registrations = new HashMap<>() ;
  private Map<String, Set<String>> clientNames = new HashMap<>() ;
  private OAuthDataProvider dataProvider ;
  private ClientRegistrationProvider clientProvider ;
  private boolean protectIdTokenWithClientSecret ;
  private MessageContext messageContext ;

  @Context
  public void setMessageContext(MessageContext messageContext)
  {
    System.out.println("setMessageContext") ;
    this.messageContext = messageContext ;
  }

  // @POST
  // @Produces({ "application/xhtml+xml", "text/html", "application/xml",
  // "application/json" })
  // @Path("/registClient")
  // public Response createClient(@QueryParam(OAuthConstants.RESPONSE_TYPE)
  // String responseType)
  // {
  // Client client = new Client() ;
  // dataProvider.asdf(client) ;
  // // RedirectionBasedGrantService service = getService(responseType);
  // // if (service != null) {
  // //
  // // } else {
  // // return reportInvalidResponseType();
  // // }
  //
  // return service.authorize() ;
  // }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Path("/login")
  public Client authenitcateClient(@FormParam("clientId") String clientId, @FormParam("clientSecret") String clientSecret)
  {

    System.out.println("authenitcateClient clientId " + clientId + " clientSecret " + clientSecret) ;

    if (clientId == null || clientSecret == null || clientId.isEmpty() || clientSecret.isEmpty())
    {

      throw new IllegalArgumentException("Client authentication credentials are required.") ;
    }
    if (messageContext != null)
    {
      messageContext.put(OAuthConstants.CLIENT_SECRET, clientSecret) ;
    }

    Client client = dataProvider.getClient(clientId) ;
    // Client client = null ;
    // client = new Client() ;
    // client.setClientId(clientId) ;
    // client.setClientSecret(clientSecret) ;
    // client.setApplicationName("AppName") ;
    Collection<Client> userClientRegs = registrations.get(client.getClientId()) ;
    if (userClientRegs == null)
    {
      userClientRegs = new TreeSet<Client>(new ClientComparator()) ;
      registrations.put(clientId, userClientRegs) ;
    }
    userClientRegs.add(client) ;
    return client ;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}")
  public Client getRegisteredClient(@PathParam("id") String id)
  {
    for (Client c : getClientRegistrations(id))
    {
      if (c.getClientId().equals(id))
      {
        return c ;
      }
    }
    return null ;
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/reset")
  public Client resetClient(@PathParam("id") String id, @FormParam("client_csrfToken") String csrfToken)
  {

    Client c = getRegisteredClient(id) ;
    if (c.isConfidential())
    {
      c.setClientSecret(generateClientSecret()) ;
    }
    clientProvider.setClient(c) ;
    return c ;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/tokens")
  public ClientTokens getClientIssuedTokens(@PathParam("id") String id)
  {
    Client c = getRegisteredClient(id) ;
    return doGetClientIssuedTokens(c) ;
  }

  protected ClientTokens doGetClientIssuedTokens(Client c)
  {
    Comparator<ServerAccessToken> tokenComp = new TokenComparator() ;
    List<ServerAccessToken> accessTokens = dataProvider.getAccessTokens(c, null) ;
    System.out.println("accessTokens " + accessTokens) ;
    if (accessTokens != null)
      Collections.sort(accessTokens, tokenComp) ;
    List<RefreshToken> refreshTokens = dataProvider.getRefreshTokens(c, null) ;
    System.out.println("refreshTokens " + refreshTokens) ;
    if (refreshTokens != null)
      Collections.sort(refreshTokens, tokenComp) ;
    return new ClientTokens(c, accessTokens, refreshTokens) ;
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/at/{tokenId}/revoke")
  public ClientTokens revokeClientAccessToken(@PathParam("id") String clientId, @PathParam("tokenId") String tokenId, @FormParam("client_csrfToken") String csrfToken)
  {

    return doRevokeClientToken(clientId, csrfToken, tokenId, OAuthConstants.ACCESS_TOKEN) ;
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/rt/{tokenId}/revoke")
  public ClientTokens revokeClientRefreshToken(@PathParam("id") String clientId, @PathParam("tokenId") String tokenId, @FormParam("client_csrfToken") String csrfToken)
  {
    return doRevokeClientToken(clientId, csrfToken, tokenId, OAuthConstants.REFRESH_TOKEN) ;
  }

  protected ClientTokens doRevokeClientToken(String clientId, String csrfToken, String tokenId, String tokenType)
  {

    Client c = getRegisteredClient(clientId) ;
    dataProvider.revokeToken(c, tokenId, tokenType) ;
    return doGetClientIssuedTokens(c) ;
  }

  @GET
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/codes")
  public ClientCodeGrants getClientCodeGrants(@PathParam("id") String id)
  {
    if (dataProvider instanceof AuthorizationCodeDataProvider)
    {
      Client c = getRegisteredClient(id) ;
      List<ServerAuthorizationCodeGrant> codeGrants = new ArrayList<>(((AuthorizationCodeDataProvider) dataProvider).getCodeGrants(c, null)) ;
      Collections.sort(codeGrants, new CodeGrantComparator()) ;
      return new ClientCodeGrants(c, codeGrants) ;
    }
    return null ;
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  @Produces(MediaType.TEXT_HTML)
  @Path("/{id}/codes/{code}/revoke")
  public ClientCodeGrants revokeClientCodeGrant(@PathParam("id") String id, @PathParam("code") String code, @FormParam("client_csrfToken") String csrfToken)
  {
    if (dataProvider instanceof AuthorizationCodeDataProvider)
    {
      ((AuthorizationCodeDataProvider) dataProvider).removeCodeGrant(code) ;
      return getClientCodeGrants(id) ;
    }
    return null ;
  }

  private boolean isValidURI(String uri, boolean requireHttps)
  {

    UrlValidator urlValidator = null ;

    if (requireHttps)
    {
      String[] schemes = { "https" } ;
      urlValidator = new UrlValidator(schemes, UrlValidator.ALLOW_LOCAL_URLS) ;
    }
    else
    {
      urlValidator = new UrlValidator(UrlValidator.ALLOW_LOCAL_URLS + UrlValidator.ALLOW_ALL_SCHEMES) ;
    }

    if (!urlValidator.isValid(uri))
    {
      return false ;
    }

    // Do additional checks on the URI
    try
    {
      URI parsedURI = new URI(uri) ;
      // The URI can't have a fragment according to the OAuth 2.0 spec (+
      // audience spec)
      if (parsedURI.getFragment() != null)
      {
        return false ;
      }
    }
    catch (URISyntaxException ex)
    {
      return false ;
    }

    return true ;
  }

  protected String generateClientId()
  {
    return Base64UrlUtility.encode(CryptoUtils.generateSecureRandomBytes(10)) ;
  }

  protected String generateClientSecret()
  {
    // TODO: may need to be 384/8 or 512/8 if not a default HS256 but HS384 or
    // HS512
    int keySizeOctets = protectIdTokenWithClientSecret ? 32 : 16 ;
    return Base64UrlUtility.encode(CryptoUtils.generateSecureRandomBytes(keySizeOctets)) ;
  }

  protected Collection<Client> getClientRegistrations()
  {
    String userName = getUserName() ;
    return getClientRegistrations(userName) ;
  }

  protected Collection<Client> getClientRegistrations(String userName)
  {
    Collection<Client> userClientRegs = registrations.get(userName) ;
    if (userClientRegs == null)
    {
      userClientRegs = new TreeSet<Client>(new ClientComparator()) ;
      registrations.put(userName, userClientRegs) ;
    }
    return userClientRegs ;
  }

  private String getUserName()
  {
    SecurityContext sc = messageContext.getSecurityContext() ;
    if (sc == null || sc.getUserPrincipal() == null)
    {
      return null ;
    }
    return sc.getUserPrincipal().getName() ;
  }

  public void init()
  {
    for (Client c : clientProvider.getClients(null))
    {
      if (c.getResourceOwnerSubject() != null)
      {
        String userName = c.getResourceOwnerSubject().getLogin() ;
        getClientRegistrations(userName).add(c) ;
        Set<String> names = clientNames.get(userName) ;
        if (names == null)
        {
          names = new HashSet<>() ;
          clientNames.put(userName, names) ;
        }
        names.add(c.getApplicationName()) ;
      }
    }
  }

  public void setProtectIdTokenWithClientSecret(boolean protectIdTokenWithClientSecret)
  {
    this.protectIdTokenWithClientSecret = protectIdTokenWithClientSecret ;
  }

  public OAuthDataProvider getDataProvider()
  {
    return dataProvider ;
  }

  public void setDataProvider(OAuthDataProvider dataProvider)
  {
    this.dataProvider = dataProvider ;
  }

  public void setClientProvider(ClientRegistrationProvider clientProvider)
  {
    this.clientProvider = clientProvider ;
  }

  private static class ClientComparator implements Comparator<Client>
  {

    @Override
    public int compare(Client c1, Client c2)
    {
      // or the registration date comparison - this can be driven from UI
      // example, Sort Clients By Name/Date/etc
      if (StringUtils.isEmpty(c1.getApplicationName()))
      {
        c1.setApplicationName("dummy") ;
      }
      return c1.getApplicationName().compareTo(c2.getApplicationName()) ;

      // return 1 ;
    }

  }

  private static class TokenComparator implements Comparator<ServerAccessToken>
  {

    @Override
    public int compare(ServerAccessToken t1, ServerAccessToken t2)
    {
      return Long.compare(t1.getIssuedAt(), t2.getIssuedAt()) ;
    }

  }

  private static class CodeGrantComparator implements Comparator<ServerAuthorizationCodeGrant>
  {

    @Override
    public int compare(ServerAuthorizationCodeGrant g1, ServerAuthorizationCodeGrant g2)
    {
      return Long.compare(g1.getIssuedAt(), g2.getIssuedAt()) ;
    }

  }
}