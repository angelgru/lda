package com.angel.lda.controller;

import com.angel.lda.model.User;
import com.angel.lda.repository.tdb.UserTdbRepository;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@RequestMapping(value = "/api/user")
@RestController
public class UserController {

    private UserService userService;
    private UserTdbRepository userTdbRepository;

    @Autowired
    public UserController(UserService userService, UserTdbRepository userTdbRepository) {
        this.userService = userService;
        this.userTdbRepository = userTdbRepository;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "/setSensorApplication/{id}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void setSensorApplication(@PathVariable("id") int id){
        userService.setSensorApplication(id);
    }

    @RequestMapping(value = "/update", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    public User updateUser(@RequestBody User user) {
        return userService.updateUser(user);
    }

    @RequestMapping(value = "/doctors", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<User> getDoctors() {
        return userService.getDoctors();
    }

//    @RequestMapping(value = "/{email}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
//    public User getUser(@PathVariable("email") String email) {
//        return userTdbRepository.findByEmail(email);
//    }
}
