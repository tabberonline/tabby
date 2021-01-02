package com.tabber.tabby.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.GET, "/ping").permitAll()
                .antMatchers(HttpMethod.POST,"/email").permitAll()
                .antMatchers(HttpMethod.GET,"/fe/get").permitAll()
                .antMatchers(HttpMethod.GET,"/website").permitAll()
                .antMatchers(HttpMethod.GET,"/website/all").permitAll()
                .antMatchers(HttpMethod.GET,"/user/guest/resume").permitAll()
                .antMatchers(HttpMethod.POST,"/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT,"/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.GET,"/admin/**").hasAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE,"/admin/**").hasAuthority("ADMIN")
                .anyRequest().authenticated();
    }
}
