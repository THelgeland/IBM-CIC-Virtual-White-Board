package com.VirtualWhiteBoard.springbootapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class UserController {

    private InMemoryUserDetailsManager userDetailsManager;

    @Autowired
    public UserController(InMemoryUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

    @RequestMapping("/login")
    public Boolean login(@RequestBody User user) {
        return userDetailsManager.userExists(user.getUsername()) &&
                userDetailsManager.loadUserByUsername(user.getUsername()).getPassword().equals(user.getPassword());
    }

    @GetMapping("/users/{name}")
    public UserDetails singular(@PathVariable String name) {
        return userDetailsManager.loadUserByUsername(name);
    }
}
