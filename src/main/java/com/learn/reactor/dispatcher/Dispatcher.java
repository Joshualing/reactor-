package com.learn.reactor.dispatcher;

import com.learn.reactor.data.EventType;
import com.learn.reactor.event.Event;
import com.learn.reactor.handler.EventHandler;
import com.learn.reactor.handler.Impl.AcceptEventHandler;
import com.learn.reactor.handler.Impl.ReadEventhandler;
import com.learn.reactor.handler.Impl.WriteEventhandler;
import com.learn.reactor.selector.Selector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

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
                if (event.getType().equals(EventType.ACCEPT))
                    executorService.submit(new AcceptEventHandler(selector, event));
                if (event.getType().equals(EventType.READ))
                    executorService.submit(new ReadEventhandler(selector, event));
                if (event.getType().equals(EventType.WRITE))
                    executorService.submit(new WriteEventhandler(selector, event));
                count++;
            }
            //System.out.println("count:" + count);
            if (count == 40)
                break;
        }
    }

    @Override
    public void run() {
        handleEvents();
    }
}
