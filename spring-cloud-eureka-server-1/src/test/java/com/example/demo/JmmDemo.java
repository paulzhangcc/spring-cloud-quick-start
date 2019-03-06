package com.example.demo;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author paul
 * @description
 * @date 2019/3/5
 */
public class JmmDemo {
    static boolean stop;//@1
    //static volatile boolean stop;//@2
    static int num;

    static Runnable test = new Runnable() {
        @Override
        public void run() {
            while (!stop) {
                num++;
            }
            System.out.println(Thread.currentThread().getName() + ":" + num);
        }
    };

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(test);
        thread.setName("JMM-TEST");
        thread.start();
        Thread.sleep(100);//让线程JMM-TEST的工作内存取到stop为false
        stop = true;
        Thread.sleep(1000);
        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName() + ":" + num);
                System.out.println(Thread.currentThread().getName() + ":" + stop);
            }
        }, 1, 2, TimeUnit.SECONDS);
    }
}
