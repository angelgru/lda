package com.angel.lda.repository.jpa;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer>, UserRepository{
    @Query("select u from User as u where u.email = :email")
    User findByEmail(@Param("email") String email) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    @Query("select u from User as u where u.doctor = 1")
    List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException;
}
