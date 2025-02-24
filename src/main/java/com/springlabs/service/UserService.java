package com.springlabs.service;

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
        return userDao.save(user);
    }

    public Optional<User> findById(Integer id) {
        return userDao.findById(id);
    }

    public User update(User userDetails) {
        User user = userDao.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setName(userDetails.getName());
        user.setSurname(userDetails.getSurname());
        return userDao.save(user);
    }

    public void delete(Integer id) {
        userDao.delete(id);
    }
}