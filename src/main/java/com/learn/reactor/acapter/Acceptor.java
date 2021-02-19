package com.learn.reactor.acapter;

import com.learn.reactor.data.EventType;
import com.learn.reactor.data.InputSource;
import com.learn.reactor.event.Event;
import com.learn.reactor.selector.Selector;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acceptor implements Runnable {
    private int port;
    private Selector selector;

    // 代表 serversocket，通过LinkedBlockingQueue来模拟外部输入请求队列
    private BlockingQueue<InputSource> sourceQueue = new LinkedBlockingQueue<>();

    public Acceptor(int port, Selector selector) {
        this.port = port;
        this.selector = selector;
    }

    //外部有输入请求后，需要加入到请求队列中
    public void addNewConnection(InputSource source) {
        sourceQueue.offer(source);
    }

    @Override
    public void run() {
        while (true) {
            InputSource source = null;
            try {
                // 相当于 serversocket.accept()，接收输入请求，该例从请求队列中获取输入请求
                source = sourceQueue.take();
            } catch (InterruptedException exception) {
                // ignore it;
                //exception.printStackTrace();
            }

            //接收到InputSource后将接收到event设置type为ACCEPT，并将source赋值给event
            if (source != null) {
                Event acceptEvent = new Event(source, EventType.ACCEPT);
                selector.addEvent(acceptEvent);
            }
        }
    }
}
