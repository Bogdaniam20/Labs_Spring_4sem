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
@RequestMapping("/info")
public class InfoController {

    @Autowired
    private InfoService infoService;

    @Autowired
    private UserService userService;

    @PostMapping("/create/{userId}")
    public Info createInfo(@PathVariable Integer userId, @RequestBody Info info) {
        // Получаем пользователя и убеждаемся, что он существует
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        info.setUsers(List.of(user));
        user.getInfo().add(info);

        Info savedInfo = infoService.save(info);

        userService.save(user);

        return savedInfo;
    }

    @GetMapping("/getAll")
    public List<Info> getAllInfo() {
        List<Info> infoList = infoService.findAll();
        if (infoList.isEmpty()) {
            throw new InfoNotFoundException("Нет доступной информации");
        }
        return infoList;
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Info> getInfoById(@PathVariable Integer id) {
        return infoService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new InfoNotFoundException("Информация не найдена"));
    }

    @PutMapping("/update")
    public ResponseEntity<Info> updateInfo(@RequestBody Info infoDetails) {
        if (infoDetails.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        Info infoToUpdate = infoService.findById(infoDetails.getId())
                .orElseThrow(() -> new InfoNotFoundException("Информация не найдена для обновления"));
        Info updatedInfo = infoService.update(infoDetails);
        return ResponseEntity.ok(updatedInfo);
    }

    @DeleteMapping("/deleteById/{id}")
    public ResponseEntity<Void> deleteInfo(@PathVariable Integer id) {
        Info info = infoService.findById(id)
                .orElseThrow(() -> new InfoNotFoundException("Информация не найдена для удаления"));
        infoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{userId}")
    public List<Info> getInfoByUserId(@PathVariable Integer userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
        List<Info> infoList = user.getInfo();
        if (infoList.isEmpty()) {
            throw new InfoNotFoundException("Нет информации для данного пользователя");
        }
        return infoList;
    }
}