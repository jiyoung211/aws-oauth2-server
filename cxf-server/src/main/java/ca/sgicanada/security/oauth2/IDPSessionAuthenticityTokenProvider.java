package ca.sgicanada.security.oauth2;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData;
import org.apache.cxf.rs.security.oauth2.common.OAuthRedirectionState;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.SessionAuthenticityTokenProvider;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.rs.security.oauth2.utils.OAuthUtils;
import org.springframework.beans.factory.annotation.Value;

import ca.sgicanada.security.dao.OAuth2DAO;

public class IDPSessionAuthenticityTokenProvider implements SessionAuthenticityTokenProvider {
	
	private long sessionAuthTokenLifeTime = 3600l;
	private OAuth2DAO oauth2DAO;
	
	@Value( "${sgi.oauth2.replyto.url}" )
	private String replayToUrl;

	@Override
	public String createSessionToken(MessageContext mc, MultivaluedMap<String, String> params, UserSubject subject,
			OAuthRedirectionState secData) {
		String sessionAuthToken = OAuthUtils.generateRandomTokenKey();
		oauth2DAO.insertSessionAuthToken(sessionAuthToken,OAuthUtils.getIssuedAt()+sessionAuthTokenLifeTime,secData.getClientId(), subject.getId());
		if(secData instanceof OAuthAuthorizationData)
			((OAuthAuthorizationData) secData).setReplyTo(replayToUrl);
		return sessionAuthToken;
	}

	@Override
	public String getSessionToken(MessageContext mc, MultivaluedMap<String, String> params, UserSubject subject) {
		return oauth2DAO.selectSessionAuthToken(params.getFirst(OAuthConstants.SESSION_AUTHENTICITY_TOKEN));
	}

	@Override
	public String removeSessionToken(MessageContext mc, MultivaluedMap<String, String> params, UserSubject subject) {
		return oauth2DAO.deleteSessionAuthToken(params.getFirst(OAuthConstants.SESSION_AUTHENTICITY_TOKEN));
	}

	@Override
	public OAuthRedirectionState getSessionState(MessageContext messageContext, String sessionToken, UserSubject subject) {
		/* we are returning null so it will gets recreated from form parameters. */
		return null;
	}

	public void setOauth2DAO(OAuth2DAO oauth2dao) {
		oauth2DAO = oauth2dao;
	}

	public void setSessionAuthTokenLifeTime(long sessionAuthTokenLifeTime) {
		this.sessionAuthTokenLifeTime = sessionAuthTokenLifeTime;
	}

	public void setReplayToUrl(String replayToUrl) {
		this.replayToUrl = replayToUrl;
	}

}
