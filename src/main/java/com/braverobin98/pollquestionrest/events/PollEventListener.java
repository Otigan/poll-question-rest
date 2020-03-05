package com.braverobin98.pollquestionrest.events;

import com.braverobin98.pollquestionrest.controller.PollController;
import com.braverobin98.pollquestionrest.jpa.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class PollEventListener  {

    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");

    @Autowired
    private PollRepository pollRepository;


   @EventListener(value = {PollController.class})
    void settingStartDate(PollEvent pollEvent) {
       pollEvent.getPoll().setEnd_date(df.format(new Date()));
       pollRepository.save(pollEvent.getPoll());
   }
}
