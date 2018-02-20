package com.angel.lda.service.impl;

import com.angel.lda.accesscontrolmethods.AccessControl;
import com.angel.lda.exceptions.ResourceNotAllowed;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class UserServiceImpl implements UserService {

  private AuthenticationService authenticationService;
  private UserRepository userRepository;
  private AccessControl accessControl;
  private SensorSyncApplicationRepository sensorSyncApplicationRepository;

  @Autowired
  public UserServiceImpl(@Qualifier("userTdbRepository") UserRepository userRepository, AuthenticationService authenticationService, @Qualifier("sensorSyncApplicationJpaRepository") SensorSyncApplicationRepository sensorSyncApplicationRepository, AccessControl accessControl) {
    this.userRepository = userRepository;
    this.authenticationService = authenticationService;
    this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
    this.accessControl = accessControl;
  }

  @Override
  public User createUser(User user) {
    user.setActive(1);
    user.setDoctor(0);
    User returnUser = User.copy(userRepository.save(user));
    returnUser.setPassword(null);
    return returnUser;
  }


  //    Преку метод во AccessControl спречувам можност корисникот да може да промени нешто друго освен име, број и број за итни повици
  @Override
  public User updateUser(User sourceUser) {
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
  public void setSensorApplication(int sensorApplicationId) {
    User saveUser = User.copy(authenticationService.getAuthenticatedUser());
    SensorSyncApplication sensorSyncApplication = sensorSyncApplicationRepository.findOne(sensorApplicationId);
    if (sensorSyncApplication != null)
      saveUser.setUsesSensorSyncApplication(sensorSyncApplication);
    userRepository.save(saveUser);
  }

  @Override
  public List<User> getDoctors() {
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
  public User cleanUser(User user) {
    User clone = User.copy(user);
    clone.setPassword(null);
    clone.setPhoneNumber(null);
    clone.setEmergencyPhone(null);
    return clone;
  }
}
