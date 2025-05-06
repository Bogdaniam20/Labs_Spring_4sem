package com.springlabs.controller;

import com.springlabs.exceptions.InfoNotFoundException;
import com.springlabs.exceptions.UserNotFoundException;
import com.springlabs.model.Info;
import com.springlabs.model.User;
import com.springlabs.service.InfoService;
import com.springlabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", methods = {RequestMethod.DELETE, RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @GetMapping("/getAll")
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @GetMapping("/search")
    public List<User> getUsersByNameAndSurname(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) String surname) {
        List<User> users = userService.findByNameAndSurname(name, surname);
        if (users.isEmpty()) {
            throw new UserNotFoundException("Пользователи не найдены");
        }
        return users;
    }

    @PostMapping("/bulk")
    public ResponseEntity<List<User>> createUsers(@RequestBody List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new RuntimeException("Список пользователей не должен быть пустым");
        }

        for (User user : users) {
            if (user.getName() == null || user.getSurname() == null) {
                throw new RuntimeException("Имя и фамилия пользователя не должны быть пустыми");
            }
            if (user.getInfo() != null) {
                for (Info info : user.getInfo()) {
                    infoService.save(info);
                }
            }
        }

        List<User> savedUsers = userService.saveAll(users);
        return ResponseEntity.ok(savedUsers);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        if (user == null || user.getName() == null || user.getSurname() == null) {
            throw new RuntimeException("Имя и фамилия пользователя не должны быть пустыми");
        }
        if (user.getInfo() != null) {
            for (Info info : user.getInfo()) {
                infoService.save(info);
            }
        }
        return userService.save(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
        if (userDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User updatedUser = userService.findById(userDetails.getId())
                .map(existingUser -> userService.update(userDetails))
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/info/{infoId}")
    public ResponseEntity<Void> addInfoToUser(@PathVariable Integer userId, @PathVariable Integer infoId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new InfoNotFoundException("Информация не найдена"));

        user.getInfo().add(info);
        info.getUsers().add(user);

        userService.save(user);
        infoService.save(info);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/info/{infoId}")
    public ResponseEntity<Void> removeInfoFromUser(@PathVariable Integer userId,
                                                   @PathVariable Integer infoId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new InfoNotFoundException("Информация не найдена"));

        user.getInfo().remove(info);
        info.getUsers().remove(user);

        userService.save(user);
        infoService.save(info);
        return ResponseEntity.noContent().build();
    }
}