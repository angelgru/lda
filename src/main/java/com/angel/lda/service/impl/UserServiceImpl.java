package com.angel.lda.service.impl;

import com.angel.lda.AccessControlMethods.AccessControl;
import com.angel.lda.model.SensorSyncApplication;
import com.angel.lda.model.User;
import com.angel.lda.repository.SensorSyncApplicationRepository;
import com.angel.lda.repository.UserRepository;
import com.angel.lda.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Angel on 1/1/2018.
 */

@Service
public class UserServiceImpl implements UserService{

    private UserRepository userRepository;
    private AccessControl accessControl;
    private SensorSyncApplicationRepository sensorSyncApplicationRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, SensorSyncApplicationRepository sensorSyncApplicationRepository, AccessControl accessControl) {
        this.userRepository = userRepository;
        this.sensorSyncApplicationRepository = sensorSyncApplicationRepository;
        this.accessControl = accessControl;
    }

    @Override
    public User createUser(User user) {
        user.setActive(1);
        user.setDoctor(0);
        return userRepository.save(user);
    }


//    Преку метод во AccessControl спречувам можност корисникот да може да промени нешто друго освен име, број и број за итни повици
    @Override
    public User updateUser(User user, String email) {
        User userToBeSaved = userRepository.findByEmail(email);
        User tmpUser = accessControl.U2(user, userToBeSaved);
        tmpUser = accessControl.A2(tmpUser);
        return userRepository.save(tmpUser);
    }

    @Override
    public void deleteUser(String email) {
        User userToBeDeleted = userRepository.findByEmail(email);
        userRepository.delete(userToBeDeleted);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void setSensorApplication(int sensorApplicationId, String email) {
        User userToBeSaved = userRepository.findByEmail(email);
        SensorSyncApplication sensorSyncApplication = sensorSyncApplicationRepository.findOne(sensorApplicationId);
        if(sensorSyncApplication != null)
            userToBeSaved.setUsesSensorSyncApplication(sensorSyncApplication);
        userRepository.save(userToBeSaved);
    }

    @Override
    public List<User> getDoctors() {
        return accessControl.P1(userRepository.getDoctors());
    }
}
