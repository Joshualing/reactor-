package com.learn.reactor.handler.Impl;

import com.learn.reactor.data.EventType;
import com.learn.reactor.event.Event;
import com.learn.reactor.handler.EventHandler;
import com.learn.reactor.selector.Selector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReadEventhandler extends EventHandler {
    private Selector selector;

    /**
     * 模拟读一次，写两次
     * @param event
     */
    @Override
    public void handler(Event event) {
        if(event.getType()== EventType.READ){
            System.out.println("处理READ事件:"+event.getSource());
            Event writeEvent1=new Event(event.getSource(),EventType.WRITE);
            Event writeEvent2=new Event(event.getSource(),EventType.WRITE);
            selector.addEvent(writeEvent1);
            selector.addEvent(writeEvent2);
        }
    }
}