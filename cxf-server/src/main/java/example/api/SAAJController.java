package example.api ;

import java.util.ArrayList ;
import java.util.List ;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.exception.ExceptionUtils ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.Model ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView ;

@Controller
public class SAAJController
{
  // cxf logging
  // protected static final java.util.logging.Logger LOG =
  // LogUtils.getL7dLogger(AbstractOAuthService.class);

  // org.slf4j.Logger
  private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

  
  //String filePath -> CommonsMultipartFile file로 수정 20.12.07
	/*
	 * @RequestMapping(value = "/main", method = RequestMethod.GET) public String
	 * main(Model model) { String soapEndpointUrl =
	 * "http://localhost:7070/services/soap" ; String soapAction = "" ; String
	 * myNamespace = "web" ; String myNamespaceURI =
	 * "http://webservice.cxfexample.exampledriven.org/" ; String elementName =
	 * "readAllLocations" ; String childElementName = "" ; String childElementValue
	 * = "" ;
	 * 
	 * return main1(model, soapEndpointUrl, soapAction, myNamespace, myNamespaceURI,
	 * elementName, childElementName, childElementValue, "") ; }
	 */

  @RequestMapping(value = "/main1", method = RequestMethod.POST)
  public String main1(Model model, @RequestParam("soapEndpointUrl") String soapEndpointUrl, @RequestParam("soapAction") String soapAction, 
		  @RequestParam("myNamespace") String myNamespace, @RequestParam("myNamespaceURI") String myNamespaceURI,
		  @RequestParam("elementName") String elementName, @RequestParam("childElementName") String childElementName, 
		  @RequestParam("childElementValue") String childElementValue, @RequestParam CommonsMultipartFile file, HttpSession session )
//  @RequestParam("filePath") String filePath)
  {
    SOAPClientSAAJ sOAPClientSAAJ = new SOAPClientSAAJ(myNamespace, myNamespaceURI, elementName, childElementName, childElementValue, file, session) ;
    String requestMessage = new String() ;
    String responseMessage = new String() ;
    List<String> result = new ArrayList<String>() ;

    try
    {
      result = sOAPClientSAAJ.callSoapWebService(soapEndpointUrl, soapAction) ;
      requestMessage = result.get(0) ;
      responseMessage = result.get(1) ;
    }
    catch (Exception e)
    {
      model.addAttribute("responseMessage", ExceptionUtils.getRootCauseMessage(e)) ;
      e.printStackTrace() ;
    }
    model.addAttribute("requestMessage", requestMessage) ;
    model.addAttribute("responseMessage", responseMessage) ;

    return "saaj" ;
  }

  @RequestMapping(value = "/helloworld", method = RequestMethod.GET)
  public ModelAndView example()
  {
    /*
     * param1. \WEB-INF\views\saaj.jsp
     * 
     * param2. ${message}
     * 
     * param3. Hello World
     */
    return new ModelAndView("saaj", "message", "Hello World") ;
  }
}
