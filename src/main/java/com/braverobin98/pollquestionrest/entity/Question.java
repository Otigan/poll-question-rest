package com.braverobin98.pollquestionrest.entity;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

//Сущность "Вопрос"
@Entity
@Table(name = "questions")
public class Question {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;

    //Связь многие-к-одному
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "poll_id", nullable = false, referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Poll poll;

    private String text;

    public Question() {}

    public Question(int id, Poll poll, String text) {
        this.id = id;
        this.poll = poll;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
