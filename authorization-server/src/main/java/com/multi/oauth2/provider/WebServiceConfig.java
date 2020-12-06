package com.multi.oauth2.provider;

import java.util.List;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;


@EnableWs
@EnableWebMvc
@Configuration
public class WebServiceConfig
								extends
									WsConfigurerAdapter {

	// ---------------------------------------------------------------------
	// Section WEB MVC config
	// ---------------------------------------------------------------------
	@Bean
	public ConfigurableServletWebServerFactory webServerFactory() {
		String servicePort = System.getProperty("service.port",
												"8000");
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		factory.setPort(Integer.parseInt(servicePort));
		return factory;
	}

}