package com.angel.lda.repository.tdb;

import com.angel.lda.model.User;
import com.angel.lda.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Angel on 2/8/2018.
 */
@Repository
public class UserTdbRepository implements UserRepository {
    @Override
    public User findByEmail(String email) {
        return null;
    }

    @Override
    public List<User> getDoctors() {
        return null;
    }

    @Override
    public User save(User user) {
        return null;
    }

    @Override
    public void delete(User userToBeDeleted) {

    }
}
