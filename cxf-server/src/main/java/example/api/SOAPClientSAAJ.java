package example.api ;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream ;
import java.io.File ;
import java.io.FileOutputStream;
import java.util.ArrayList ;
import java.util.List ;

import javax.activation.DataHandler ;
import javax.activation.FileDataSource ;
import javax.servlet.http.HttpSession;
import javax.xml.soap.AttachmentPart ;
import javax.xml.soap.MessageFactory ;
import javax.xml.soap.SOAPBody ;
import javax.xml.soap.SOAPConnection ;
import javax.xml.soap.SOAPConnectionFactory ;
import javax.xml.soap.SOAPElement ;
import javax.xml.soap.SOAPEnvelope ;
import javax.xml.soap.SOAPException ;
import javax.xml.soap.SOAPMessage ;
import javax.xml.soap.SOAPPart ;

import org.apache.commons.lang3.StringUtils ;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.w3c.dom.Node ;
import org.w3c.dom.NodeList ;

class SOAPClientSAAJ
{
  private String myNamespace = "myNamespace" ;
  private String myNamespaceURI = "https://www.w3schools.com/xml/" ;
  private String elementName = "CelsiusToFahrenheit" ;
  private String childElementName = "Celsius" ;
  private String childElementValue = "100" ;
  private String filePath = "" ;

  //MulipartFile로 수정 20.12.07
  private String fileRealPath = "" ;
  private CommonsMultipartFile file = null;


  public SOAPClientSAAJ(String myNamespace, String myNamespaceURI, String elementName, String childElementName, String childElementValue, String filePath)
  {
    if (StringUtils.isNotBlank(myNamespace))
    {
      this.myNamespace = myNamespace ;
    }
    if (StringUtils.isNotBlank(myNamespaceURI))
    {
      this.myNamespaceURI = myNamespaceURI ;
    }
    if (StringUtils.isNotBlank(elementName))
    {
      this.elementName = elementName ;
    }
    if (StringUtils.isNotBlank(childElementName))
    {
      this.childElementName = childElementName ;
    }
    if (StringUtils.isNotBlank(childElementValue))
    {
      this.childElementValue = childElementValue ;
    }
    if (StringUtils.isNotBlank(filePath))
    {
      this.filePath = filePath ;
    }
  }
  
  public SOAPClientSAAJ(String myNamespace, String myNamespaceURI, String elementName, String childElementName, String childElementValue, CommonsMultipartFile  file, HttpSession session)
  {
    if (StringUtils.isNotBlank(myNamespace))
    {
      this.myNamespace = myNamespace ;
    }
    if (StringUtils.isNotBlank(myNamespaceURI))
    {
      this.myNamespaceURI = myNamespaceURI ;
    }
    if (StringUtils.isNotBlank(elementName))
    {
      this.elementName = elementName ;
    }
    if (StringUtils.isNotBlank(childElementName))
    {
      this.childElementName = childElementName ;
    }
    if (StringUtils.isNotBlank(childElementValue))
    {
      this.childElementValue = childElementValue ;
    }
    if (file != null)
    {
      this.file = file ;
    }
    if (session != null)
    {
      this.fileRealPath = session.getServletContext().getRealPath("/");  
    }
  }

  // SAAJ - SOAP Client Testing //사용하지 않음
  protected void createSoapEnvelope(SOAPMessage soapMessage, String tttt) throws SOAPException
  {
    SOAPPart soapPart = soapMessage.getSOAPPart() ;

    // SOAP Envelope
    SOAPEnvelope envelope = soapPart.getEnvelope() ;
    envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI) ;

    /*
     * Constructed SOAP Request Message: <SOAP-ENV:Envelope
     * xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/"
     * xmlns:myNamespace="https://www.w3schools.com/xml/"> <SOAP-ENV:Header/>
     * <SOAP-ENV:Body> <myNamespace:CelsiusToFahrenheit>
     * <myNamespace:Celsius>100</myNamespace:Celsius>
     * </myNamespace:CelsiusToFahrenheit> </SOAP-ENV:Body> </SOAP-ENV:Envelope>
     */

    // SOAP Body
    SOAPBody soapBody = envelope.getBody() ;
    SOAPElement soapBodyElem = soapBody.addChildElement(elementName, myNamespace) ;

