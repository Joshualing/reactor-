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
public class AcceptEventHandler extends EventHandler {
    private Selector selector;

    @Override
    public void handler(Event event) {
        if(event.getType()== EventType.ACCEPT){
            System.out.println("处理ACCETP事件:"+event.getSource());
            Event readEvent=new Event(event.getSource(),EventType.READ);
            selector.addEvent(readEvent);
        }
    }
}
