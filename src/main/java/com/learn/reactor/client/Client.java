package com.learn.reactor.client;

import com.learn.reactor.data.InputSource;
import com.learn.reactor.server.Server;
import lombok.Data;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Data
@Service
public class Client implements Runnable{
    @SneakyThrows
    @Override
    public void run() {
        start();
    }

    //@Autowired
    //private ClientConfig clientConfig;

    @Autowired
    private Server server;

    public void start() throws InterruptedException {
        long id=0;
        int num=10;
        while(num-->0){
            //每隔5秒生产一个事件
            String string = UUID.randomUUID().toString();
            System.out.println("生成一个新的事件:"+string+" id:"+id);
            InputSource source=new InputSource(string,id++);
            server.putEvent(source);
            //Thread.sleep(3*1000);
        }
    }
}
