package com.angel.lda.controller;

import com.angel.lda.model.User;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) throws IOException {
        return userService.createUser(user);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/doctors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
        return userService.getDoctors();
    }
}
