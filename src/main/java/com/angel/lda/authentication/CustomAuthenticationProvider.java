package com.angel.lda.authentication;

import com.angel.lda.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angel on 1/11/2018.
 */

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    public CustomAuthenticationProvider(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(authentication.getName());

        if(!userDetails.getPassword().equals(authentication.getCredentials())) {
            throw new BadCredentialsException("Incorrect credentials!");
        } else{
            System.out.println("USER AUTHENTICATED");
        }

        // Параметрите кои ги враќаме преку UsernamePasswordAuthenticationToken се оние кои ги добиваме со Principal.getName
        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), getAuthorities(userDetails.getUser()));
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(UsernamePasswordAuthenticationToken.class);
    }

    private List<GrantedAuthority> getAuthorities(User aUser) {
        return new ArrayList<>();
    }
}
