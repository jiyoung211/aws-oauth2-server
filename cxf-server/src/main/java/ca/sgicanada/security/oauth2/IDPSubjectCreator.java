package ca.sgicanada.security.oauth2;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.cxf.jaxrs.ext.MessageContext;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.oauth2.common.AuthenticationMethod;
import org.apache.cxf.rs.security.oauth2.common.UserSubject;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException;
import org.apache.cxf.rs.security.oauth2.provider.SubjectCreator;
import org.apache.cxf.security.LoginSecurityContext;
import org.apache.cxf.security.SecurityContext;

import ca.sgicanada.security.dao.OAuth2DAO;

public class IDPSubjectCreator implements SubjectCreator{

	private OAuth2DAO oauth2DAO;
	
	@Override
	public UserSubject createUserSubject(MessageContext mc, MultivaluedMap<String, String> params) throws OAuthServiceException {
		SecurityContext securityContext = (SecurityContext)mc.get(SecurityContext.class.getName());
		return createSubject(mc,securityContext);
	}

	
	public UserSubject createSubject(MessageContext mc, SecurityContext sc) {
        UserSubject subject = mc.getContent(UserSubject.class);
        if (subject != null) {
            return subject;
        }
        return createSubject(sc);
    }
	
    public UserSubject createSubject(SecurityContext securityContext) {
    	UserSubject subject = null;
        List<String> roleNames = Collections.emptyList();
        if(null!=securityContext && null!=securityContext.getUserPrincipal()) {
	        if (securityContext instanceof LoginSecurityContext) {
	            roleNames = new ArrayList<>();
	            Set<Principal> roles = ((LoginSecurityContext)securityContext).getUserRoles();
	            for (Principal p : roles) {
	                roleNames.add(p.getName());
	            }
	        }
	        
	        subject = oauth2DAO.selectUserSubject(null,securityContext.getUserPrincipal().getName());
	        if(subject ==null)
	        	subject = new UserSubject(securityContext.getUserPrincipal().getName(), roleNames);
	        Message m = JAXRSUtils.getCurrentMessage();
	        if (m != null && m.get(AuthenticationMethod.class) != null) {
	            subject.setAuthenticationMethod(m.get(AuthenticationMethod.class));
	        }
        }
        return subject;
    }
    
    public void setOauth2DAO(OAuth2DAO oauth2dao) {
		oauth2DAO = oauth2dao;
	}
	
}
