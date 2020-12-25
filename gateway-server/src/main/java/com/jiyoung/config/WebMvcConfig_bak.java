package com.jiyoung.config;

import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = "com.jiyoung")
public class WebMvcConfig_bak extends WebMvcConfigurationSupport
{
	// static files (html, css, js, etc)
	@Override
	protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
	}

	// use jsp
	@Override
	protected void configureViewResolvers(ViewResolverRegistry registry)
	{
		registry.jsp("/WEB-INF/views/", ".jsp");
	}

	// 컨트롤러 구현 없이 컨트롤러 추가 (단순 페이지 이동이나 정적페이지 처리)
	@Override
	protected void addViewControllers(ViewControllerRegistry registry)
	{
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/main").setViewName("index");
	}

//	@Override
//	protected void addCorsMappings(CorsRegistry registry)
//	{
//		registry.addMapping("/**");
//	}

}
