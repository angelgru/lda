package com.angel.lda.repository;

import com.angel.lda.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 12/28/2017.
 */

@Repository
public interface UserRepository {

    @Query("select u from User as u where u.email = :email")
    User findByEmail(@Param("email") String email) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    @Query("select u from User as u where u.doctor = 1")
    List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException;

    User save(User user);
}
