package com.VirtualWhiteBoard.springbootapi.controller;

import com.VirtualWhiteBoard.springbootapi.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST API Controller for accessing and creating Users, access to these resources is
 * controlled by the InMemorySecurityConfigurerAdapter
 */

@RestController
public class UserController {

    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    public UserController(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @RequestMapping("/register/{username}/{password}")
    void registerUser(@PathVariable String username, @PathVariable String password) {
        if (!userDetailsManager.userExists(username)) {
            userDetailsManager.createUser(User.withUsername(username).password(password)
                    .roles(Constants.USER_ROLE).build());
        }
    }

    @GetMapping("/users/{name}")
    public UserDetails singular(@PathVariable String name) {
        return userDetailsManager.loadUserByUsername(name);
    }
}
