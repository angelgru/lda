package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class UserServiceImpl implements UserService {

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private AccessControl accessControl;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService) {
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.accessControl = accessControl;
  }

  @Override
  public List<User> getDoctors() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
    List<User> doctors = userRepository.getDoctors();
    return accessControl.P1(doctors);
  }

  @Override
  public User getUserByEmail(String email) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
    return userRepository.findByEmail(email);
  }

  @Override
  public User createUser(User user) throws IOException {
    return userRepository.save(user);
  }

  @Override
  public User updateUser(User sourceUser) throws IllegalAccessException, InvocationTargetException, InstantiationException, IOException {
    User user = authenticationService.getAuthenticatedUser();

    sourceUser.setEmail(user.getEmail());
    return userRepository.save(user);
  }
}
