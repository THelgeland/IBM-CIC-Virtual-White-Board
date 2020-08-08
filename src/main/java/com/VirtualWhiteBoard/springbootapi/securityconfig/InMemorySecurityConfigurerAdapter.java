package com.VirtualWhiteBoard.springbootapi.securityconfig;

import com.VirtualWhiteBoard.springbootapi.util.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
@EnableWebSecurity
public class InMemorySecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(getInitialDetailsManager());
    }

    @Override
    public void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic().and().csrf().disable()
                .authorizeRequests()
                //.antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/login").permitAll()
                .anyRequest().authenticated();
    }

    @Bean
    public InMemoryUserDetailsManager getInitialDetailsManager() {
        InMemoryUserDetailsManager detailsManager = new InMemoryUserDetailsManager();
        detailsManager.createUser(User.withUsername("sam"+Constants.VIRTUAL_BOARD_EMAIL).password("{noop}adminpass")
                .roles(Constants.USER_ROLE, Constants.MODERATOR_ROLE).build());
        detailsManager.createUser(User.withUsername("team"+Constants.VIRTUAL_BOARD_EMAIL).password("{noop}teampass")
                .roles(Constants.USER_ROLE).build());
        return detailsManager;
    }
}
