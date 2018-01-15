package com.angel.lda.controller;

import com.angel.lda.model.User;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping(value = "/api/user")
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/userInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public User findUserByEmail(Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return user;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) {
        System.out.println("CREATE");
        return userService.createUser(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user, Principal principal) {
        return userService.updateUser(user, principal.getName());
    }

    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public void deleteUser(Principal principal) {
        userService.deleteUser(principal.getName());
    }
}
