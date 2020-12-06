package example.swagger ;

import java.util.ArrayList ;
import java.util.List ;

import org.springframework.context.annotation.Configuration ;
import org.springframework.web.servlet.config.annotation.CorsRegistry ;
import org.springframework.web.servlet.config.annotation.EnableWebMvc ;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry ;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry ;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter ;

@Configuration
@EnableWebMvc
// @EnableSwagger2
// @ComponentScan(basePackages = "com.naver", useDefaultFilters = false,
// includeFilters = { @ComponentScan.Filter(type = FilterType.ANNOTATION, value
// = Controller.class), @ComponentScan.Filter(type = FilterType.ANNOTATION,
// value = ControllerAdvice.class) })
// @Import({ CoreApplicationContextConfiguration.class, SwaggerConfig.class })
public class SpringMvcConfiguration extends WebMvcConfigurerAdapter
{

  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry)
  {
    System.out.println("#### SpringMvcConfiguration addResourceHandlers") ;

    registry.addResourceHandler("/static/**").addResourceLocations("/static/") ;
    // swagger 설정
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/") ;
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/") ;
  }

  /* interceptor 에서 swagger ui 관련 URL 제외 */

  @Override
  public void addInterceptors(InterceptorRegistry registry)
  {
    System.out.println("#### SpringMvcConfiguration addInterceptors") ;

    // HandlerInterceptorAdapter
    // new HttpInterceptor()
    // WebContentInterceptor
    // WebRequestHandlerInterceptorAdapter

    // registry.addInterceptor(naverLoginInterceptor()).excludePathPatterns("/static/**",
    // "swagger-ui.html",
    // "/webjars/**",
    // "/v2/api-docs",
    // "/configuration/security",
    // "/configuration/ui", "/swagger-resources") ;

    List<String> excludePathPatterns = new ArrayList<String>() ;
    excludePathPatterns.add("swagger-ui.html") ;
    excludePathPatterns.add("/swagger-ui.html") ;
    excludePathPatterns.add("/webjars/**") ;
    excludePathPatterns.add("/v2/api-docs") ;
    excludePathPatterns.add("/v2/**") ;
    excludePathPatterns.add("/configuration/security") ;
    excludePathPatterns.add("/configuration/ui") ;
    excludePathPatterns.add("/swagger-resources") ;
    excludePathPatterns.add("/swagger-resources/**") ;

    excludePathPatterns.add("/app") ;
    excludePathPatterns.add("/app/**") ;
    excludePathPatterns.add("app/swagger-ui.html") ;
    excludePathPatterns.add("/app/swagger-ui.html") ;
    excludePathPatterns.add("/app/webjars/**") ;
    excludePathPatterns.add("/app/v2/api-docs") ;
    excludePathPatterns.add("/app/v2/**") ;
    excludePathPatterns.add("/app/configuration/security") ;
    excludePathPatterns.add("/app/configuration/ui") ;
    excludePathPatterns.add("/app/swagger-resources") ;
    excludePathPatterns.add("/app/swagger-resources/**") ;
    registry.addInterceptor(new LoginInterceptor()).excludePathPatterns(excludePathPatterns) ;

  }

  @Override
  public void addCorsMappings(CorsRegistry registry)
  {
    registry.addMapping("/**").allowedOrigins("*") ;
  }

}