package com.angel.lda.service.impl;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

/**
 * @author Riste Stojanov
 */
@Service
public class AuthenticationService {

  private final UserRepository repository;

  @Autowired
  public AuthenticationService(UserRepository repository) {
    this.repository = repository;
  }

  public User getAuthenticatedUser() throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException {
    Optional<String> loginOptional = SecurityUtils.getCurrentUserLogin();

    return this.repository.findByEmail(loginOptional
      .orElseThrow(IllegalStateException::new)
    );

  }
}
