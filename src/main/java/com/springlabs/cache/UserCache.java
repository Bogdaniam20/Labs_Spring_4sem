package com.springlabs.cache;

import com.springlabs.model.User;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserCache {

    private final Map<Integer, User> userMap = new HashMap<>();
    private final Map<String, User> userByNameMap = new HashMap<>();
    private final Map<String, User> userBySurnameMap = new HashMap<>();

    public User getUser(Integer id) {
        return userMap.get(id);
    }

    public User getUserByName(String name) {
        return userByNameMap.get(name);
    }

    public User getUserBySurname(String surname) {
        return userBySurnameMap.get(surname);
    }

    public void putUser(User user) {
        userMap.put(user.getId(), user);
        userByNameMap.put(user.getName(), user);
        userBySurnameMap.put(user.getSurname(), user);
    }

    public void removeUser(Integer id) {
        User user = userMap.remove(id);
        if (user != null) {
            userByNameMap.remove(user.getName());
            userBySurnameMap.remove(user.getSurname());
        }
    }

    public Map<Integer, User> getAllUsers() {
        return new HashMap<>(userMap);
    }

    public Collection<User> getAllUsersCollection() {
        return userMap.values();
    }

    public boolean containsUser(Integer id) {
        return userMap.containsKey(id);
    }

    public boolean containsUserByName(String name) {
        return userByNameMap.containsKey(name);
    }

    public boolean containsUserBySurname(String surname) {
        return userBySurnameMap.containsKey(surname);
    }

    public void clearCache() {
        userMap.clear();
        userByNameMap.clear();
        userBySurnameMap.clear();
    }

    public void updateUser(User user) {
        if (userMap.containsKey(user.getId())) {
            userMap.put(user.getId(), user);
            userByNameMap.put(user.getName(), user);
            userBySurnameMap.put(user.getSurname(), user);
        }
    }
}