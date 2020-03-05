package com.braverobin98.pollquestionrest.entity;



import javax.persistence.*;

//Сущность "опрос"
@Entity
@Table(name = "poll")
public class Poll {

    private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;
    private String name;
    private String start_date;
    private String end_date;
    private boolean activity;

    Poll() {}

    public Poll(int id, String name, String start_date, String end_date, boolean activity) {
        this.id = id;
        this.name = name;
        this.start_date = start_date;
        this.end_date = end_date;
        this.activity = activity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public boolean isActivity() {
        return activity;
    }

    public void setActivity(boolean activity) {
        this.activity = activity;
    }
}
