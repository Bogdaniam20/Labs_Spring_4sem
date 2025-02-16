package com.springlabs.controller;

import com.springlabs.model.Result;
import com.springlabs.model.TextRequest;
import com.springlabs.service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextController {

    @Autowired
    private TextService textService;

    @PostMapping("/parse")
    public Result parseText(@RequestBody TextRequest request) {
        return textService.parseText(request.getText());
    }
}