package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.repository.MarathonRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MarathonRepoTest {

    @Autowired
    private MarathonRepository marathonRepository;

    @Test
    public void newMarathonTest(){
        Marathon actual = new Marathon();
        actual.setTitle("Java");
        marathonRepository.save(actual);

        Marathon expected = marathonRepository.findMarathonByTitle("Java");

        Assertions.assertEquals("Java", expected.getTitle());

    }
}
