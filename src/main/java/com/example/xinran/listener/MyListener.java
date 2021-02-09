package com.example.xinran.listener;

import java.util.EventObject;

/**
 * Created by gonglu
 * 2020/12/25
 */
public class MyListener {

    public void listen(MyEvent myEvent) {
        myEvent.fire();
    }
}
