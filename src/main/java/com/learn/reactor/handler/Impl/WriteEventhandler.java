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
public class WriteEventhandler extends EventHandler {
    private Selector selector;

    /**
     * 写完结束
     * @param event
     */
    @Override
    public void handler(Event event) {
        if(event.getType()== EventType.WRITE){
            System.out.println("处理WRITE事件:"+event.getSource());
        }
    }
}