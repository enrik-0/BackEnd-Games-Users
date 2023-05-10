package edu.uclm.esi.ds.account.config;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
@Configuration
@EnableWebSecurity
public class SecurityConfig implements WebSecurityConfigurer<WebSecurity> {
    
	/*
	@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("usuario")
                .password("{noop}1234")
                .roles("USER");
        
    }
    */
    public void configure(HttpSecurity http) throws Exception {
           http.authorizeRequests().anyRequest().permitAll();
    }



	@Override
	public void init(WebSecurity builder) throws Exception {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void configure(WebSecurity builder) throws Exception {
		// TODO Auto-generated method stub
		
	}
}

