package example.swagger ;

//import com.itcast.tpms.exp.UserExp;

import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

import org.springframework.web.servlet.HandlerInterceptor ;
import org.springframework.web.servlet.ModelAndView ;

public class LoginInterceptor implements HandlerInterceptor
{
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
  {
    // UserExp userExp = (UserExp) request.getSession().getAttribute("userExp");
    // if (userExp != null) {
    // Cookie[] cookies = request.getCookies();
    // String account = null;
    // for (Cookie cookie : cookies) {
    // if ("account".equals(cookie.getName())) {
    // account = cookie.getValue();
    // break;
    // }
    // }
    // if (account.equals(userExp.getUser().getAccount())) {
    // return true;
    // }
    // }
    // request.getRequestDispatcher("/page/login").forward(request, response);
    // return false;
    return true ;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
  {
    // TODO Auto-generated method stub

  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception
  {
    // TODO Auto-generated method stub

  }

}