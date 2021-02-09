package com.example.xinran.test;


import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gonglu
 * 2020/8/27
 */
public class RegexpTest {

    @Test
    public void test(){
//        String pattern = "^[1-9][0-9]{3}-([0][1-9]|[1][0-2])-([0-2][0-9]|[3][0-1])\\s([0-1][0-9]|[2][0-4]):[0-5][0-9]:[0-5][0-9]$";
//        String temp = "2019-12-23 12:34:48";
//        String temp1 = "c";
//        System.out.println(temp.matches(pattern));

        Map map = new HashMap<>();
        map.put("123","345");
        String format = String.format("123%s", map.toString());
        System.out.println(format);


        Long aa = 12314526537L;
        Long bb = 12314526537L;
        System.out.println(aa.equals(bb));

    }
}
