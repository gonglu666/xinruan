package com.example.xinran.demo;

/**
 * Created by kaonglu
 * 2020/8/17
 */
public class OverTest {

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    //重写equals后必须重写hashCode，如果根据属性判断两对象相等，但是hashCode的默认方法中两对象不相等
    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }



}
