package com.example.xinran.domain;

/**
 * Created by kaonglu
 * 2020/8/3
 */
public class Arcserve {

    private String name;

    private int age;

    private String phone;

    private int sale;

    private String addr;

    private String dream;

    public Arcserve(String name, int age, String phone, int sale, String addr, String dream) {
        this.name = name;
        this.age = age;
        this.phone = phone;
        this.sale = sale;
        this.addr = addr;
        this.dream = dream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getDream() {
        return dream;
    }

    public void setDream(String dream) {
        this.dream = dream;
    }
}
