package demo.wssec.common ;

import org.apache.cxf.common.injection.NoJSR250Annotations ;
import org.apache.cxf.interceptor.LoggingMessage ;

/**
 * A simple logging handler which outputs the bytes of the message to the
 * Logger.
 * 
 * @deprecated use the logging module rt/features/logging instead
 */
@NoJSR250Annotations
@Deprecated
public class SaveLoggingOutInterceptor extends org.apache.cxf.interceptor.LoggingOutInterceptor
{
  public String outboundMessage = new String("initial") ;

  // @Override
  // public void handleMessage(Message message) throws Fault
  // {
  // outboundMessage = createLogMessage(message) ;
  //
  // }
  @Override
  protected String formatLoggingMessage(LoggingMessage buffer)
  {
    outboundMessage = super.formatLoggingMessage(buffer) ;
    // TODO Auto-generated method stub
    return outboundMessage ;
  }

}
