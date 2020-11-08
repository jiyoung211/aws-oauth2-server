package example.swagger ;

import java.util.List ;

import org.springframework.context.annotation.Bean ;
import org.springframework.context.annotation.Configuration ;
import org.springframework.http.converter.HttpMessageConverter ;
import org.springframework.http.converter.json.GsonHttpMessageConverter ;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry ;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport ;

import springfox.documentation.builders.ApiInfoBuilder ;
import springfox.documentation.builders.PathSelectors ;
import springfox.documentation.builders.RequestHandlerSelectors ;
import springfox.documentation.service.ApiInfo ;
import springfox.documentation.service.Contact ;
import springfox.documentation.spi.DocumentationType ;
import springfox.documentation.spring.web.plugins.Docket ;
import springfox.documentation.swagger2.annotations.EnableSwagger2 ;

import com.google.gson.Gson ;

@Configuration
@EnableSwagger2
// @ComponentScan("example.api")
public class SwaggerConfig extends WebMvcConfigurationSupport
{
  private final Gson gson = new Gson() ;

  @Bean
  public Docket productApi()
  {
    return new Docket(DocumentationType.SWAGGER_2).select()
    // .apis(RequestHandlerSelectors.basePackage("example.api"))
        .apis(RequestHandlerSelectors.any())
        // .paths(regex("/product.*"))
        .paths(PathSelectors.any()).build().apiInfo(metaData()).useDefaultResponseMessages(false) ;
  }

  private ApiInfo metaData()
  {
    return new ApiInfoBuilder().title("SwaggerConfig metaData title").description("SwaggerConfig metaData description").version("SwaggerConfig metaData version").license("SwaggerConfig metaData license ").licenseUrl("SwaggerConfig metaData licenseUrl").contact(new Contact("Contact John Thompson", "Contact https://springframework.guru/about/", "Contact john@springfrmework.guru")).build() ;

    // .title("Spring Boot REST API")
    // .description("\"Spring Boot REST API for Online Store\"")
    // .version("1.0.0")
    // .license("Apache License Version 2.0")
    // .licenseUrl("https://www.apache.org/licenses/LICENSE-2.0\"")
    // .contact(new Contact("John Thompson",
    // "https://springframework.guru/about/", "john@springfrmework.guru"))
    // .build();
  }

  @Override
  protected void addResourceHandlers(ResourceHandlerRegistry registry)
  {
    registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/") ;
    registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/") ;
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters)
  {
    GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter() ;
    gsonHttpMessageConverter.setGson(gson) ;
    converters.add(gsonHttpMessageConverter) ;
  }
}
