package com.angel.lda.authentication;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by Angel on 12/28/2017.
 */

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = null;
        try {
            user = userRepository.findByEmail(username);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            e.printStackTrace();
        }

        if(user == null) {
            throw new UsernameNotFoundException("Username not found!");
        }

        return new CustomUserDetails(user);
    }
}
