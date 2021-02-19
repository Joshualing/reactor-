package com.learn.reactor.event;

import com.learn.reactor.data.EventType;
import com.learn.reactor.data.InputSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Event {
    private InputSource source;
    private EventType type;
}
