package com.example.xinran.SubTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gonglu
 * 2020/12/2
 */
public class Position extends SubPosition{

    private static Map son = new HashMap();

    static {
        son.put("son","456");
    }


    public static void main(String[] args) {
        List<Map> list = new ArrayList();
        List<Map> remove = new ArrayList();
        Map map1 = new HashMap();
        map1.put("aa",1);
        Map map2 = new HashMap();
        map2.put("aa",2);
        list.add(map1);
        list.add(map2);

        list.forEach(m -> {
            if ((int)m.get("aa") == 2){
                remove.add(m);
            }
        });

        list.removeAll(remove);

        System.out.println(list.size());




    }


}
