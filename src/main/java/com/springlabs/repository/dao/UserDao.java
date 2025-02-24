package com.springlabs.dao;

import com.springlabs.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    void delete(Integer id);
}