package com.springlabs.repository.dao;

import com.springlabs.cache.UserCache;
import com.springlabs.model.User;
import com.springlabs.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImpl implements UserDao {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCache userCache;

    @Override
    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        users.forEach(userCache::putUser);
        return users;
    }

    @Override
    public List<User> saveAll(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Список пользователей не должен быть пустым");
        }
        List<User> savedUsers = userRepository.saveAll(users);
        savedUsers.forEach(userCache::putUser);

        return savedUsers;
    }


    @Override
    public Optional<User> findById(Integer id) {
        User cachedUser = userCache.getUser(id);
        if (cachedUser != null) {
            return Optional.of(cachedUser);
        }
        Optional<User> user = userRepository.findById(id);
        user.ifPresent(userCache::putUser);
        return user;
    }

    @Override
    public User save(User user) {
        User savedUser = userRepository.save(user);
        userCache.putUser(savedUser);
        return savedUser;
    }

    @Override
    public void delete(Integer id) {
        userRepository.deleteById(id);
        userCache.removeUser(id);
    }

    @Override
    public List<User> findByNameAndSurname(String name, String surname) {
        return userRepository.findByNameAndSurname(name, surname);
    }

    @Override
    public List<User> findByName(String name) {
        User cachedUser = userCache.getUserByName(name);
        if (cachedUser != null) {
            return List.of(cachedUser);
        }
        List<User> users = userRepository.findByName(name);
        users.forEach(userCache::putUser);
        return users;
    }

    @Override
    public List<User> findBySurname(String surname) {
        User cachedUser = userCache.getUserBySurname(surname);
        if (cachedUser != null) {
            return List.of(cachedUser);
        }
        List<User> users = userRepository.findBySurname(surname);
        users.forEach(userCache::putUser);
        return users;
    }
}