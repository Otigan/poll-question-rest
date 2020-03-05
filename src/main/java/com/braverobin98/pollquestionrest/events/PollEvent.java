package com.braverobin98.pollquestionrest.events;

import com.braverobin98.pollquestionrest.entity.Poll;
import org.springframework.context.ApplicationEvent;

public class PollEvent extends ApplicationEvent {

    private Poll poll;

    public PollEvent(Object source, Poll poll) {
        super(source);
        this.poll = poll;
    }

    public Poll getPoll(){ return poll;}
}
