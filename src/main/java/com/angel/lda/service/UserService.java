package com.angel.lda.service;

import com.angel.lda.model.User;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface UserService {

    public User createUser(User user);

    public User updateUser(User user) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public void setSensorApplication(int sensorApplicationId) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    public List<User> getDoctors() throws IllegalAccessException, InstantiationException, InvocationTargetException;

    public User getUserByEmail(String emailT) throws IllegalAccessException, InstantiationException, InvocationTargetException;

    public User cleanUser(User user);
}
