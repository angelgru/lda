package com.angel.lda.service.impl;

import com.angel.lda.model.User;
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

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {
        user.setActive(1);
        user.setDoctor(0);
        return userRepository.save(user);
    }

    @Override
    public User updateUser(User user, String email) {
        User userToBeSaved = userRepository.findByEmail(email);
        userToBeSaved.setActive(user.getActive());
        userToBeSaved.setEmergencyPhone(user.getEmergencyPhone());
        userToBeSaved.setName(user.getName());
        userToBeSaved.setPassword(user.getPassword());
        userToBeSaved.setPhoneNumber(user.getPhoneNumber());
        return userRepository.save(userToBeSaved);
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
}
