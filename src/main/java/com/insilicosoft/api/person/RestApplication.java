package com.insilicosoft.api.person;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Spring Boot base.
 *
 * @author geoff
 */
@ComponentScan(basePackages = { "com.insilicosoft.api.person" })
@EnableAutoConfiguration
@SpringBootApplication
public class RestApplication {

  /**
   * Entry point.
   * 
   * @param args App args.
   */
  public static void main(final String[] args) {
    SpringApplication.run(RestApplication.class, args);
  }
}