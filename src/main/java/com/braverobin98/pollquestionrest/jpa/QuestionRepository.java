package com.braverobin98.pollquestionrest.jpa;

import com.braverobin98.pollquestionrest.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;


public interface QuestionRepository extends JpaRepository<Question, Integer>{

    //Поиск по id опроса
    Page<Question> findByPollId(int poll_id, Pageable pageable);

    //Удаление по id опроса и id вопроса
    @Transactional
    void deleteByPollIdAndId(int poll_id, int question_id);
}
