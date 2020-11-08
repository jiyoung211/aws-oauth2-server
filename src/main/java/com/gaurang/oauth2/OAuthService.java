package com.gaurang.oauth2 ;

import javax.ws.rs.Path ;

@Path("/")
public class OAuthService
{

  private OAuthDataProviderImpl dataProvider ;

  public void setDataProvider(OAuthDataProviderImpl dataProvider)
  {
    this.dataProvider = dataProvider ;
  }

  /*
   * @GET
   * 
   * @POST
   * 
   * @Path("/{input}")
   * 
   * @Produces("text/html") public String displayValues(@PathParam("input")
   * String input,@QueryParam("code") String code) {
   * System.out.println("displayValues API parameters:"+input);
   * System.out.println("displayValues API code parameters:"+code); return code;
   * }
   */

  // @GET
  // @Path("/client/{clientId}")
  // @Produces("application/json")
  // public Client getClient(@PathParam("clientId") String clientId)
  // {
  // return dataProvider.getClient(clientId) ;
  // }
  //
  // @GET
  // @Path("/client")
  // @Produces("application/json")
  // public List<Client> getClients()
  // {
  // return dataProvider.getClients(null) ;
  // }

  // @POST
  // @Consumes(MediaType.MULTIPART_FORM_DATA)
  // @Path("/registerClientApp")
  // public void registerClientApps(MultipartBody body)
  // {
  //
  // String appName = body.getAttachmentObject("appName", String.class) ;
  // String appURI = body.getAttachmentObject("appURI", String.class) ;
  // String appRedirectURI = body.getAttachmentObject("appRedirectURI",
  // String.class) ;
  // String appDesc = body.getAttachmentObject("appDescription", String.class) ;
  //
  // registerClientApp(appName, appURI, appRedirectURI, appDesc) ;
  //
  // }

}
