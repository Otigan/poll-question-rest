package com.braverobin98.pollquestionrest.jpa;


import com.braverobin98.pollquestionrest.entity.Poll;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PollRepository extends JpaRepository<Poll, Integer> {
    /*
    @Query("select s from Poll s where name like %?1%")
    Page<Poll> findAllByName(@Param("name") String name, Pageable pageable);
    */

    @Query("select s from Poll s where name like %?1%")
    Page<Poll> findAllByName(String name, Pageable pageable);

}
