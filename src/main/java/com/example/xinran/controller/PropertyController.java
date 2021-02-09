package com.example.xinran.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by gonglu
 * 2020/12/23
 */
@RestController
public class PropertyController {


    @GetMapping("/property/{key}")
    public String getProperty(@PathVariable String key) {
        return null;
    }

}
