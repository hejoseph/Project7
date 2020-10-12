package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This configuration file is aimed to configure secure login to admin and users and specify passsword validation method
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceCustom userDetailsService;

    /**
     * This method allows endpoints access to users and related actions on behalf
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/bidList/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**")
            .hasAnyAuthority("ADMIN", "USER")
            .antMatchers("/user/**")
            .permitAll()
//            .hasAnyAuthority("ADMIN")
            .and().formLogin()  //login configuration
            .defaultSuccessUrl("/bidList/list")
            .and().logout()    //logout configuration
            .logoutUrl("/app-logout")
            .logoutSuccessUrl("/")
            .and().exceptionHandling() //exception handling configuration
            .accessDeniedPage("/app/error")
            .and().cors()
            .and().csrf().disable();
    }

    /**
     * This method encode the password that is informed by a new user
     * @param auth is user or admin info that are informed at sign in form
     * @throws Exception
     */
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        BCryptPasswordEncoder
                passwordEncoder =
                new BCryptPasswordEncoder();
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

}
