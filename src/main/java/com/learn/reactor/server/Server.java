package com.learn.reactor.server;

import com.learn.reactor.acapter.Acceptor;
import com.learn.reactor.data.EventType;
import com.learn.reactor.data.InputSource;
import com.learn.reactor.dispatcher.Dispatcher;
import com.learn.reactor.handler.Impl.AcceptEventHandler;
import com.learn.reactor.handler.Impl.ReadEventhandler;
import com.learn.reactor.handler.Impl.WriteEventhandler;
import com.learn.reactor.selector.Selector;

public class Server {
    //Dispatcher和Acceptor持有同一个selector
    //Acceptor负责将事件放入selector中
    //Dispatcher负责将selector中的事件分发给相对应的handler进行处理
    Selector selector=new Selector();
    Dispatcher eventLooper=new Dispatcher(selector);
    Acceptor acceptor;

    public Server(int port){
        acceptor=new Acceptor(port,selector);
    }

    public void start(){
        //eventLooper绑定EventType.ACCEPT
        eventLooper.registEventHandler(EventType.ACCEPT,new AcceptEventHandler(selector));
        eventLooper.registEventHandler(EventType.READ,new ReadEventhandler(selector));
        eventLooper.registEventHandler(EventType.WRITE,new WriteEventhandler(selector));
        new Thread(acceptor,"Acceptor-"+acceptor.getPort()).start();
        System.out.println("server start...");
        eventLooper.handleEvents();
    }

    public void putEvent(InputSource source){
        acceptor.addNewConnection(source);
    }
}
