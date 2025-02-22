package com.springlabs.controller;

import com.springlabs.model.User;
import com.springlabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(user -> ResponseEntity.ok().body(user))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
        if (userDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User updatedUser = userService.update(userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestBody User user) {
        userService.delete(user.getId());
        return ResponseEntity.noContent().build();
    }
}