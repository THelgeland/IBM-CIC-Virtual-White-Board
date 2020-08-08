package com.VirtualWhiteBoard.springbootapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    public UserController(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }


}
