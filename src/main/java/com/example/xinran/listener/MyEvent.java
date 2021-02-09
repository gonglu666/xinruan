package com.example.xinran.listener;

import java.util.EventObject;

/**
 * Created by gonglu
 * 2020/12/25
 */
public class MyEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source The object on which the Event initially occurred.
     * @throws IllegalArgumentException if source is null.
     */
    public MyEvent(Object source) {
        super(source);
    }

    public void fire() {
        Object source = super.getSource();
        if (source instanceof MySource) {
            System.out.println(((MySource) source).getName());
        }
    }




}
