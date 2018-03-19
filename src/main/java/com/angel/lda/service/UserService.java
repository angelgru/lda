package com.angel.lda.service;

import com.angel.lda.model.User;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */
public interface UserService {

    User createUser(User user) throws IOException;

    User updateUser(User user) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException;

    List<User> getDoctors() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;

    User getUserByEmail(String emailT) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException;

}
