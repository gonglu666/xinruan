package com.example.xinran.controller;

/**
 * Created by kaonglu
 * 2020/8/5
 */

import com.example.xinran.config.AsyncConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ExcutorTest {

    @Autowired
    private AsyncConfig asyncConfig;

    @RequestMapping("/test/executor")
    public void testExcutor(){

        for (int i = 0;i<20;i++){
            int finalI = i;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread()+"------"+ finalI +"-------");
                }
            };
            asyncConfig.submit(runnable);
        }






    }

}
