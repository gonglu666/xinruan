package com.example.xinran.test;


import org.springframework.util.StopWatch;

/**
 * Created by gonglu
 * 2020/12/4
 */
public class StopWatchTeset {


    public static void main(String[] args) throws InterruptedException {
        StopWatch stopWatch = new StopWatch("55");
        stopWatch.start("睡1秒");
        System.out.println("1");
        Thread.sleep(1000);
        stopWatch.stop();

        stopWatch.start("睡2秒");
        System.out.println("2");
        Thread.sleep(2000);
        stopWatch.stop();
        stopWatch.start("睡3秒");
        System.out.println("3");
        Thread.sleep(3000);
        stopWatch.stop();
        stopWatch.start("睡4秒");
        System.out.println("4");
        Thread.sleep(4000);
        stopWatch.stop();
//
//        System.out.println(stopWatch.toString());
        System.out.println(stopWatch.prettyPrint());



    }
}
