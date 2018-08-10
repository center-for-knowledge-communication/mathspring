package edu.umass.ckc.wo.svc.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by marshall on 7/13/17.
 */
@Configuration
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Setup a simple strategy: use all the defaults and return JSON by default when not sure.
     */
//    @Override
//    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
//        configurer.defaultContentType(MediaType.APPLICATION_JSON).useJaf(false).ignoreAcceptHeader(true).favorPathExtension(true);
//    }
}