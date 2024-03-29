package edu.uclm.esi.ds.account.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
@Configuration
public class SecurityConfig {
	@Bean
	SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
		.authorizeHttpRequests()
		.requestMatchers("/**")
		.permitAll();
		DefaultSecurityFilterChain filterChain = http.build();
		return filterChain;
	} 
}

