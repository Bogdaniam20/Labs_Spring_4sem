package com.springlabs.repository.dao;

import com.springlabs.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> findAll();
    Optional<User> findById(Integer id);
    User save(User user);
    void delete(Integer id);
    List<User> saveAll(List<User> users);

    List<User> findByNameAndSurname(String name, String surname);
    List<User> findByName(String name);
    List<User> findBySurname(String surname);
}