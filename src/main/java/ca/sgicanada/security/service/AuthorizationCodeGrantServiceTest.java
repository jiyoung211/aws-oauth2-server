package ca.sgicanada.security.service ;

import java.util.List ;

import javax.ws.rs.Path ;
import javax.ws.rs.core.MultivaluedMap ;
import javax.ws.rs.core.Response ;
import javax.ws.rs.core.UriBuilder ;

import org.apache.cxf.rs.security.oauth2.common.Client ;
import org.apache.cxf.rs.security.oauth2.common.FormAuthorizationResponse ;
import org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData ;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission ;
import org.apache.cxf.rs.security.oauth2.common.OAuthRedirectionState ;
import org.apache.cxf.rs.security.oauth2.common.OOBAuthorizationResponse ;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken ;
import org.apache.cxf.rs.security.oauth2.common.UserSubject ;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeDataProvider ;
import org.apache.cxf.rs.security.oauth2.grants.code.AuthorizationCodeRegistration ;
import org.apache.cxf.rs.security.oauth2.grants.code.ServerAuthorizationCodeGrant ;
import org.apache.cxf.rs.security.oauth2.provider.AuthorizationCodeResponseFilter ;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException ;
import org.apache.cxf.rs.security.oauth2.provider.OOBResponseDeliverer ;
import org.apache.cxf.rs.security.oauth2.services.RedirectionBasedGrantService ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants ;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils ;

@Path("/authorize1")
public class AuthorizationCodeGrantServiceTest extends RedirectionBasedGrantService
{
  private static final String AUTHORIZATION_REQUEST_PARAMETERS = "authorization.request.parameters" ;
  private static final String PREAUTHORIZED_TOKEN_KEY = "preauthorized.token.key" ;
  // private Set<String> supportedResponseTypes ;
  // private String supportedGrantType ;
  private boolean useAllClientScopes ;
  private boolean partialMatchScopeValidation ;

  private static final long RECOMMENDED_CODE_EXPIRY_TIME_SECS = 10L * 60L ;
  private boolean canSupportPublicClients ;
  private boolean canSupportEmptyRedirectForPrivateClients ;
  private OOBResponseDeliverer oobDeliverer ;
  private AuthorizationCodeResponseFilter codeResponseFilter ;

  public AuthorizationCodeGrantServiceTest()
  {
    super(OAuthConstants.CODE_RESPONSE_TYPE, OAuthConstants.AUTHORIZATION_CODE_GRANT) ;
  }

  // protected AuthorizationCodeGrantServiceTest(Set<String>
  // supportedResponseTypes, String supportedGrantType)
  // {
  // super(supportedResponseTypes, supportedGrantType) ;
  // this.supportedResponseTypes = supportedResponseTypes ;
  // this.supportedGrantType = supportedGrantType ;
  // }

  @Override
  protected Response startAuthorization(MultivaluedMap<String, String> params)
  {
    System.out.println("startAuthorization1 ") ;
    // TODO Auto-generated method stub
    return super.startAuthorization(params) ;
  }

  @Override
  public void setUseAllClientScopes(boolean useAllClientScopes)
  {
    this.useAllClientScopes = useAllClientScopes ;
    // TODO Auto-generated method stub
    super.setUseAllClientScopes(useAllClientScopes) ;
  }

  @Override
  public void setPartialMatchScopeValidation(boolean partialMatchScopeValidation)
  {
    this.partialMatchScopeValidation = partialMatchScopeValidation ;
    // TODO Auto-generated method stub
    super.setPartialMatchScopeValidation(partialMatchScopeValidation) ;
  }

