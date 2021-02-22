package com.learn.reactor.handler.Impl;

import com.learn.reactor.data.EventType;
import com.learn.reactor.event.Event;
import com.learn.reactor.handler.EventHandler;
import com.learn.reactor.selector.Selector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.function.Supplier;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WriteEventhandler extends EventHandler implements Supplier {
    @Autowired
    private Selector selector;

    private Event event;

    /**
     * 写完结束
     * @param event
     */
    @Override
    public String handler(Event event) {
        StringBuilder sb=new StringBuilder();
        sb.append(Thread.currentThread().toString()).append(" ");
        if(event.getType()== EventType.WRITE){
            sb.append("处理WRITE事件:").append(event.getSource());
            //System.out.println(sb.toString());
            try {
                Thread.sleep(5*1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            sb.append(" 处理完成");
        }
        return sb.toString();
    }

    @Override
    public Object get() {
        return handler(event);
    }
}