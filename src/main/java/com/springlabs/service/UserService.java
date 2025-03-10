package com.springlabs.service;

import com.springlabs.exceptions.UserNotFoundException;
import com.springlabs.repository.dao.UserDao;
import com.springlabs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User save(User user) {

        if (user.getName() == null || user.getSurname() == null) {
            throw new IllegalArgumentException("Name and surname must not be null");
        }
        return userDao.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userDao.findById(id);
    }

    public User update(User userDetails) {

        User user = userDao.findById(userDetails.getId())
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userDetails.getId()));


        if (userDetails.getName() == null || userDetails.getSurname() == null) {
            throw new IllegalArgumentException("Name and surname must not be null");
        }

        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        return userDao.save(user);
    }

    public void delete(Integer id) {

        User user = userDao.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
        userDao.delete(user.getId());
    }

    public List<User> findByNameAndSurname(String name, String surname) {
        if (name != null && surname != null) {
            return userDao.findByNameAndSurname(name, surname);
        } else if (name != null) {
            return userDao.findByName(name);
        } else if (surname != null) {
            return userDao.findBySurname(surname);
        } else {
            return userDao.findAll();
        }
    }
}