  @Override
  protected Response startAuthorization(MultivaluedMap<String, String> params, UserSubject userSubject, Client client, String redirectUri)
  {

    // Enforce the client confidentiality requirements
    if (!OAuthUtils.isGrantSupportedForClient(client, canSupportPublicClient(client), getSupportedGrantType()))
    {
      System.out.println("The grant type is not supported") ;
      return createErrorResponse(params, redirectUri, OAuthConstants.UNAUTHORIZED_CLIENT) ;
    }

    // Check response_type
    String responseType = params.getFirst(OAuthConstants.RESPONSE_TYPE) ;
    if (responseType == null || !getSupportedResponseTypes().contains(responseType))
    {
      System.out.println("The response type is null or not supported") ;
      return createErrorResponse(params, redirectUri, OAuthConstants.UNSUPPORTED_RESPONSE_TYPE) ;
    }
    // Get the requested scopes
    String providedScope = params.getFirst(OAuthConstants.SCOPE) ;
    List<String> requestedScope = null ;
    List<OAuthPermission> requestedPermissions = null ;
    try
    {
      requestedScope = OAuthUtils.getRequestedScopes(client, providedScope, useAllClientScopes, partialMatchScopeValidation) ;
      requestedPermissions = getDataProvider().convertScopeToPermissions(client, requestedScope) ;
    }
    catch (OAuthServiceException ex)
    {
      ex.printStackTrace() ;
      // System.out.println(Level.FINE, "Error processing scopes", ex) ;
      return createErrorResponse(params, redirectUri, OAuthConstants.INVALID_SCOPE) ;
    }

    // Validate the audience
    String clientAudience = params.getFirst(OAuthConstants.CLIENT_AUDIENCE) ;
    // Right now if the audience parameter is set it is expected to be contained
    // in the list of Client audiences set at the Client registration time.
    if (!OAuthUtils.validateAudience(clientAudience, client.getRegisteredAudiences()))
    {
      System.out.println("Error validating audience parameter") ;
      return createErrorResponse(params, redirectUri, OAuthConstants.INVALID_REQUEST) ;
    }

    // Request a new grant only if no pre-authorized token is available
    ServerAccessToken preAuthorizedToken = null ;
    if (canAccessTokenBeReturned(responseType))
    {
      preAuthorizedToken = getDataProvider().getPreauthorizedToken(client, requestedScope, userSubject, getSupportedGrantType()) ;
    }

    List<OAuthPermission> alreadyAuthorizedPerms = null ;
    boolean preAuthorizationComplete = false ;
    if (preAuthorizedToken != null)
    {
      alreadyAuthorizedPerms = preAuthorizedToken.getScopes() ;
      preAuthorizationComplete = OAuthUtils.convertPermissionsToScopeList(alreadyAuthorizedPerms).containsAll(requestedScope) ;
    }

    Response finalResponse = null ;
    try
    {
      final boolean authorizationCanBeSkipped = preAuthorizationComplete || canAuthorizationBeSkipped(params, client, userSubject, requestedScope, requestedPermissions) ;

      // Populate the authorization challenge data
      OAuthAuthorizationData data = createAuthorizationData(client, params, redirectUri, userSubject, requestedPermissions, alreadyAuthorizedPerms, authorizationCanBeSkipped) ;
      System.out.println("### authorizationCanBeSkipped " + authorizationCanBeSkipped) ;
      if (true)
      {
        getMessageContext().put(AUTHORIZATION_REQUEST_PARAMETERS, params) ;
        List<OAuthPermission> approvedScopes = preAuthorizationComplete ? preAuthorizedToken.getScopes() : requestedPermissions ;
        System.out.println("createGrant") ;
        finalResponse = createGrant(data, client, requestedScope, OAuthUtils.convertPermissionsToScopeList(approvedScopes), userSubject, preAuthorizedToken) ;
      }
      else
      {
        if (preAuthorizedToken != null)
        {
          data.setPreauthorizedTokenKey(preAuthorizedToken.getTokenKey()) ;
        }
        finalResponse = Response.ok(data).build() ;
      }
    }
    catch (OAuthServiceException ex)
    {
      ex.printStackTrace() ;
      finalResponse = createErrorResponse(params, redirectUri, ex.getError().getError()) ;
    }

    return finalResponse ;

  }

  @Override
  protected String validateRedirectUri(Client client, String redirectUri)
  {
    // System.out.println("validateRedirectUri") ;
    // System.out.println("client " + client.getClientId()) ;
    // System.out.println("redirectUri " + redirectUri) ;
    // return redirectUri ;
    //
    //
    // TODO Auto-generated method stub
    return super.validateRedirectUri(client, redirectUri) ;

  }

  @Override
  protected OAuthAuthorizationData createAuthorizationData(Client client, MultivaluedMap<String, String> params, String redirectUri, UserSubject subject, List<OAuthPermission> requestedPerms, List<OAuthPermission> alreadyAuthorizedPerms, boolean authorizationCanBeSkipped)
  {
    OAuthAuthorizationData data = super.createAuthorizationData(client, params, redirectUri, subject, requestedPerms, alreadyAuthorizedPerms, authorizationCanBeSkipped) ;
    setCodeChallenge(data, params) ;
    return data ;
  }

  protected OAuthRedirectionState recreateRedirectionStateFromParams(MultivaluedMap<String, String> params)
  {
    OAuthRedirectionState state = super.recreateRedirectionStateFromParams(params) ;
    setCodeChallenge(state, params) ;
    return state ;
  }

  private static void setCodeChallenge(OAuthRedirectionState data, MultivaluedMap<String, String> params)
  {
    data.setClientCodeChallenge(params.getFirst(OAuthConstants.AUTHORIZATION_CODE_CHALLENGE)) ;
  }

