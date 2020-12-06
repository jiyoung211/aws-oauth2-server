package ca.sgicanada.security.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;

import org.apache.cxf.common.util.StringUtils;
import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxrs.json.basic.JsonMapObjectReaderWriter;
import org.apache.cxf.rs.security.jose.common.JoseConstants;
import org.apache.cxf.rs.security.oauth2.client.OAuthClientUtils;
import org.apache.cxf.rs.security.oauth2.common.ClientAccessToken;
import org.apache.cxf.rs.security.oauth2.common.OAuthAuthorizationData;
import org.apache.cxf.rs.security.oauth2.common.OAuthError;
import org.apache.cxf.rs.security.oauth2.common.OAuthPermission;
import org.apache.cxf.rs.security.oauth2.common.TokenIntrospection;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;

@Provider
@Produces("application/json")
@Consumes("application/json")
public class OAuthAuthorizationDataProvider  implements MessageBodyWriter<Object>, MessageBodyReader<Object> {

    public long getSize(Object obj, Class<?> clt, Type t, Annotation[] anns, MediaType mt) {
        return -1;
    }

    public boolean isWriteable(Class<?> cls, Type t, Annotation[] anns, MediaType mt) {
        return OAuthAuthorizationData.class.isAssignableFrom(cls);
    }

    public void writeTo(Object obj, Class<?> cls, Type t, Annotation[] anns, MediaType mt,
                        MultivaluedMap<String, Object> headers, OutputStream os) throws IOException,
        WebApplicationException {
        if (obj instanceof OAuthAuthorizationData) {
            writeOAuthAuthorizationData((OAuthAuthorizationData)obj, os);
        } else {
            System.out.println("Could not convert data");
        }
    }

    private void writeOAuthAuthorizationData(OAuthAuthorizationData obj, OutputStream os) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        appendJsonPair(sb,"applicationName", obj.getApplicationName());
        appendJsonPair(sb,"authenticityToken", obj.getAuthenticityToken());
        appendJsonPair(sb,"supportSinglePageApplications", obj.isSupportSinglePageApplications(),false);
        appendJsonPair(sb,"alreadyAuthorizedPermissions", obj.getAlreadyAuthorizedPermissionsAsStrings());
        appendJsonPair(sb,"preauthorizedTokenKey", obj.getPreauthorizedTokenKey());
        appendJsonPair(sb,"hidePreauthorizedScopesInForm", obj.isHidePreauthorizedScopesInForm(),false);
        appendJsonPair(sb,"applicationRegisteredDynamically", obj.isApplicationRegisteredDynamically(),false);
        appendJsonPair(sb,"implicitFlow", obj.isImplicitFlow(),false);
        appendJsonPair(sb,"endUserName", obj.getEndUserName());
        appendJsonPair(sb,"replyTo", obj.getReplyTo());
        appendJsonPair(sb,"applicationWebUri", obj.getApplicationWebUri());
        appendJsonPair(sb,"applicationLogoUri", obj.getApplicationLogoUri());
        appendJsonPair(sb,"applicationDescription", obj.getApplicationDescription());
        appendJsonPair(sb,"state", obj.getState());
        appendJsonPair(sb,"responseType", obj.getResponseType());
        appendJsonPair(sb,"nonce", obj.getNonce());
        appendJsonPair(sb,"clientId", obj.getClientId());
        appendJsonPair(sb,"redirectUri", obj.getRedirectUri());
        appendJsonPair(sb,"proposedScope", obj.getProposedScope());
        appendJsonPair(sb,"audience", obj.getAudience());
        appendJsonPair(sb,"clientCodeChallenge", obj.getClientCodeChallenge());
        
