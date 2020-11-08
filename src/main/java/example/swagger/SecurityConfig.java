package example.swagger ;

import org.springframework.context.annotation.Bean ;
import org.springframework.security.config.annotation.web.builders.HttpSecurity ;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter ;
import org.springframework.web.cors.CorsConfiguration ;
import org.springframework.web.cors.CorsConfigurationSource ;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource ;

import com.google.common.collect.ImmutableList ;

//@Configuration
//@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception
  {
    httpSecurity.authorizeRequests().antMatchers("/", "/").permitAll() ;
    // httpSecurity.csrf().disable() ;
    // httpSecurity.cors().disable() ;
    // httpSecurity.headers().frameOptions().disable() ;
    httpSecurity.cors() ;
  }

  @Bean
  public CorsConfigurationSource corsConfigurationSource()
  {
    final CorsConfiguration configuration = new CorsConfiguration() ;
    configuration.setAllowedOrigins(ImmutableList.of("*")) ;
    configuration.setAllowedMethods(ImmutableList.of("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH")) ;
    configuration.setAllowCredentials(true) ;
    configuration.setAllowedHeaders(ImmutableList.of("Authorization", "Cache-Control", "Content-Type")) ;
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource() ;
    source.registerCorsConfiguration("/**", configuration) ;
    return source ;
  }
}