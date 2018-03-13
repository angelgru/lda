package com.angel.lda.repository.jpa;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.springframework.context.annotation.Profile;
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
@Profile("jpa")
public interface UserJpaRepository extends JpaRepository<User, Integer>, UserRepository{
}
