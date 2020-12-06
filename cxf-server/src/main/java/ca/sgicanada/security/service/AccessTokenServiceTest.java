package ca.sgicanada.security.service ;

import javax.ws.rs.Consumes ;
import javax.ws.rs.POST ;
import javax.ws.rs.Path ;
import javax.ws.rs.Produces ;
import javax.ws.rs.WebApplicationException ;
import javax.ws.rs.core.HttpHeaders ;
import javax.ws.rs.core.MultivaluedMap ;
import javax.ws.rs.core.Response ;

import org.apache.commons.lang.StringUtils ;
import org.apache.cxf.rs.security.oauth2.common.Client ;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken ;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken ;
import org.apache.cxf.rs.security.oauth2.provider.AccessTokenGrantHandler ;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException ;
import org.apache.cxf.rs.security.oauth2.services.AccessTokenService ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils ;
import org.codehaus.plexus.util.ExceptionUtils ;

@Path("/token1")
public class AccessTokenServiceTest extends AccessTokenService
{
  @POST
  @Consumes("application/x-www-form-urlencoded")
  // @Produces("application/json")
  @Produces({ "application/xhtml+xml", "text/html", "application/xml", "application/json" })
  public Response handleTokenRequest(MultivaluedMap<String, String> params)
  {
    System.out.println("handleTokenRequest") ;
    // Make sure the client is authenticated
    Client client = authenticateClientIfNeeded(params) ;

    if (!OAuthUtils.isGrantSupportedForClient(client, isCanSupportPublicClients(), params.getFirst(OAuthConstants.GRANT_TYPE)))
    {
      System.out.println("The grant type {} is not supported for the client" + params.getFirst(OAuthConstants.GRANT_TYPE)) ;
      // LOG.log(Level.FINE,
      // "The grant type {} is not supported for the client",
      // params.getFirst(OAuthConstants.GRANT_TYPE)) ;
      return createErrorResponse(params, OAuthConstants.UNAUTHORIZED_CLIENT) ;
    }

    try
    {
      checkAudience(client, params) ;
    }
    catch (OAuthServiceException ex)
    {
      return super.createErrorResponseFromBean(ex.getError()) ;
    }

    // Find the grant handler
    AccessTokenGrantHandler handler = findGrantHandler(params) ;
    if (handler == null)
    {
      System.out.println("No Grant Handler found") ;
      // LOG.fine("No Grant Handler found") ;
      return createErrorResponse(params, OAuthConstants.UNSUPPORTED_GRANT_TYPE) ;
    }

    // Create the access token
    ServerAccessToken serverToken = null ;
    try
    {
      serverToken = handler.createAccessToken(client, params) ;
    }
    catch (WebApplicationException ex)
    {
      System.out.println(StringUtils.join(ExceptionUtils.getRootCauseStackTrace(ex), "\n")) ;
      ex.printStackTrace() ;
      throw ex ;
    }
    catch (RuntimeException ex)
    {
      ex.printStackTrace() ;
      // This is done to bypass a Check-Style
      // restriction on a number of return statements
      OAuthServiceException oauthEx = ex instanceof OAuthServiceException ? (OAuthServiceException) ex : new OAuthServiceException(ex) ;
      return handleException(oauthEx, OAuthConstants.INVALID_GRANT) ;
    }
    if (serverToken == null)
    {
      System.out.println("No access token was created") ;
      // LOG.fine("No access token was created") ;
      return createErrorResponse(params, OAuthConstants.INVALID_GRANT) ;
    }

    // Extract the information to be of use for the client
    ClientAccessToken clientToken = OAuthUtils.toClientAccessToken(serverToken, isWriteOptionalParameters()) ;
    processClientAccessToken(clientToken, serverToken) ;
    // Return it to the client
    return Response.ok(clientToken).header(HttpHeaders.CACHE_CONTROL, "no-store").header("Pragma", "no-cache").build() ;
  }
}
