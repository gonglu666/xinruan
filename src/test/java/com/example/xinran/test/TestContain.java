package com.example.xinran.test;

import lombok.SneakyThrows;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by gonglu
 * 2021/1/23
 */
public class TestContain extends Thread{

    private  final static AtomicInteger count = new AtomicInteger(0);
    private final static ConcurrentHashMap<Long, String> cacheMap = new ConcurrentHashMap<>();
    private static Object lock = new Object();

    private  String getValue(Long lockName) throws InterruptedException {
        if (!cacheMap.containsKey(lock)) {
//            synchronized (lock) {
//                if (!cacheMap.containsKey(lockName)) {
//                    count.incrementAndGet();
//                    cacheMap.put(lockName, getValueFromIO(lockName));
//                }
//            }
            cacheMap.computeIfAbsent(lockName,this::getValueFromIO);
            cacheMap.putIfAbsent(lockName,getValueFromIO(lockName));
        }
        return cacheMap.get(lockName);
    }

    public  String getValueFromIO(Long key) {
        count.incrementAndGet();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

        }
        return "get value from io ...";
    }

    @SneakyThrows
    @Override
    public void run() {
        int index = (int)((Math.random())*10+1234455667);
//        int index = (int)((Math.random())*10);
        Long keyName = (long)index;
        getValue(keyName);

    };

    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        List<Thread> list = new ArrayList<>();
        for (int i = 0; i < 200; i++) {
            list.add(new TestContain());
        }
        list.forEach(t ->{t.start();});
        list.forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println("... end ..."+count.get()+": spend:"+(end-start));
    }
}
