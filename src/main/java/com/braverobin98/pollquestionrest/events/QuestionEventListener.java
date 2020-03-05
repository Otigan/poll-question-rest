package com.braverobin98.pollquestionrest.events;

import com.braverobin98.pollquestionrest.jpa.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class QuestionEventListener implements ApplicationListener<QuestionEvent> {
    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

    @Autowired
    private PollRepository pollRepository;

    @Override
    public void onApplicationEvent(QuestionEvent questionEvent) {
        questionEvent.getPoll().setStart_date(df.format(new Date()));
        pollRepository.save(questionEvent.getPoll());
    }
}
