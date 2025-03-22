package com.springlabs.service;

import com.springlabs.exceptions.UserNotFoundException;
import com.springlabs.repository.dao.UserDao;
import com.springlabs.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RequestCounter requestCounter;

    public List<User> findAll() {
        requestCounter.increment();
        try {
            log.info("Попытка получить всех пользователей");
            log.info("Количество вызовов этого метода: " + requestCounter.getCount());
            return userDao.findAll();
        } catch (Exception e) {
            log.error("Ошибка при получении пользователей: {}", e.getMessage(), e);
            throw new RuntimeException("Ошибка при получении пользователей", e);
        }
    }

    public User save(User user) {
        requestCounter.increment();
        if (user.getName() == null || user.getSurname() == null) {
            throw new IllegalArgumentException("Имя и фамилия пользователя не должны быть пустыми");
        }
        log.info("Пользователь сохранен: {}", user.getName());
        return userDao.save(user);
    }

    public Optional<User> findById(Integer id) {
        requestCounter.increment();
        log.info("Попытка найти пользователя по ID: {}", id);
        return userDao.findById(id);
    }

    public User update(User userDetails) {
        requestCounter.increment();
        log.info("Попытка обновить пользователя с ID: {}", userDetails.getId());
        return userDao.findById(userDetails.getId())
                .map(existingUser -> {
                    if (userDetails.getName() == null || userDetails.getSurname() == null) {
                        throw new IllegalArgumentException("Имя и фамилия пользователя не должны быть пустыми");
                    }
                    existingUser.setName(userDetails.getName());
                    existingUser.setSurname(userDetails.getSurname());
                    return userDao.save(existingUser);
                })
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден с ID: " + userDetails.getId()));
    }

    public List<User> saveAll(List<User> users) {
        requestCounter.increment();
        if (users == null || users.isEmpty()) {
            throw new IllegalArgumentException("Список пользователей не должен быть пустым");
        }

        users.stream()
                .filter(user -> user.getName() == null || user.getSurname() == null)
                .findAny()
                .ifPresent(user -> {
                    throw new IllegalArgumentException("Имя и фамилия пользователя не должны быть пустыми");
                });

        log.info("Попытка сохранить список пользователей");
        return userDao.saveAll(users);
    }

    public void delete(Integer id) {
        requestCounter.increment();
        log.info("Попытка удалить пользователя с ID: {}", id);
        userDao.findById(id)
                .ifPresentOrElse(
                        user -> userDao.delete(user.getId()),
                        () -> {
                            throw new UserNotFoundException("Пользователь не найден с ID: " + id);
                        }
                );
    }

    public List<User> findByNameAndSurname(String name, String surname) {
        requestCounter.increment();
        log.info("Поиск пользователей по имени: {} и фамилии: {}", name, surname);
        return userDao.findAll().stream()
                .filter(user -> (name == null || user.getName().equals(name)))
                .filter(user -> (surname == null || user.getSurname().equals(surname)))
                .collect(Collectors.toList());
    }
}