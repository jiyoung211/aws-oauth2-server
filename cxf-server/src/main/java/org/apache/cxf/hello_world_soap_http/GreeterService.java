package org.apache.cxf.hello_world_soap_http ;

import java.net.MalformedURLException ;
import java.net.URL ;

import javax.xml.namespace.QName ;
import javax.xml.ws.Service ;
import javax.xml.ws.WebEndpoint ;
import javax.xml.ws.WebServiceClient ;
import javax.xml.ws.WebServiceFeature ;

/**
 * This class was generated by Apache CXF 3.3.7 2020-07-09T20:34:10.511+09:00
 * Generated source version: 3.3.7
 *
 */
@WebServiceClient(name = "GreeterService", wsdlLocation = "http://localhost:7070/Service/GreeterPort?WSDL", targetNamespace = "http://cxf.apache.org/hello_world_soap_http")
public class GreeterService extends Service
{

  public final static URL WSDL_LOCATION ;

  public final static QName SERVICE = new QName("http://cxf.apache.org/hello_world_soap_http", "GreeterService") ;
  public final static QName GreeterPort = new QName("http://cxf.apache.org/hello_world_soap_http", "GreeterPort") ;
  static
  {
    URL url = null ;
    try
    {
      url = new URL("http://localhost:7070/Service/GreeterPort?WSDL") ;
    }
    catch (MalformedURLException e)
    {
      java.util.logging.Logger.getLogger(GreeterService.class.getName()).log(java.util.logging.Level.INFO, "Can not initialize the default wsdl from {0}", "http://localhost:7070/Service/GreeterPort?WSDL") ;
    }
    WSDL_LOCATION = url ;
  }

  public GreeterService(URL wsdlLocation)
  {
    super(wsdlLocation, SERVICE) ;
  }

  public GreeterService(URL wsdlLocation, QName serviceName)
  {
    super(wsdlLocation, serviceName) ;
  }

  public GreeterService()
  {
    super(WSDL_LOCATION, SERVICE) ;
  }

  public GreeterService(WebServiceFeature... features)
  {
    super(WSDL_LOCATION, SERVICE, features) ;
  }

  public GreeterService(URL wsdlLocation, WebServiceFeature... features)
  {
    super(wsdlLocation, SERVICE, features) ;
  }

  public GreeterService(URL wsdlLocation, QName serviceName, WebServiceFeature... features)
  {
    super(wsdlLocation, serviceName, features) ;
  }

  /**
   *
   * @return returns Greeter
   */
  @WebEndpoint(name = "GreeterPort")
  public Greeter getGreeterPort()
  {
    return super.getPort(GreeterPort, Greeter.class) ;
  }

  /**
   *
   * @param features
   *          A list of {@link javax.xml.ws.WebServiceFeature} to configure on
   *          the proxy. Supported features not in the <code>features</code>
   *          parameter will have their default values.
   * @return returns Greeter
   */
  @WebEndpoint(name = "GreeterPort")
  public Greeter getGreeterPort(WebServiceFeature... features)
  {
    return super.getPort(GreeterPort, Greeter.class, features) ;
  }

}