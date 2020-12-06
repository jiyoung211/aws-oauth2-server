package ca.sgicanada.security.oauth2;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.cxf.common.security.SimplePrincipal;
import org.apache.cxf.common.util.Base64Exception;
import org.apache.cxf.common.util.Base64Utility;
import org.apache.cxf.jaxrs.utils.JAXRSUtils;
import org.apache.cxf.message.Message;
import org.apache.cxf.security.SecurityContext;

public class SecurityContextFilter implements ContainerRequestFilter {

	@Context
	private HttpHeaders headers;
	private Map<String,String> userAccounts;
	

	public void setUserAccounts(Map<String,String> userAccounts) {
		this.userAccounts = userAccounts;
	}

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		
		Message message = JAXRSUtils.getCurrentMessage();
		String accountUsername,accountPassword = null;
		SecurityContext sc = message.get(SecurityContext.class);
		
		if (sc != null) {
			Principal principal = sc.getUserPrincipal();
			if (principal != null) {
				String principalUsername = principal.getName();
				if (!checkAuthenticity(principalUsername,null)) {					
					requestContext.abortWith(createFaultResponse());
				} else {
					setNewSecurityContext(message, principalUsername);
				}
				return;
			}
		}

		List<String> authValues = headers.getRequestHeader("Authorization");
		if (authValues == null || authValues.size() != 1) {
			requestContext.abortWith(createFaultResponse());
			return;
		}
		
		String[] values = authValues.get(0).split(" ");
		if (values.length != 2 || !"Basic".equals(values[0])) {
			requestContext.abortWith(createFaultResponse());
			return;		
		}

		String decodedValue = null;
		try {
			decodedValue = new String(Base64Utility.decode(values[1]));
		} catch (Base64Exception ex) {
			requestContext.abortWith(createFaultResponse());
			return;
		}
		String[] namePassword = decodedValue.split(":");
		if (namePassword.length != 2) {
			requestContext.abortWith(createFaultResponse());
			return;
		}
		accountUsername = namePassword[0];
		accountPassword = namePassword[1];
		if (!checkAuthenticity(accountUsername,accountPassword)) {
			requestContext.abortWith(createFaultResponse());
			return;
		}

		setNewSecurityContext(message, accountUsername);
	}

	private boolean checkAuthenticity(String userId,String password) {
		
		if(null==userId || userId.trim().isEmpty()) {
			return false;
		}
		
		String userPwd = userAccounts.get(userId);
		
		if(userPwd==null || (null!=password && !userPwd.equals(password.trim()))) {
			return false;
		}
		
		return true;
	}
	
	private void setNewSecurityContext(Message message, final String user) {
		final SecurityContext newSc = new SecurityContext() {

			public Principal getUserPrincipal() {
				return new SimplePrincipal(user);
			}

			public boolean isUserInRole(String arg0) {
				return false;
			}

		};
		message.put(SecurityContext.class, newSc);
	}

	private Response createFaultResponse() {
		return Response.status(401).header("WWW-Authenticate", "Basic realm=\"idp.sgicanada.ca\"").build();
	}

}
