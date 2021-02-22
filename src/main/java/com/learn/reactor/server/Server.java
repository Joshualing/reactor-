package com.learn.reactor.server;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.learn.reactor.acapter.Acceptor;
import com.learn.reactor.data.EventType;
import com.learn.reactor.data.InputSource;
import com.learn.reactor.dispatcher.Dispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

@Service
public class Server implements Callable{
    //Dispatcher和Acceptor持有同一个selector
    //Acceptor负责将事件放入selector中
    //Dispatcher负责将selector中的事件分发给相对应的handler进行处理
    //Selector selector=new Selector();
    @Autowired
    private Dispatcher eventLooper;
    @Autowired
    Acceptor acceptor;


    public void start(){
        ThreadFactory acceptThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Accept-task-%d").build();

        ThreadFactory readThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Read-task-%d").build();

        ThreadFactory writeThreadFactory = new ThreadFactoryBuilder()
                .setNameFormat("Write-task-%d").build();

        ExecutorService acceptExecutor = new ThreadPoolExecutor(6, 6,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), acceptThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        ExecutorService readExecutor = new ThreadPoolExecutor(6, 6,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), readThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        ExecutorService writeExecutor = new ThreadPoolExecutor(6, 6,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024), writeThreadFactory, new ThreadPoolExecutor.AbortPolicy());

        eventLooper.registEventHandler(EventType.ACCEPT, acceptExecutor);
        eventLooper.registEventHandler(EventType.READ,readExecutor);
        eventLooper.registEventHandler(EventType.WRITE,writeExecutor);

        new Thread(acceptor,"Acceptor-"+acceptor.getPort()).start();
        System.out.println("server start...");
        //new Thread(eventLooper).start();
        eventLooper.handleEvents();
    }

    public void putEvent(InputSource source){
        acceptor.addNewConnection(source);
    }

    @Override
    public Object call() throws Exception {
        start();
        return "call 返回值...";
    }
}
