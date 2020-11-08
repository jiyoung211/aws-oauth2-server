package example.api ;

import io.swagger.annotations.Api ;
import io.swagger.annotations.ApiOperation ;
import io.swagger.annotations.ApiResponse ;
import io.swagger.annotations.ApiResponses ;

import javax.ws.rs.Produces ;
import javax.ws.rs.core.MediaType ;

import org.slf4j.Logger ;
import org.slf4j.LoggerFactory ;
import org.springframework.stereotype.Controller ;
import org.springframework.ui.Model ;
import org.springframework.web.bind.annotation.RequestMapping ;
import org.springframework.web.bind.annotation.RequestMethod ;
import org.springframework.web.bind.annotation.RequestParam ;

@Controller
@Api(tags = { "JaxRs" })
@ApiResponses(value = { @ApiResponse(code = 100, message = "Continue"), @ApiResponse(code = 101, message = "Switching Protocols"), @ApiResponse(code = 102, message = "Processing"), @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 201, message = "Created"), @ApiResponse(code = 202, message = "Accepted"), @ApiResponse(code = 203, message = "Non-authoritative Information"), @ApiResponse(code = 204, message = "No Content"), @ApiResponse(code = 205, message = "Reset Content"), @ApiResponse(code = 206, message = "Partial Content"), @ApiResponse(code = 207, message = "Multi-Status"), @ApiResponse(code = 208, message = "Already Reported"), @ApiResponse(code = 226, message = "IM Used"), @ApiResponse(code = 300, message = "Multiple Choices"), @ApiResponse(code = 301, message = "Moved Permanently"), @ApiResponse(code = 302, message = "Found"), @ApiResponse(code = 303, message = "See Other"), @ApiResponse(code = 304, message = "Not Modified"), @ApiResponse(code = 305, message = "Use Proxy"), @ApiResponse(code = 307, message = "Temporary Redirect"), @ApiResponse(code = 308, message = "Permanent Redirect"), @ApiResponse(code = 400, message = "Bad Request"), @ApiResponse(code = 401, message = "Unauthorized"), @ApiResponse(code = 402, message = "Payment Required"), @ApiResponse(code = 403, message = "Forbidden"), @ApiResponse(code = 404, message = "Not Found"), @ApiResponse(code = 405, message = "Method Not Allowed"), @ApiResponse(code = 406, message = "Not Acceptable"), @ApiResponse(code = 407, message = "Proxy Authentication Required"), @ApiResponse(code = 408, message = "Request Timeout"), @ApiResponse(code = 409, message = "Conflict"), @ApiResponse(code = 410, message = "Gone"), @ApiResponse(code = 411, message = "Length Required"), @ApiResponse(code = 412, message = "Precondition Failed"), @ApiResponse(code = 413, message = "Payload Too Large"), @ApiResponse(code = 414, message = "Request-URI Too Long"), @ApiResponse(code = 415, message = "Unsupported Media Type"), @ApiResponse(code = 416, message = "Requested Range Not Satisfiable"), @ApiResponse(code = 417, message = "Expectation Failed"), @ApiResponse(code = 418, message = "I'm a teapot"), @ApiResponse(code = 421, message = "Misdirected Request"), @ApiResponse(code = 422, message = "Unprocessable Entity"), @ApiResponse(code = 423, message = "Locked"), @ApiResponse(code = 424, message = "Failed Dependency"), @ApiResponse(code = 426, message = "Upgrade Required"), @ApiResponse(code = 428, message = "Precondition Required"), @ApiResponse(code = 429, message = "Too Many Requests"), @ApiResponse(code = 431, message = "Request Header Fields Too Large"), @ApiResponse(code = 444, message = "Connection Closed Without Response"), @ApiResponse(code = 451, message = "Unavailable For Legal Reasons"), @ApiResponse(code = 499, message = "Client Closed Request"), @ApiResponse(code = 500, message = "Internal Server Error"), @ApiResponse(code = 501, message = "Not Implemented"), @ApiResponse(code = 502, message = "Bad Gateway"), @ApiResponse(code = 503, message = "Service Unavailable"), @ApiResponse(code = 504, message = "Gateway Timeout"), @ApiResponse(code = 505, message = "HTTP Version Not Supported"), @ApiResponse(code = 506, message = "Variant Also Negotiates"), @ApiResponse(code = 507, message = "Insufficient Storage"), @ApiResponse(code = 508, message = "Loop Detected"), @ApiResponse(code = 510, message = "Not Extended"), @ApiResponse(code = 511, message = "Network Authentication Required"), @ApiResponse(code = 599, message = "Network Connect Timeout Error") })
public class SampleRestController
{
  // cxf logging
  // protected static final java.util.logging.Logger LOG =
  // LogUtils.getL7dLogger(AbstractOAuthService.class);

  // org.slf4j.Logger
  private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

  @RequestMapping(value = "/rest", method = RequestMethod.GET)
  @ApiOperation(value = "Adiciona un producto a una lista de Productos", consumes = MediaType.APPLICATION_JSON, produces = MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_XML)
  public String main(Model model)
  {
    System.out.println("rest") ;

    model.addAttribute("requestMessage", "main") ;
    model.addAttribute("responseMessage", "main") ;

    return "rest" ;
  }

  @RequestMapping(value = "/rest/{input}", method = RequestMethod.GET)
  public String input(Model model, @RequestParam("input") String input)
  {
    model.addAttribute("requestMessage", input) ;
    model.addAttribute("responseMessage", input) ;

    return "rest" ;
  }

  // @GET
  // @Path("/{input}")
  // @Produces("text/plain")
  // public String test(@PathParam("input") String input)
  // {
  // System.out.println("test API parameters:" + input) ;
  // return input ;
  // }

}
