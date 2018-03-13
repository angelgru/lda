package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.model.Sensor;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorRepository;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.repository.tdb.SensorTdbRepository;
import com.angel.lda.service.UserService;
import org.apache.jena.base.Sys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class UserServiceImpl implements UserService {

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private AccessControl accessControl;
  private SensorSyncApplicationRepository sensorSyncApplicationRepository;
  private SensorRepository sensorRepository;

  @Autowired
  public UserServiceImpl(UserRepository userRepository, AuthenticationService authenticationService, SensorSyncApplicationRepository sensorSyncApplicationRepository, AccessControl accessControl, SensorRepository sensorRepository) {
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
    this.accessControl = accessControl;
    this.sensorRepository = sensorRepository;
  }

  @Override
  public User createUser(User user) {
    user.setActive(1);
    User returnUser = User.copy(userRepository.save(user));
    returnUser.setPassword(null);
    return returnUser;
  }

  //    Преку метод во AccessControl спречувам можност корисникот да може да промени нешто друго освен име, број и број за итни повици
  @Override
  public User updateUser(User sourceUser) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    User targetUser = User.copy(authenticationService.getAuthenticatedUser());
    if(sourceUser.getEmail() != null) {
      targetUser.setEmail(sourceUser.getEmail());
    }

    if(sourceUser.getEmergencyPhone() != null) {
      targetUser.setEmergencyPhone(sourceUser.getEmergencyPhone());
    }

    if(sourceUser.getPhoneNumber() != null) {
      targetUser.setPhoneNumber(sourceUser.getPhoneNumber());
    }

    if(sourceUser.getName() != null) {
      targetUser.setName(sourceUser.getName());
    }

    if(sourceUser.getPassword() != null) {
      targetUser.setPassword(sourceUser.getPassword());
    }

    if(accessControl.U2(targetUser, authenticationService.getAuthenticatedUser())){
      userRepository.save(targetUser);
      targetUser.setPassword(null);
      return targetUser;
    }

    throw new ResourceNotAllowed("You are only able to edit name, phone number and emergency phone number");
  }

  @Override
  public void setSensorApplication(int sensorApplicationId) throws IllegalAccessException, InvocationTargetException, InstantiationException {
    User saveUser = User.copy(authenticationService.getAuthenticatedUser());
    SensorSyncApplication sensorSyncApplication = sensorSyncApplicationRepository.findOne(sensorApplicationId);
    if (sensorSyncApplication != null) {
      saveUser.setUsesSensorSyncApplication(sensorSyncApplication);
    }
    userRepository.save(saveUser);
  }

  @Override
  public List<User> getDoctors() throws IllegalAccessException, InstantiationException, InvocationTargetException {
    List<User> doctors = new ArrayList<>();
    User doctor;
    for(User doc: userRepository.getDoctors()){
      doctor  = User.copy(doc);
      doctor.setPassword(null);
      doctors.add(doctor);
    }

    return accessControl.P1(doctors);
  }

  @Override
  public User getUserByEmail(String emailT) throws IllegalAccessException, InstantiationException, InvocationTargetException {
    User doctor = null;
    doctor = userRepository.findByEmail(emailT);
    return doctor;
  }

  @Override
  public User cleanUser(User user) {
    User clone = User.copy(user);
    clone.setPassword(null);
    clone.setPhoneNumber(null);
    clone.setEmergencyPhone(null);
    return clone;
  }
}
