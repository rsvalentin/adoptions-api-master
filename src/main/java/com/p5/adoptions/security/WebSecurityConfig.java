package com.p5.adoptions.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
    private final DataSource dataSource;
    private final AdoptionPasswordEncoder passwordEncoder;
    private final AdoptionUserDetailsService userDetailsService;

    public WebSecurityConfig(DataSource dataSource, AdoptionPasswordEncoder passwordEncoder, AdoptionUserDetailsService userDetailsService)
    {
        this.dataSource = dataSource;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.httpBasic()
                .and().authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/shelters").permitAll()
                .antMatchers(HttpMethod.GET, "/api/v1/shelters/*").hasRole("USER")
                .anyRequest().authenticated()
                .and().csrf().disable();

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }
}