    // SOAP AttachmentPart
    if (StringUtils.isNotBlank(filePath))
    {
      File file = new File(filePath) ;
      FileDataSource fileDataSource = new FileDataSource(file) ;
      DataHandler dataHandler = new DataHandler(fileDataSource) ;
      AttachmentPart attachment = soapMessage.createAttachmentPart(dataHandler) ;
      soapMessage.addAttachmentPart(attachment) ;
    }
    /*
     * 예상치 않은 요소(URI: "http://webservice.cxfexample.exampledriven.org/", 로컬:
     * "Celsius")입니다. 필요한 요소는 (none)입니다.
     */
    // SOAPElement soapBodyElem1 =
    // soapBodyElem.addChildElement(childElementName, myNamespace) ;
    // soapBodyElem1.addTextNode(childElementValue) ;
  }

  
  protected void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException
  {
    SOAPPart soapPart = soapMessage.getSOAPPart() ;

    // SOAP Envelope
    SOAPEnvelope envelope = soapPart.getEnvelope() ;
    envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI) ;

    // SOAP Body
    SOAPBody soapBody = envelope.getBody() ;
    SOAPElement soapBodyElem = soapBody.addChildElement(elementName, myNamespace) ;

    String path= this.fileRealPath;
    
    // SOAP AttachmentPart
    if(this.file != null)
    {
        String filename=file.getOriginalFilename();  
        
       File attachFile =  new File(path + File.separator + filename);
        
        FileDataSource fileDataSource = new FileDataSource(attachFile) ;
        DataHandler dataHandler = new DataHandler(fileDataSource) ;
        AttachmentPart attachment = soapMessage.createAttachmentPart(dataHandler) ;
        soapMessage.addAttachmentPart(attachment) ;
    
    }
  }
  
  protected List<String> callSoapWebService(String soapEndpointUrl, String soapAction )
  {
    SOAPMessage soapRequest = null ;
    SOAPMessage soapResponse = null ;
    List<String> soapResult = new ArrayList<String>() ;
    try
    {
      // Create SOAP Connection
      SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance() ;
      SOAPConnection soapConnection = soapConnectionFactory.createConnection() ;

      // Send SOAP Message to SOAP Server
      soapRequest = createSOAPRequest(soapAction) ;
      soapResponse = soapConnection.call(soapRequest, soapEndpointUrl) ;

      // Print the SOAP Response
      System.out.println("Response SOAP Message:") ;
      soapResponse.writeTo(System.out) ;
      System.out.println() ;

      // Add soapResult
      soapResult.add(SOAPMessageToString(soapRequest)) ;
      soapResult.add(SOAPMessageToString(soapResponse)) ;

      SOAPBody soapResponseBody = soapResponse.getSOAPBody() ;
      NodeList returnList = soapResponseBody.getElementsByTagName(elementName) ;
      for (int i = 0 ; i < returnList.getLength() ; i++)
      {
        NodeList innerResultList = returnList.item(i).getChildNodes() ;
        for (int j = 0 ; j < innerResultList.getLength() ; j++)
        {
          Node result = innerResultList.item(j) ;
          System.out.println("Fahrenheit temperature: " + result.getNodeValue()) ;
        }
      }

      soapConnection.close() ;
    }
    catch (Exception e)
    {
      System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n") ;
      e.printStackTrace() ;
    }

    return soapResult ;
  }

  protected SOAPMessage createSOAPRequest(String soapAction) throws Exception
  {
    // String soapEndpointUrl = "https://www.w3schools.com/xml/tempconvert.asmx"
    // String soapAction = "https://www.w3schools.com/xml/CelsiusToFahrenheit" ;
    MessageFactory messageFactory = MessageFactory.newInstance() ;
    SOAPMessage soapMessage = messageFactory.createMessage() ;

    createSoapEnvelope(soapMessage) ;

    /*
     * Server did not recognize the value of HTTP Header SOAPAction : 서버가 HTTP
     * 헤더 SOAPAction의 값을 인식하지 못했습니다 - wsdl에 soapAction 정의, 요청시 header soapAction
     * 누락
     */

    /*
     * <faultstring>The given SOAPAction
     * https://www.w3schools.com/xml/readAllLocations does not match an
     * operation.</faultstring>
     */
    // MimeHeaders headers = soapMessage.getMimeHeaders() ;
    // headers.addHeader("SOAPAction", soapAction) ;
    // soapMessage.saveChanges() ;

    /* Print the request message, just for debugging purposes */
    System.out.println("Request SOAP Message:") ;
    soapMessage.writeTo(System.out) ;
    System.out.println("\n") ;

    return soapMessage ;
  }

  public String SOAPMessageToString(SOAPMessage soapMessage) throws Exception
  {

    ByteArrayOutputStream out = new ByteArrayOutputStream() ;
    soapMessage.writeTo(out) ;
    String soapResult = new String(out.toByteArray(), "UTF-8") ;
    out.close() ;
    return soapResult ;
  }

}
