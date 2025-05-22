package com.yeahpeu.wedding.domain;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EventWrapper {
    private Event event;
    private List<String> tasks;
}
