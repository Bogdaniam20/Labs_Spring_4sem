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

    @PostMapping("/{userId}")
    public Info createInfo( @PathVariable Integer userId,@RequestBody String request) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return infoService.GetText(request, user);
    }

    @GetMapping
    public List<Info> getAllInfo() {
        return infoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Info> getInfoById(@PathVariable Integer id) {
        return infoService.findById(id)
                .map(info -> ResponseEntity.ok().body(info))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping
    public ResponseEntity<Info> updateInfo(@RequestBody Info infoDetails) {
        if (infoDetails.getId() == null) {
            return ResponseEntity.badRequest().build(); // Возвращаем ошибку, если ID не указан
        }
        Info updatedInfo = infoService.update(infoDetails);
        return ResponseEntity.ok(updatedInfo);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfo(@PathVariable Integer id) {
        infoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}