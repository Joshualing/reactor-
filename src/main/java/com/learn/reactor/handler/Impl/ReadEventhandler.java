package com.learn.reactor.handler.Impl;

import com.learn.reactor.data.EventType;
import com.learn.reactor.event.Event;
import com.learn.reactor.handler.EventHandler;
import com.learn.reactor.selector.Selector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadEventhandler extends EventHandler implements Runnable{
    @Autowired
    private Selector selector;

    private Event event;

    @Override
    public void run() {
        handler(event);
    }
    /**
     * 模拟读一次，写两次
     * @param event
     */
    @Override
    public void handler(Event event) {
        StringBuilder sb=new StringBuilder();
        sb.append(Thread.currentThread().toString()).append(" ");
        if(event.getType()== EventType.READ){
            sb.append("处理READ事件:").append(event.getSource());
            System.out.println(sb.toString());
            try {
                Thread.sleep(3*1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            Event writeEvent1=new Event(event.getSource(),EventType.WRITE);
            Event writeEvent2=new Event(event.getSource(),EventType.WRITE);
            selector.addEvent(writeEvent1);
            selector.addEvent(writeEvent2);
        }
    }
}