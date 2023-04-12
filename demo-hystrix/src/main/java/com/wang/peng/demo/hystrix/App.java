package com.wang.peng.demo.hystrix;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

/**
 * Hello world!
 */
public class App {

    public FutureTask<String> get(){
        FutureTask<String> futureTask = new FutureTask<>(() -> "result");
        return futureTask;
    }
    public static void main(String[] args) {
        System.out.println("Hello World!");
    }
}
