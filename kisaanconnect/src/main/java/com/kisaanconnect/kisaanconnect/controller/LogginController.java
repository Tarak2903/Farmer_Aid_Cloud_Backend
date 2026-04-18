package com.kisaanconnect.kisaanconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LogginController {

    @GetMapping("/")
    public String hello(){
        System.out.println("Controller has been hit");
        return "Controller has been hit";}
}
