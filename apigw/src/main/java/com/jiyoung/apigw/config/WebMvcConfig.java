package com.jiyoung.apigw.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class WebMvcConfig implements WebMvcConfigurer
{

	@Override
	public void addCorsMappings(CorsRegistry registry)
	{
		log.debug("############################ addCorsMappings ############################");
		registry.addMapping("/**").allowedMethods("*").allowedOrigins("*");
	}
}
