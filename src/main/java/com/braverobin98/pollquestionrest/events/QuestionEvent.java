package com.braverobin98.pollquestionrest.events;

import com.braverobin98.pollquestionrest.entity.Poll;
import org.springframework.context.ApplicationEvent;

public class QuestionEvent extends ApplicationEvent {

    private Poll poll;

    public QuestionEvent(Object source, Poll poll) {
        super(source);
        this.poll = poll;
    }

    public Poll getPoll(){ return poll;}
}
