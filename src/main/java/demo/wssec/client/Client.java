/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package demo.wssec.client ;

import java.io.Closeable ;
import java.lang.reflect.UndeclaredThrowableException ;
import java.net.URL ;
import java.util.HashMap ;
import java.util.List ;
import java.util.Map ;

import org.apache.commons.codec.binary.StringUtils ;
import org.apache.commons.lang3.exception.ExceptionUtils ;
import org.apache.cxf.Bus ;
import org.apache.cxf.BusFactory ;
import org.apache.cxf.bus.spring.SpringBusFactory ;
import org.apache.cxf.frontend.ClientProxy ;
import org.apache.cxf.hello_world_soap_http.Greeter ;
import org.apache.cxf.hello_world_soap_http.GreeterService ;
import org.apache.cxf.interceptor.Interceptor ;
import org.apache.cxf.message.Message ;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker ;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor ;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor ;

import demo.wssec.common.SaveLoggingInInterceptor ;
import demo.wssec.common.SaveLoggingOutInterceptor ;

/**
 * A DOM-based client JAVA API를 이용하여 클라이언트 작성
 * ->Qname??????????????????????????????????????????????????
 */
public final class Client
{

  private static final String WSSE_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd" ;
  private static final String WSU_NS = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd" ;

  public Client()
  {
  }

