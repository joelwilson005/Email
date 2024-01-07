package com.joel.email.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Home {

    @GetMapping("/")
    public ResponseEntity<String> test() {

        return new ResponseEntity<>("Testing", HttpStatus.OK);
    }
}
