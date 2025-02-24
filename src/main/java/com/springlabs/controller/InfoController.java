package com.springlabs.controller;

import com.springlabs.model.Info;
import com.springlabs.model.User;
import com.springlabs.service.InfoService;
import com.springlabs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @Autowired
    private UserService userService;

    @PostMapping("/create/{userId}")
    public Info createInfo(@PathVariable Integer userId, @RequestBody String request) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return infoService.GetText(request, user);
    }

    @GetMapping("/getAll")
    public List<Info> getAllInfo() {
        return infoService.findAll();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Info> getInfoById(@PathVariable Integer id) {
        return infoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update")
    public ResponseEntity<Info> updateInfo(@RequestBody Info infoDetails) {
        if (infoDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Info updatedInfo = infoService.update(infoDetails);
        return ResponseEntity.ok(updatedInfo);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteInfo(@PathVariable Integer id) {
        infoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Info> getInfoByUserId(@PathVariable Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getInfo();
    }
}