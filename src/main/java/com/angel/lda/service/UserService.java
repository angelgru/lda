package com.angel.lda.service;

import com.angel.lda.model.User;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface UserService {

    public User createUser(User user);

    public User updateUser(User user);

    public void setSensorApplication(int sensorApplicationId);

    public List<User> getDoctors();

    public User cleanUser(User user);
}
