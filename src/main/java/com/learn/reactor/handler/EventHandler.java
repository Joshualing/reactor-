package com.learn.reactor.handler;

import com.learn.reactor.data.InputSource;
import com.learn.reactor.event.Event;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class EventHandler {
    private InputSource source;

    public abstract void handler(Event event);
}
