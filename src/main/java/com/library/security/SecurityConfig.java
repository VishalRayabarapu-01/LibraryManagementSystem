package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public UserDetailsService service() {
		return new CustomUserDetailsService();
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity security)throws Exception{
		return security
				.userDetailsService(this.service())
				.authorizeHttpRequests()
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/student/**").hasRole("STUDENT")
				.requestMatchers("/**").permitAll()
				.anyRequest().authenticated()
				.and()
				.formLogin()
				.loginPage("/signin")
				.defaultSuccessUrl("/validateLoginUser", true)
				.and()
				.csrf().disable()
//				.formLogin( form -> form
//						.loginPage("/signin")
//						.defaultSuccessUrl("/validateLoginUser", true)
//						.failureUrl("/signin?error")
//						)
				.build();
				
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
		return (web)-> web.ignoring().requestMatchers("/contents/**","/img/**");
	}
}