        /*
        
        appendJsonPair(sb, "active", obj.isActive(), false);
        if (obj.isActive()) {
            if (obj.getClientId() != null) {
                sb.append(",");
                appendJsonPair(sb, OAuthConstants.CLIENT_ID, obj.getClientId());
            }
            if (obj.getUsername() != null) {
                sb.append(",");
                appendJsonPair(sb, "username", obj.getUsername());
            }
            if (obj.getTokenType() != null) {
                sb.append(",");
                appendJsonPair(sb, OAuthConstants.ACCESS_TOKEN_TYPE, obj.getTokenType());
            }
            if (obj.getScope() != null) {
                sb.append(",");
                appendJsonPair(sb, OAuthConstants.SCOPE, obj.getScope());
            }
            if (!StringUtils.isEmpty(obj.getAud())) {
                sb.append(",");
                if (obj.getAud().size() == 1) {
                    appendJsonPair(sb, "aud", obj.getAud().get(0));
                } else {
                    StringBuilder arr = new StringBuilder();
                    arr.append("[");
                    List<String> auds = obj.getAud();
                    for (int i = 0; i < auds.size(); i++) {
                        if (i > 0) {
                            arr.append(",");
                        }
                        arr.append("\"").append(auds.get(i)).append("\"");
                    }
                    arr.append("]");
                    appendJsonPair(sb, "aud", arr.toString(), false);

                }
            }
            if (obj.getIss() != null) {
                sb.append(",");
                appendJsonPair(sb, "iss", obj.getIss());
            }
            sb.append(",");
            appendJsonPair(sb, "iat", obj.getIat(), false);
            if (obj.getExp() != null) {
                sb.append(",");
                appendJsonPair(sb, "exp", obj.getExp(), false);
            }
            if (!obj.getExtensions().isEmpty()) {
                for (Map.Entry<String, String> entry : obj.getExtensions().entrySet()) {
                    sb.append(",");
                    if (JoseConstants.HEADER_X509_THUMBPRINT_SHA256.equals(entry.getKey())) {
                        StringBuilder cnfObj = new StringBuilder();
                        cnfObj.append("{");
                        appendJsonPair(cnfObj, entry.getKey(), entry.getValue());
                        cnfObj.append("}");
                        appendJsonPair(sb, "cnf", cnfObj.toString(), false);
                    } else {
                        appendJsonPair(sb, entry.getKey(), entry.getValue());
                    }
                }
            }
        }*/
        sb.append("}");
        String result = sb.toString();
        os.write(result.getBytes(StandardCharsets.UTF_8));
        os.flush();

    }
    
    private void appendJsonPair(StringBuilder sb, String key, Object value) {
        appendJsonPair(sb, key, value, true);
    }

    private void appendJsonPair(StringBuilder sb, String key, Object value,
                                boolean valueQuote) {
        sb.append("\"").append(key).append("\"");
        sb.append(":");
        if (valueQuote) {
            sb.append("\"");
        }
        sb.append(value);
        if (valueQuote) {
            sb.append("\"");
        }
    }

    public boolean isReadable(Class<?> cls, Type t, Annotation[] anns, MediaType mt) {
    	System.out.println("isReadable: got name"+cls.getName());
        return OAuthAuthorizationData.class.isAssignableFrom(cls);
    }

    public Object readFrom(Class<Object> cls, Type t, Annotation[] anns,
                           MediaType mt, MultivaluedMap<String, String> headers, InputStream is)
        throws IOException, WebApplicationException {
    	System.out.println("readFrom: got name"+cls.getName());
        if (OAuthAuthorizationData.class.isAssignableFrom(cls)) {
            return fromMapToOAuthAuthorizationData(is);
        } else {
            throw new WebApplicationException(500);
        }
    }

    private Object fromMapToOAuthAuthorizationData(InputStream is) throws IOException {
    	OAuthAuthorizationData resp = new OAuthAuthorizationData();
        Map<String, Object> params = new JsonMapObjectReaderWriter().fromJson(is);
        resp.setApplicationName((String)params.get("applicationName"));
        String clientId = (String)params.get("clientId");
        if (clientId != null) {
            resp.setClientId(clientId);
        }
        String authenticityToken = (String)params.get("authenticityToken");
        if (authenticityToken != null) {
            resp.setAuthenticityToken(authenticityToken);
        }
        Boolean supportSinglePageApplications = (Boolean)params.get("supportSinglePageApplications");
        if (supportSinglePageApplications != null) {
            resp.setSupportSinglePageApplications(supportSinglePageApplications);
        }
        String preauthorizedTokenKey = (String)params.get("preauthorizedTokenKey");
        if (preauthorizedTokenKey != null) {
            resp.setPreauthorizedTokenKey(preauthorizedTokenKey);
        }
        
        Boolean hidePreauthorizedScopesInForm = (Boolean)params.get("hidePreauthorizedScopesInForm");
        if (hidePreauthorizedScopesInForm != null) {
            resp.setHidePreauthorizedScopesInForm(hidePreauthorizedScopesInForm);
        }
        Boolean applicationRegisteredDynamically = (Boolean)params.get("applicationRegisteredDynamically");
        if (applicationRegisteredDynamically != null) {
            resp.setApplicationRegisteredDynamically(applicationRegisteredDynamically);
        }
        Boolean implicitFlow = (Boolean)params.get("implicitFlow");
        if (implicitFlow != null) {
            resp.setImplicitFlow(implicitFlow);
        }
        
        String endUserName = (String)params.get("endUserName");
        if (endUserName != null) {
            resp.setEndUserName(endUserName);
        }
        String replyTo = (String)params.get("replyTo");
        if (replyTo != null) {
            resp.setReplyTo(replyTo);
        }
        String applicationWebUri = (String)params.get("applicationWebUri");
        if (applicationWebUri != null) {
            resp.setApplicationWebUri(applicationWebUri);
        }
        String applicationLogoUri = (String)params.get("applicationLogoUri");
        if (applicationLogoUri != null) {
            resp.setApplicationLogoUri(applicationLogoUri);
        }
        String applicationDescription = (String)params.get("applicationDescription");
        if (applicationDescription != null) {
            resp.setApplicationDescription(applicationDescription);
        }
        String state = (String)params.get("state");
        if (state != null) {
            resp.setState(state);
        }
        String responseType = (String)params.get("responseType");
        if (responseType != null) {
            resp.setResponseType(responseType);
        }
        String nonce = (String)params.get("nonce");
        if (nonce != null) {
            resp.setNonce(nonce);
        }
        String redirectUri = (String)params.get("redirectUri");
        if (redirectUri != null) {
            resp.setRedirectUri(redirectUri);
        }
        String proposedScope = (String)params.get("proposedScope");
        if (proposedScope != null) {
            resp.setProposedScope(proposedScope);
        }
        String audience = (String)params.get("audience");
        if (audience != null) {
            resp.setAudience(audience);
        }
        String clientCodeChallenge = (String)params.get("clientCodeChallenge");
        if (clientCodeChallenge != null) {
            resp.setClientCodeChallenge(clientCodeChallenge);
        }
        
        
        Object permissions = params.get("permissions");
        if (permissions != null) {
            if (permissions.getClass() == String.class) {
                resp.setPermissions(Collections.singletonList(new OAuthPermission((String)permissions)));
            } else {
                @SuppressWarnings("unchecked")
                List<OAuthPermission> auds = (List<OAuthPermission>)permissions;
                resp.setPermissions(auds);
            }
        }
        
        Map<String, String> extraApplicationProperties = CastUtils.cast((Map<?, ?>)params.get("extraApplicationProperties"));
        if (extraApplicationProperties != null) {
                resp.setExtraApplicationProperties(extraApplicationProperties);
            
        }
        
        Map<String, String> extraProperties = CastUtils.cast((Map<?, ?>)params.get("extraProperties"));
        if (extraProperties != null) {
        	resp.setExtraProperties(extraProperties);            
        }
        
        List<String> applicationCertificates = CastUtils.cast((List<?>)params.get("applicationCertificates"));
        if (applicationCertificates != null) {
                resp.setApplicationCertificates(applicationCertificates);
        }

        return resp;
    }
    
    public Map<String, String> readJSONResponse(InputStream is) throws IOException  {
        String str = IOUtils.readStringFromStream(is).trim();
        if (str.length() == 0) {
            return Collections.emptyMap();
        }
        if (!str.startsWith("{") || !str.endsWith("}")) {
            throw new IOException("JSON Sequence is broken");
        }
        Map<String, String> map = new LinkedHashMap<String, String>();

        str = str.substring(1, str.length() - 1).trim();
        String[] jsonPairs = str.split(",");
        for (int i = 0; i < jsonPairs.length; i++) {
            String pair = jsonPairs[i].trim();
            if (pair.length() == 0) {
                continue;
            }
            int index = pair.indexOf(":");
            String key = pair.substring(0, index).trim();
            if (key.startsWith("\"") && key.endsWith("\"")) {
                key = key.substring(1, key.length() - 1);
            }
            String value = pair.substring(index + 1).trim();
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            map.put(key, value);
        }

        return map;
    }

}
