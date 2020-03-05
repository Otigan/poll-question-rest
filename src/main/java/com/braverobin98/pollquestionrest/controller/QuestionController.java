package com.braverobin98.pollquestionrest.controller;

import com.braverobin98.pollquestionrest.ResourceNotFoundException;
import com.braverobin98.pollquestionrest.entity.Poll;
import com.braverobin98.pollquestionrest.entity.Question;
import com.braverobin98.pollquestionrest.events.QuestionEvent;
import com.braverobin98.pollquestionrest.jpa.PollRepository;
import com.braverobin98.pollquestionrest.jpa.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@RestController
public class QuestionController {

    @Autowired
    PollRepository pollRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ApplicationEventPublisher applicationEventPublisher;


    //Отображает все вопросы из опроса, то есть начинает прохождение опроса
    @GetMapping("/poll/{poll_id}/questions")
    public EntityModel<Page<Question>> all(@PathVariable(value = "poll_id") int poll_id,
                                           Pageable pageable) {

        Poll poll = pollRepository.findById(poll_id)
                .orElseThrow(() -> new ResourceNotFoundException("Id " + poll_id + " not found"));

        //Устанавливаем активность опроса
        poll.setActivity(true);

        //Создание события и его регистрация, при срабатывании события
        //устанавливается дата начала прохождения опроса
        QuestionEvent questionEvent = new QuestionEvent(this, poll);
        applicationEventPublisher.publishEvent(questionEvent);


        Page<Question> questions = questionRepository.findByPollId(poll_id, pageable);

        return new EntityModel<>(questions,
                linkTo(methodOn(QuestionController.class).all(poll_id, pageable)).withSelfRel(),
                linkTo(methodOn(PollController.class).one(poll_id)).withRel("go_back"));
    }


    @PostMapping("/poll/{poll_id}/questions")
    public Question createQuestion(@PathVariable(value = "poll_id") int poll_id,
                                   @Valid @RequestBody Question question) {
        return pollRepository.findById(poll_id).map(poll -> {
            question.setPoll(poll);
            poll.setEnd_date(" ");
            pollRepository.save(poll);
            return questionRepository.save(question);
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + poll_id + " not found"));
    }

    @PutMapping("/poll/{poll_id}/questions/{question_id}")
    public Question updateComment(@PathVariable(value = "poll_id") int poll_id,
                                  @PathVariable(value = "question_id") int question_id,
                                  @Valid @RequestBody Question commentRequest) {
        if (!pollRepository.existsById(poll_id)) {
            throw new ResourceNotFoundException("Poll Id " + poll_id + " not found");
        }

        return questionRepository.findById(question_id).map(question -> {
            question.setText(commentRequest.getText());
            return questionRepository.save(question);
        }).orElseGet(() -> {
                commentRequest.setId(question_id);
                commentRequest.setPoll(pollRepository.findById(poll_id).get());
                return questionRepository.save(commentRequest);
        });
    }

    @DeleteMapping("/poll/{poll_id}/questions/{question_id}")
    void deletePoll(@PathVariable(value = "poll_id") int poll_id,
                    @PathVariable(value = "question_id") int question_id) {

        if (!pollRepository.existsById(poll_id)) {
            throw new ResourceNotFoundException("PollId " + poll_id + " not found");
        }
        if (!questionRepository.existsById(question_id)) {
            throw new ResourceNotFoundException("QuestionId " + question_id + " not found");
        }
        questionRepository.deleteByPollIdAndId(poll_id, question_id);
    }
}
