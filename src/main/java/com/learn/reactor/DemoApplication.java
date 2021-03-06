package com.learn.reactor;

import com.google.common.base.Stopwatch;
import com.learn.reactor.client.Client;
import com.learn.reactor.server.Server;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 手写Reactor模式（没调用javaNOI库）
 * CLient持有Server的事件队列模拟socket的功能，突出Reactor模式本身，弱化NIO API
 *
 * 可扩展的地方:
 * 1.Dispatcher中的HandlerMap中的handler可以替换成3个线程池，使用异步处理的方式（处理任务10秒一个）
 * 2.Dispatcher中的selector可以根据事件的类型改成3个Accept，read，write
 * 3.使用手写工厂模式或者spring框架,抽离出代码中耦合的selector
 * 4.
 * 5.
 */
@SpringBootApplication
public class DemoApplication {
    private static ExecutorService executorService=Executors.newFixedThreadPool(1);

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(DemoApplication.class, args);
        //// 创建并启动计时器
        //Stopwatch stopwatch = Stopwatch.createStarted();
        //
        //new Thread(client,"client thread").start();
        //
        //Future future = executorService.submit(server);
        //
        //while(true){
        //    if(future.isDone())
        //        break;
        //}
        //
        //stopwatch.stop();
        //// 执行时间（单位：秒）
        //System.out.printf("执行时长：%d 秒. %n", stopwatch.elapsed().getSeconds()); // %n 为换行
    }

}
