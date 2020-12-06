package ca.sgicanada.security.oauth2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Provider;
import javax.xml.stream.XMLStreamWriter;

import org.apache.cxf.jaxrs.impl.MetadataMap;
import org.apache.cxf.jaxrs.provider.FormEncodingProvider;
import org.apache.cxf.rs.security.oauth2.utils.OAuthConstants;
import org.apache.cxf.staxutils.StaxUtils;
import org.apache.cxf.rs.security.oauth2.common.OOBAuthorizationResponse;

@Produces({MediaType.APPLICATION_FORM_URLENCODED,MediaType.TEXT_HTML })
@Consumes({MediaType.APPLICATION_FORM_URLENCODED})
@Provider
public class OOBResponseProvider implements MessageBodyReader<OOBAuthorizationResponse>, MessageBodyWriter<OOBAuthorizationResponse> {

    private FormEncodingProvider<Form> formProvider = new FormEncodingProvider<Form>();    

    public boolean isReadable(Class<?> type, Type genericType,
                              Annotation[] annotations, MediaType mt) {
        return OOBAuthorizationResponse.class.isAssignableFrom(type);
    }

    public OOBAuthorizationResponse readFrom(
        Class<OOBAuthorizationResponse> clazz, Type genericType, Annotation[] annotations, MediaType mt,
        MultivaluedMap<String, String> headers, InputStream is) throws IOException {
        Form form = formProvider.readFrom(Form.class, Form.class, annotations, mt, headers, is);
        MultivaluedMap<String, String> data = form.asMap();
        OOBAuthorizationResponse resp = new OOBAuthorizationResponse();

        resp.setAuthorizationCode(data.getFirst(OAuthConstants.AUTHORIZATION_CODE_VALUE));        
        resp.setClientId(data.getFirst(OAuthConstants.CLIENT_ID));
        resp.setExpiresIn(Long.parseLong(data.getFirst(OAuthConstants.ACCESS_TOKEN_EXPIRES_IN)));
        resp.setUserId(data.getFirst("userid"));

        return resp;
    }


    public long getSize(OOBAuthorizationResponse t, Class<?> type,
                        Type genericType, Annotation[] annotations,
                        MediaType mediaType) {
        return -1;
    }

    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations,
                               MediaType mt) {
        return OOBAuthorizationResponse.class.isAssignableFrom(type);
    }

    public void writeTo(OOBAuthorizationResponse obj, Class<?> c, Type t,
                        Annotation[] anns,
                        MediaType mt, MultivaluedMap<String, Object> headers, OutputStream os)
        throws IOException, WebApplicationException {

        Form form = new Form(new MetadataMap<String, String>());
        form.param(OAuthConstants.AUTHORIZATION_CODE_VALUE, obj.getAuthorizationCode());
        form.param(OAuthConstants.CLIENT_ID, obj.getClientId());
        form.param(OAuthConstants.ACCESS_TOKEN_EXPIRES_IN, String.valueOf(obj.getExpiresIn()));   
        form.param("userid",obj.getUserId());
        formProvider.writeTo(form, Form.class, Form.class, anns, mt, headers, os);
        
    }

}
