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

    User findByEmail(@Param("email") String email) throws IllegalAccessException, InvocationTargetException, InstantiationException;

    List<User> getDoctors() throws IllegalAccessException, InvocationTargetException, InstantiationException;

    User save(User user);

    void delete(User userToBeDeleted);
}
