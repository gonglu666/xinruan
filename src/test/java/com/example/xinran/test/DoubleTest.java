package com.example.xinran.test;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by kaonglu
 * 2020/8/10
 */
public class DoubleTest {


    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE,-1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND, 0);
        Date minDate = calendar.getTime();
        System.out.println(sf.format(minDate));
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND, 59);
        Date maxDate = calendar.getTime();
        System.out.println(sf.format(maxDate));


        Long a = 123L;
        Long b = 123L;
        Map map = new HashMap();
        map.put(a,"aa");
        if (map.containsKey(b)){
            System.out.println(map.get(b));
        }






    }
}
