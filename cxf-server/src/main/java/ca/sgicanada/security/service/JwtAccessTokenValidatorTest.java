package ca.sgicanada.security.service ;

import javax.ws.rs.core.MultivaluedMap ;

import org.apache.cxf.jaxrs.ext.MessageContext ;
import org.apache.cxf.rs.security.oauth2.common.AccessTokenValidation ;
import org.apache.cxf.rs.security.oauth2.filters.JwtAccessTokenValidator ;
import org.apache.cxf.rs.security.oauth2.provider.OAuthServiceException ;

public class JwtAccessTokenValidatorTest extends JwtAccessTokenValidator
{
  @Override
  public AccessTokenValidation validateAccessToken(MessageContext mc, String authScheme, String authSchemeData, MultivaluedMap<String, String> extraProps) throws OAuthServiceException
  {
    System.out.println("mc " + mc) ;
    System.out.println("authScheme" + authScheme) ;
    System.out.println("authSchemeData " + authSchemeData) ;
    System.out.println("extraProps " + extraProps) ;

    return super.validateAccessToken(mc, authScheme, authSchemeData, extraProps) ;
    // try
    // {
    // JwtToken token = super.getJwtToken(authSchemeData) ;
    // return convertClaimsToValidation(token.getClaims()) ;
    // }
    // catch (Exception ex)
    // {
    // throw new OAuthServiceException(ex) ;
    // }
  }
}
