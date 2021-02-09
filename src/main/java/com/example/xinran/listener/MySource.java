package com.example.xinran.listener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gonglu
 * 2020/12/25
 */
public class MySource {

    private List<MyListener> list = new ArrayList<>();

    public void add(MyListener myListener) {
        this.list.add(myListener);
    }

    private String name;

    public void say() {
        System.out.println("source ...");
        list.forEach(s -> {
            s.listen(new MyEvent(this));
        });
    }

    public MySource(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static void main(String[] args) {
        MySource mySource = new MySource("gonglu");
        mySource.add(new MyListener());
        mySource.say();

    }
}