  protected Response createGrant(OAuthRedirectionState state, Client client, List<String> requestedScope, List<String> approvedScope, UserSubject userSubject, ServerAccessToken preauthorizedToken)
  {
    // in this flow the code is still created, the preauthorized token
    // will be retrieved by the authorization code grant handler
    ServerAuthorizationCodeGrant grant = null ;
    try
    {
      grant = getGrantRepresentation(state, client, requestedScope, approvedScope, userSubject, preauthorizedToken) ;
    }
    catch (OAuthServiceException ex)
    {
      return createErrorResponse(state.getState(), state.getRedirectUri(), OAuthConstants.ACCESS_DENIED) ;
    }
    String grantCode = processCodeGrant(client, grant.getCode(), grant.getSubject()) ;
    if (state.getRedirectUri() == null)
    {
      OOBAuthorizationResponse bean = new OOBAuthorizationResponse() ;
      bean.setClientId(client.getClientId()) ;
      bean.setClientDescription(client.getApplicationDescription()) ;
      bean.setAuthorizationCode(grantCode) ;
      bean.setUserId(userSubject.getLogin()) ;
      bean.setExpiresIn(grant.getExpiresIn()) ;
      return deliverOOBResponse(bean) ;
    }
    else if (isFormResponse(state))
    {
      FormAuthorizationResponse bean = new FormAuthorizationResponse() ;
      bean.setAuthorizationCode(grantCode) ;
      bean.setExpiresIn(grant.getExpiresIn()) ;
      bean.setState(state.getState()) ;
      bean.setRedirectUri(state.getRedirectUri()) ;
      return createHtmlResponse(bean) ;
    }
    else
    {
      // return the code by appending it as a query parameter to the redirect
      // URI
      UriBuilder ub = getRedirectUriBuilder(state.getState(), state.getRedirectUri()) ;
      ub.queryParam(OAuthConstants.AUTHORIZATION_CODE_VALUE, grantCode) ;
      return Response.seeOther(ub.build()).build() ;
    }
  }

  public ServerAuthorizationCodeGrant getGrantRepresentation(OAuthRedirectionState state, Client client, List<String> requestedScope, List<String> approvedScope, UserSubject userSubject, ServerAccessToken preauthorizedToken)
  {
    AuthorizationCodeRegistration codeReg = createCodeRegistration(state, client, requestedScope, approvedScope, userSubject, preauthorizedToken) ;

    ServerAuthorizationCodeGrant grant = ((AuthorizationCodeDataProvider) getDataProvider()).createCodeGrant(codeReg) ;
    if (grant.getExpiresIn() > RECOMMENDED_CODE_EXPIRY_TIME_SECS)
    {
      LOG.warning("Code expiry time exceeds 10 minutes") ;
    }
    return grant ;
  }

  protected AuthorizationCodeRegistration createCodeRegistration(OAuthRedirectionState state, Client client, List<String> requestedScope, List<String> approvedScope, UserSubject userSubject, ServerAccessToken preauthorizedToken)
  {
    AuthorizationCodeRegistration codeReg = new AuthorizationCodeRegistration() ;
    codeReg.setPreauthorizedTokenAvailable(preauthorizedToken != null) ;
    codeReg.setClient(client) ;
    codeReg.setRedirectUri(state.getRedirectUri()) ;
    codeReg.setRequestedScope(requestedScope) ;
    codeReg.setResponseType(state.getResponseType()) ;
    codeReg.setApprovedScope(getApprovedScope(requestedScope, approvedScope)) ;
    codeReg.setSubject(userSubject) ;
    codeReg.setAudience(state.getAudience()) ;
    codeReg.setNonce(state.getNonce()) ;
    codeReg.setClientCodeChallenge(state.getClientCodeChallenge()) ;
    codeReg.getExtraProperties().putAll(state.getExtraProperties()) ;
    return codeReg ;
  }

  protected String processCodeGrant(Client client, String code, UserSubject endUser)
  {
    if (codeResponseFilter != null)
    {
      return codeResponseFilter.process(client, code, endUser) ;
    }
    return code ;
  }

  protected Response deliverOOBResponse(OOBAuthorizationResponse response)
  {
    if (oobDeliverer != null)
    {
      return oobDeliverer.deliver(response) ;
    }
    return createHtmlResponse(response) ;
  }

  protected Response createErrorResponse(String state, String redirectUri, String error)
  {
    if (redirectUri == null)
    {
      return Response.status(401).entity(error).build() ;
    }
    UriBuilder ub = getRedirectUriBuilder(state, redirectUri) ;
    ub.queryParam(OAuthConstants.ERROR_KEY, error) ;
    return Response.seeOther(ub.build()).build() ;
  }

  protected UriBuilder getRedirectUriBuilder(String state, String redirectUri)
  {
    UriBuilder ub = UriBuilder.fromUri(redirectUri) ;
    if (state != null)
    {
      ub.queryParam(OAuthConstants.STATE, state) ;
    }
    return ub ;
  }

  @Override
  protected boolean canSupportPublicClient(Client c)
  {
    return canSupportPublicClients && !c.isConfidential() && c.getClientSecret() == null ;
  }

  @Override
  protected boolean canRedirectUriBeEmpty(Client c)
  {
    // If a redirect URI is empty then the code will be returned out of band,
    // typically will be returned directly to a human user
    return (c.isConfidential() && canSupportEmptyRedirectForPrivateClients || canSupportPublicClient(c)) && c.getRedirectUris().isEmpty() ;
  }

  public void setCanSupportPublicClients(boolean support)
  {
    this.canSupportPublicClients = support ;
  }

  public void setCodeResponseFilter(AuthorizationCodeResponseFilter filter)
  {
    this.codeResponseFilter = filter ;
  }

  public void setCanSupportEmptyRedirectForPrivateClients(boolean canSupportEmptyRedirectForPrivateClients)
  {
    this.canSupportEmptyRedirectForPrivateClients = canSupportEmptyRedirectForPrivateClients ;
  }

}
