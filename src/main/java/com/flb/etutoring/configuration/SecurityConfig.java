package com.flb.etutoring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.flb.etutoring.services.UserService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    UserService myUserService() {
        return new UserService();
    };

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auProvider = new DaoAuthenticationProvider();

        auProvider.setUserDetailsService(myUserService());
        auProvider.setPasswordEncoder(passwordEncoder());

        return auProvider;
    }

    /*
     * @Bean
     * public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
     * http.authorizeRequests()
     * .anyRequest()
     * .authenticated()
     * .and()
     * .formLogin()
     * .loginPage("/login")
     * .permitAll()
     * .and()
     * .logout()
     * .permitAll();
     * return http.build();
     * }
     */

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/registry")
                .permitAll()
                .antMatchers("/saveRegistro")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**");
        web.ignoring().antMatchers("/js/**");
        web.ignoring().antMatchers("/img/**");
    }

}
