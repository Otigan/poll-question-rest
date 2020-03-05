package com.braverobin98.pollquestionrest.controller;


import com.braverobin98.pollquestionrest.ResourceNotFoundException;
import com.braverobin98.pollquestionrest.entity.Poll;
import com.braverobin98.pollquestionrest.events.PollEvent;
import com.braverobin98.pollquestionrest.jpa.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class PollController {

    @Autowired
    private PollRepository repository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");


    //Отобржает все существующие опросы
    @GetMapping("/poll")
    public EntityModel<Page<Poll>> all(@Param("name")Optional<String> name, Pageable pageable) {

        //Деактивирует все опросы
        Page<Poll> polls = repository.findAllByName(name.orElse("_"), pageable);
        for (Poll i : polls
             ) {
            i.setActivity(false);
            repository.save(i);
        }

        return new EntityModel<Page<Poll>>(polls,
                linkTo(methodOn(PollController.class).all(name, pageable)).withSelfRel());

    }

    //Возвращает один конкретный опрос по его id
    @GetMapping("poll/{id}")
    public EntityModel<Poll> one(@PathVariable int id) {

        Poll poll = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + id + " not found"));

        //Деактивирует опрос
        poll.setActivity(false);
        repository.save(poll);

        //Создание события и его регистрация, при срабатывании события
        //устанавливается дата оконочания прохождения опроса
        PollEvent pollEvent = new PollEvent(this, poll);
        applicationEventPublisher.publishEvent(pollEvent);

        //Проверка наличия даты начала прохождения опроса
        //Если опрос не был начат, то дата окончания не сохранится
        if(poll.getStart_date() != null) {
            poll.setEnd_date(df.format(new Date()));
            repository.save(poll);
        }


        return new EntityModel<>(poll,
                linkTo(methodOn(PollController.class).one(id)).withSelfRel(),
                linkTo(methodOn(PollController.class).all(null, null)).withRel("Polls"),
                linkTo(methodOn(QuestionController.class).all(id, null)).withRel("Questions"));
    }


    @PostMapping("/poll")
    Poll newPoll(@RequestBody Poll newPoll) {
        return repository.save(newPoll);
    }


    @PutMapping("/poll/{id}")
    Poll replacePoll(@RequestBody Poll newPoll, @PathVariable int id) {

        return repository.findById(id)
                .map(poll -> {
                    poll.setName(newPoll.getName());
                    poll.setStart_date(newPoll.getStart_date());
                    poll.setEnd_date(newPoll.getEnd_date());
                    poll.setActivity(newPoll.isActivity());
                    return repository.save(poll);
                })
                .orElseGet(() -> {
                    newPoll.setId(id);
                    return repository.save(newPoll);
                });
    }

    @DeleteMapping("/poll/{id}")
    void deletePoll(@PathVariable int id) {
        repository.deleteById(id);
    }
}
