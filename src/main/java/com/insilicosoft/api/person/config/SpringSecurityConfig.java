package com.insilicosoft.api.person.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

/**
 * Derived from <a href="https://mkyong.com/spring-boot/spring-rest-spring-security-example/">Spring REST + Spring Security Example</a>
 * 
 * @see <a href="https://github.com/mkyong/spring-boot/blob/master/spring-rest-security/src/main/java/com/mkyong/config/SpringSecurityConfig.java">mkyong/spring-boot</a>
 */
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
  // Create 2 users for demo
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {

      auth.inMemoryAuthentication()
              .withUser("user").password("{noop}password").roles("USER")
              .and()
              .withUser("admin").password("{noop}password").roles("USER", "ADMIN");

  }

  // Secure the endpoins with HTTP Basic authentication
  @Override
  protected void configure(HttpSecurity http) throws Exception {

      http
              //HTTP Basic authentication
              .httpBasic()
              .and()
              .sessionManagement()
              // Geoff - added to avoid "Set-Cookie: JSESSION=..." cookie baking
              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
              .and()
              .authorizeRequests()
              .antMatchers(HttpMethod.GET, "/persons/**").hasRole("USER")
              .antMatchers(HttpMethod.POST, "/persons").hasRole("ADMIN")
              .antMatchers(HttpMethod.PUT, "/persons/**").hasRole("ADMIN")
              .antMatchers(HttpMethod.DELETE, "/persons/**").hasRole("ADMIN")
              .and()
              .csrf().disable()
              .formLogin().disable();
  }
}