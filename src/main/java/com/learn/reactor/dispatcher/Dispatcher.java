package com.learn.reactor.dispatcher;

import com.google.common.collect.ConcurrentHashMultiset;
import com.learn.reactor.data.EventType;
import com.learn.reactor.event.Event;
import com.learn.reactor.handler.EventHandler;
import com.learn.reactor.handler.Impl.AcceptEventHandler;
import com.learn.reactor.handler.Impl.ReadEventhandler;
import com.learn.reactor.handler.Impl.WriteEventhandler;
import com.learn.reactor.selector.Selector;
import com.sun.xml.internal.ws.util.CompletedFuture;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Controller
public class Dispatcher implements Runnable {
    //通过ConcurrentHashMap来维护不同事件处理器
    Map<EventType, ExecutorService> eventHandlerMap = new ConcurrentHashMap<>();
    //本例只维护一个selector负责事件选择，netty为了保证性能实现了多个selector来保证循环处理性能，不同事件加入不同的selector的事件缓冲队列
    @Autowired
    Selector selector;


    Set<Future> taskSet= Collections.synchronizedSet(new HashSet<>());

    //在Dispatcher中注册eventHandler
    public void registEventHandler(EventType eventType, ExecutorService executorService) {
        eventHandlerMap.put(eventType, executorService);
    }

    public void removeEventHandler(EventType eventType) {
        eventHandlerMap.remove(eventType);
    }

    public void handleEvents() {
        dispatch();
    }

    //此例只是实现了简单的事件分发给相应的处理器处理，例子中的处理器都是同步，在reactor模式的典型实现NIO中都是在handle异步处理，来保证非阻塞
    private void dispatch() {
        int count = 0;
        while (true) {
            List<Event> events = selector.select();

            for (Event event : events) {
                ExecutorService executorService = eventHandlerMap.get(event.getType());
                if (event.getType().equals(EventType.ACCEPT)){
                    CompletableFuture completableFuture = CompletableFuture.supplyAsync(new AcceptEventHandler(selector, event), executorService);
                    taskSet.add(completableFuture);
                    completableFuture.thenAccept(System.out::println);
                }
                if (event.getType().equals(EventType.READ)){
                    CompletableFuture completableFuture = CompletableFuture.supplyAsync(new ReadEventhandler(selector, event), executorService);
                    taskSet.add(completableFuture);
                    completableFuture.thenAccept(System.out::println);
                }
                if (event.getType().equals(EventType.WRITE)){
                    CompletableFuture completableFuture = CompletableFuture.supplyAsync(new WriteEventhandler(selector, event), executorService);
                    taskSet.add(completableFuture);
                    completableFuture.thenAccept(System.out::println);
                }
                count++;
            }
            if (count == 40)
                break;
        }
    }

    public boolean taskSetBeDone(){
        boolean ret=true;
        for (Future future : taskSet) {
            if(!future.isDone())
                return false;
        }
        return ret;
    }

    @Override
    public void run() {
        handleEvents();
    }
}
