package com.sortimo.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sortimo.security.JwtAuthenticationEntryPoint;
import com.sortimo.security.JwtAuthenticationTokenFilter;
import com.sortimo.services.MyUserDetailsService;

@Profile("!dev")
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	MyUserDetailsService myUserDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService);
	}
	
    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;
	
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {
		
		
        httpSecurity
        // we don't need CSRF because our token is invulnerable
        .csrf().disable()

        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

        // don't create session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()

        .authorizeRequests()
        //.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()

        // allow anonymous resource requests
        .antMatchers(
                HttpMethod.GET,
                "/",
                "/*.html",
                "/favicon.ico",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
        ).permitAll()
        .antMatchers("/auth/**").permitAll()
        .anyRequest().authenticated();

		// Custom JWT based security filter
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);
		
		// disable page caching
		httpSecurity.headers().cacheControl();
		
		
		
		
		
//		http.addFilterBefore(new CustomLoginFilter(), UsernamePasswordAuthenticationFilter.class)
//		.authorizeRequests()
//		.and()
//		.formLogin();
		
//        http
//        .authorizeRequests()
//        .antMatchers("/api/**").authenticated()
//
//        // Allow anonymous resource requests
//        .antMatchers("/").permitAll()
//        .antMatchers("/favicon.ico").permitAll()
//        .antMatchers("**/*.html").permitAll()
//        .antMatchers("**/*.css").permitAll()
//        .antMatchers("**/*.js").permitAll()
//
//        // Allow anonymous logins
//        .antMatchers("/login/**").permitAll()
//
//        // All other request need to be authenticated
//        .anyRequest().authenticated().and()
//
//        // Custom Token based authentication based on the header previously given to the client
//        .addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService),
//                UsernamePasswordAuthenticationFilter.class);
		
		
//        http.csrf().disable() // disable csrf for our requests.
//        .authorizeRequests()
//        .antMatchers("/api/**").authenticated()
//        .antMatchers(HttpMethod.GET, "/login").permitAll()
////        .anyRequest().authenticated()
//        .and()
//        // We filter the api/login requests
//        .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()), UsernamePasswordAuthenticationFilter.class)
//        // And filter other requests to check the presence of JWT in header
//        .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);		
////		
	}
}
