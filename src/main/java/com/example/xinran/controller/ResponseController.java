package com.example.xinran.controller;

import com.example.xinran.common.GlResponse;
import com.example.xinran.dto.XinranDTO;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Created by kaonglu
 * 2020/8/25
 */
@RestController
public class ResponseController {


    @RequestMapping("/xinran/response")
    public GlResponse getResponse(@Valid @RequestBody XinranDTO xinranDTO){
        System.out.println(xinranDTO.toString());
        return GlResponse.getResponse("200","success","kan xin ran xi zao!");
    }
}
