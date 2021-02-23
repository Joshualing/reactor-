package com.learn.reactor.context;

import com.learn.reactor.client.Client;
import com.learn.reactor.server.Server;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Data
public class MyContext {
    @Autowired
    private Server server;

    @Autowired
    private Client client;
}