  public Map<String, String> callMain(String operation, String requestParam) throws Exception
  {
    // String result = new String() ;
    // List<String> result = new ArrayList<String>();
    Map<String, String> result = new HashMap<String, String>() ;

    try
    {

      SpringBusFactory bf = new SpringBusFactory() ;
      URL busFile = Client.class.getResource("wssec.xml") ;
      Bus bus = bf.createBus(busFile.toString()) ;
      BusFactory.setDefaultBus(bus) ;

      Map<String, Object> outProps = new HashMap<>() ;
      outProps.put("action", "UsernameToken Timestamp Signature Encrypt") ;

      outProps.put("passwordType", "PasswordDigest") ;

      outProps.put("user", "abcd") ;
      outProps.put("signatureUser", "bethal") ;

      outProps.put("passwordCallbackClass", "demo.wssec.client.UTPasswordCallback") ;

      outProps.put("encryptionUser", "morpit") ;
      outProps.put("encryptionPropFile", "etc/Client_Encrypt.properties") ;
      outProps.put("encryptionKeyIdentifier", "IssuerSerial") ;
      outProps.put("encryptionParts", "{Element}{" + WSSE_NS + "}UsernameToken;" + "{Content}{http://schemas.xmlsoap.org/soap/envelope/}Body") ;

      outProps.put("signaturePropFile", "etc/Client_Sign.properties") ;
      outProps.put("signatureKeyIdentifier", "DirectReference") ;
      outProps.put("signatureParts", "{Element}{" + WSU_NS + "}Timestamp;" + "{Element}{http://schemas.xmlsoap.org/soap/envelope/}Body;" + "{}{http://www.w3.org/2005/08/addressing}ReplyTo;") ;

      outProps.put("encryptionKeyTransportAlgorithm", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p") ;
      outProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1") ;

      Map<String, Object> inProps = new HashMap<>() ;

      inProps.put("action", "UsernameToken Timestamp Signature Encrypt") ;
      inProps.put("passwordType", "PasswordText") ;
      inProps.put("passwordCallbackClass", "demo.wssec.client.UTPasswordCallback") ;

      inProps.put("decryptionPropFile", "etc/Client_Sign.properties") ;
      inProps.put("encryptionKeyIdentifier", "IssuerSerial") ;

      inProps.put("signaturePropFile", "etc/Client_Encrypt.properties") ;
      inProps.put("signatureKeyIdentifier", "DirectReference") ;

      inProps.put("encryptionKeyTransportAlgorithm", "http://www.w3.org/2001/04/xmlenc#rsa-oaep-mgf1p") ;
      inProps.put("signatureAlgorithm", "http://www.w3.org/2000/09/xmldsig#rsa-sha1") ;

      // Check to make sure that the SOAP Body and Timestamp were signed, // and
      // that the SOAP Body was encrypted
      DefaultCryptoCoverageChecker coverageChecker = new DefaultCryptoCoverageChecker() ;
      coverageChecker.setSignBody(true) ;
      coverageChecker.setSignTimestamp(true) ;
      coverageChecker.setEncryptBody(true) ;

      // jaxws:client //
      // serviceClass="org.apache.cxf.hello_world_soap_http.GreeterService"
      GreeterService service = new GreeterService() ;
      Greeter port = service.getGreeterPort() ;
      org.apache.cxf.endpoint.Client client = ClientProxy.getClient(port) ;

      // jaxws:inInterceptors bean
      // class="org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor.WSS4JInInterceptor"
      client.getInInterceptors().add(new WSS4JInInterceptor(inProps)) ;

      // jaxws:outInterceptors bean class=
      // "org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor.WSS4JOutInterceptor"
      client.getOutInterceptors().add(new WSS4JOutInterceptor(outProps)) ;
      client.getInInterceptors().add(coverageChecker) ;

      // add
      Map<String, Object> loggingProps = new HashMap<>() ;
      loggingProps.put("prettyLogging", true) ;
      client.getInInterceptors().add(new SaveLoggingInInterceptor()) ;
      client.getOutInterceptors().add(new SaveLoggingOutInterceptor()) ;

      client.getEndpoint().getInInterceptors().add(new SaveLoggingInInterceptor()) ;
      client.getEndpoint().getOutInterceptors().add(new SaveLoggingOutInterceptor()) ;

      String response = null ;

      System.out.println("Invoking greetMe...") ;
      if (StringUtils.equals(operation, "greetMe"))
      {
        response = port.greetMe(requestParam) ;
      }
      else if (StringUtils.equals(operation, "greetMeOneWay"))
      {
        port.greetMeOneWay(requestParam) ;
        response = new String("success") ;
      }
      else if (StringUtils.equals(operation, "greetMe"))
      {
        response = port.sayHi() ;
      }
      else
      {
        response = new String("Not Support Operation") ;
      }
      System.out.println("response: " + response + "\n") ;

      // allow asynchronous resends to occur Thread.sleep(10 * 1000);
      String reqSoapMessage = new String("SaveLoggingInInterceptor Not found ") ;
      String resSoapMessage = new String("SaveLoggingOutInterceptor Not found ") ;
      String reqSoapMessageServer = new String("SaveLoggingInInterceptor Not found ") ;
      String resSoapMessageServer = new String("SaveLoggingOutInterceptor Not found ") ;

      List<Interceptor<? extends Message>> outInterceptors = client.getOutInterceptors() ;
      for (Interceptor<? extends Message> outInterceptor : outInterceptors)
      {
        if (outInterceptor instanceof SaveLoggingOutInterceptor)
        {
          reqSoapMessage = ((SaveLoggingOutInterceptor) outInterceptor).outboundMessage ;
        }
      }

      List<Interceptor<? extends Message>> inInterceptors = client.getInInterceptors() ;
      for (Interceptor<? extends Message> inInterceptor : inInterceptors)
      {
        if (inInterceptor instanceof SaveLoggingInInterceptor)
        {
          resSoapMessage = ((SaveLoggingInInterceptor) inInterceptor).inboundMessage ;
        }
      }

      // ClassPathXmlApplicationContext context = new
      // ClassPathXmlApplicationContext(new String[] { "/WEB-INF/beansApi.xml"
      // }) ;
      // SpringEndpointImpl endpoint = (SpringEndpointImpl)
      // context.getBean("myWsSecServer") ;
      // System.out.println("########TEST : " + endpoint.getAddress()) ;

      org.apache.cxf.endpoint.Endpoint endpoint = client.getEndpoint() ;

      inInterceptors = endpoint.getInInterceptors() ;
      for (Interceptor<? extends Message> inInterceptor : inInterceptors)
      {
        if (inInterceptor instanceof SaveLoggingInInterceptor)
        {
          reqSoapMessageServer = ((SaveLoggingInInterceptor) inInterceptor).inboundMessage ;
        }
      }

      outInterceptors = endpoint.getOutInterceptors() ;
      for (Interceptor<? extends Message> outInterceptor : outInterceptors)
      {
        if (outInterceptor instanceof SaveLoggingOutInterceptor)
        {
          resSoapMessageServer = ((SaveLoggingOutInterceptor) outInterceptor).outboundMessage ;
        }
      }

      result.put("req", requestParam) ;
      result.put("res", response) ;
      result.put("reqSoapMessage", reqSoapMessage) ;
      result.put("resSoapMessage", resSoapMessage) ;
      result.put("reqSoapMessageServer", reqSoapMessageServer) ;
      result.put("resSoapMessageServer", resSoapMessageServer) ;

      if (port instanceof Closeable)
      {
        ((Closeable) port).close() ;
      }

      bus.shutdown(true) ;

    }
    catch (UndeclaredThrowableException ex)
    {
      result.put("res", ExceptionUtils.getRootCauseMessage(ex)) ;
      ex.getUndeclaredThrowable().printStackTrace() ;
    }
    catch (Exception ex)
    {
      result.put("res", ExceptionUtils.getRootCauseMessage(ex)) ;
      ex.printStackTrace() ;
    }
    return result ;
  }

}
