package com.angel.lda.repository.jpa;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Angel on 2/8/2018.
 */

@Repository
public interface UserJpaRepository extends JpaRepository<User, Integer>, UserRepository{
}
