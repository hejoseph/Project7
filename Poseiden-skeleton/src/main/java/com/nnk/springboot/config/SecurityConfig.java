package com.nnk.springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

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
        http.authorizeRequests().antMatchers("/bidList/**","/user/**", "/rating/**", "/ruleName/**", "/trade/**", "/curvePoint/**").hasAnyRole("ADMIN", "USER").antMatchers("/user/**").permitAll().and().formLogin()  //login configuration
                .defaultSuccessUrl("/bidList/list").and().logout()    //logout configuration
                .logoutUrl("/app-logout").logoutSuccessUrl("/").and().exceptionHandling() //exception handling configuration
                .accessDeniedPage("/app/error")
                .and().cors().and().
                csrf().disable();
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and().formLogin()
//                .loginPage("/login").permitAll();
//        http.authorizeRequests().antMatchers(HttpMethod.POST,"/**").permitAll().and().
//                cors().and().
//                csrf().disable();
    }

//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        auth.inMemoryAuthentication()
//                .passwordEncoder(encoder)
//                .withUser("spring")
//                .password(encoder.encode("secret"))
//                .roles("USER");
//    }

}
