package com.gaurang.api ;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry ;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter ;

public class SwaggerWebApplication extends WebMvcConfigurerAdapter
{
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry)
  {
    // registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/")
    // ;
    // registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/")
    // ;
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/") ;
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/webjars/") ;
  }
}
