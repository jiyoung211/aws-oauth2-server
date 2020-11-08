package example.api ;

import java.util.HashMap ;
import java.util.Map ;

import org.apache.commons.lang3.exception.ExceptionUtils ;
import org.apache.cxf.hello_world_soap_http.Greeter ;
import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.context.support.ClassPathXmlApplicationContext ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.Model ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;

import demo.wssec.client.Client ;
import demo.wssec.server.Server ;

@Controller
public class SampleSoapController
{
  // cxf logging
  // protected static final java.util.logging.Logger LOG =
  // LogUtils.getL7dLogger(AbstractOAuthService.class);

  // org.slf4j.Logger
  private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

  // @RequestMapping(value = "/soapClient/server", method = RequestMethod.GET)
  // public String soapClientServer(Model model)
  // {
  // Client client = new Client() ;
  // String input ;
  // try
  // {
  // input = client.callServer() ;
  // }
  // catch (Exception e)
  // {
  // input = ExceptionUtils.getRootCauseMessage(e) ;
  // e.printStackTrace() ;
  // }
  // model.addAttribute("requestMessage", input) ;
  // model.addAttribute("responseMessage", input) ;
  //
  // return "saaj" ;
  // }

  @RequestMapping(value = "/soapClient/main", method = RequestMethod.GET)
  public String soapClientMain(Model model, @RequestParam("operation") String operation, @RequestParam("requestParam") String requestParam)
  {
    Client client = new Client() ;
    Map<String, String> input = new HashMap<String, String>() ;
    try
    {
      input = client.callMain(operation, requestParam) ;
    }
    catch (Exception e)
    {
      input.put("res", ExceptionUtils.getRootCauseMessage(e)) ;
      e.printStackTrace() ;
    }

    Server server = new Server() ;

    try
    {
      server.callMain() ;
    }
    catch (Exception e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace() ;
    }

    model.addAttribute("req", input.get("req")) ;
    model.addAttribute("res", input.get("res")) ;
    model.addAttribute("reqSoapMessage", input.get("reqSoapMessage")) ;
    model.addAttribute("resSoapMessage", input.get("resSoapMessage")) ;
    model.addAttribute("reqSoapMessageServer", input.get("reqSoapMessageServer")) ;
    model.addAttribute("resSoapMessageServer", input.get("resSoapMessageServer")) ;

    return "wssecurity" ;
  }

  @RequestMapping(value = "/soapClient/spring", method = RequestMethod.GET)
  public String soapClientSpring(Model model)
  {

    ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "/WEB-INF/beansApi.xml" }) ;

    Greeter client = (Greeter) context.getBean("myWsSecClient") ;

    String input = null ;
    String[] names = new String[] { "Anne", "Bill", "Chris", "Sachin Tendulkar" } ;
    for (int i = 0 ; i < 1 ; i++)
    {
      System.out.println("Invoking greetMe...") ;
      String response = client.greetMe(names[i]) ;
      input = response ;
      System.out.println("response: " + response + "\n") ;
    }
    model.addAttribute("requestMessage", input) ;
    model.addAttribute("responseMessage", input) ;

    return "saaj" ;
  }
}
