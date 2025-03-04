package com.springlabs.controller;

import com.springlabs.model.User;
import com.springlabs.model.Info;
import com.springlabs.service.UserService;
import com.springlabs.service.InfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private InfoService infoService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        return userService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<User> getUsersByNameAndSurname(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) String surname) {
        return userService.findByNameAndSurname(name, surname);
    }

    @PostMapping("/create")
    public User createUser(@RequestBody User user) {
        return userService.save(user);
    }

    @PutMapping("/update")
    public ResponseEntity<User> updateUser(@RequestBody User userDetails) {
        if (userDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        User updatedUser = userService.update(userDetails);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{userId}/info/{infoId}")
    public ResponseEntity<Void> addInfoToUser(@PathVariable Integer userId, @PathVariable Integer infoId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new RuntimeException("Info not found"));

        user.getInfo().add(info);
        info.getUsers().add(user);

        userService.save(user);
        infoService.save(info);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}/info/{infoId}")
    public ResponseEntity<Void> removeInfoFromUser(@PathVariable Integer userId, @PathVariable Integer infoId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Info info = infoService.findById(infoId)
                .orElseThrow(() -> new RuntimeException("Info not found"));

        user.getInfo().remove(info);
        info.getUsers().remove(user);

        userService.save(user);
        infoService.save(info);
        return ResponseEntity.noContent().build();
    }
}