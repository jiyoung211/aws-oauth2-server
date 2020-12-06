package ca.sgicanada.security.oauth2;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.rs.security.oauth2.common.AccessTokenRegistration;
import org.apache.cxf.rs.security.oauth2.common.Client;
import org.apache.cxf.rs.security.oauth2.common.ServerAccessToken;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.grants.clientcred.ClientCredentialsGrantHandler;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;

import ca.sgicanada.security.constant.OAuth2Constants;

public class CustomClientCredentialGrantHandler extends ClientCredentialsGrantHandler{

	@Override
	protected ServerAccessToken doCreateAccessToken(Client client, UserSubject subject, MultivaluedMap<String, String> params) { 
	
		List<String> requestedScopes = OAuthUtils.parseScope(params.getFirst(OAuthConstants.SCOPE));
		List<String> audiences = getAudiences(client, params.getFirst(OAuthConstants.CLIENT_AUDIENCE));
		String requestedGrant = getSingleGrantType();
		ServerAccessToken token = getPreAuthorizedToken(client, subject,requestedGrant , requestedScopes, audiences);
		if (token != null) {
			return token;
		}
		/* Filtering parameters to start with defined prefix and if we have multiple values for same parameter then they will be joined by comma. */
		Map<String,String>extraProperties = client.getProperties();/*params.entrySet().stream()
			.filter(entry -> entry.getKey().startsWith(OAuth2Constants.PARAMETER_PREFIX_TO_INCLUDE))			
			.collect(Collectors.toMap(entry->entry.getKey(), entry -> entry.getValue().stream().collect(Collectors.joining(", "))));*/

		/* Delegate to the data provider to create the one */
		AccessTokenRegistration reg = new AccessTokenRegistration();
		reg.setClient(client);
		reg.setGrantType(requestedGrant);
		reg.setSubject(subject);
		reg.setRequestedScope(requestedScopes);
		reg.setApprovedScope(getApprovedScopes(client, subject, requestedScopes));
		reg.setAudiences(audiences);
		reg.setExtraProperties(extraProperties);
		return getDataProvider().createAccessToken(reg);
	}
	
	@Override
	protected List<String> getApprovedScopes(Client client, UserSubject subject, List<String> requestedScopes) {
		if(requestedScopes.contains(OAuth2Constants.API_ACCESS_SCOPE)) {
			return requestedScopes;
		} else {
			/* If required scope is not included in access token request then do not approve it. */
			throw new OAuthServiceException("Required scopes are missing");
		}
    }

}
