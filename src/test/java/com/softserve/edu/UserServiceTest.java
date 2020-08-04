package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.UserService;
import com.softserve.edu.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest(classes = {Application.class})
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MarathonRepository marathonRepository;

    private Marathon marathon1;
    private Marathon marathon2;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @BeforeEach
    public void before() {
        userService = new UserServiceImpl(userRepository, marathonRepository);



        marathon1 = new Marathon();
        marathon1.setTitle("marathon1");
        marathonRepository.save(marathon1);

        marathon2 = new Marathon();
        marathon2.setTitle("marathon2");
        marathonRepository.save(marathon2);

        user1 = new User();
        user1.setRole(User.Role.TRAINEE);
        user1.setEmail("newUser1@email.com");
        user1.setFirstName("firstName1");
        user1.setLastName("lastName1");
        user1.setPassword("pass1");
        userRepository.save(user1);

        user2 = new User();
        user2.setRole(User.Role.TRAINEE);
        user2.setEmail("newUser2@email.com");
        user2.setFirstName("firstName2");
        user2.setLastName("lastName2");
        user2.setPassword("pass2");
        userRepository.save(user2);

        user3 = new User();
        user3.setRole(User.Role.MENTOR);
        user3.setEmail("newUser3@email.com");
        user3.setFirstName("firstName3");
        user3.setLastName("lastName3");
        user3.setPassword("pass3");
        userRepository.save(user3);

        user4 = new User();
        user4.setRole(User.Role.MENTOR);
        user4.setEmail("newUser4@email.com");
        user4.setFirstName("firstName4");
        user4.setLastName("lastName4");
        user4.setPassword("pass4");
        userRepository.save(user4);

        user5 = new User();
        user5.setRole(User.Role.TRAINEE);
        user5.setEmail("newUser5@email.com");
        user5.setFirstName("firstName5");
        user5.setLastName("lastName5");
        user5.setPassword("pass5");
        userRepository.save(user5);
    }

    @Rollback
    @Test
    public void shouldGetAllStudents() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);
        expected.add(user4);
        expected.add(user5);
        List<User> actual = userService.getAll();
        Assertions.assertEquals(expected, actual);

    }

    @Rollback
    @Test
    public void shouldGetUserById() {
        User expected = user5;
        Long id = user5.getId();

        User actual = userService.getUserById(id);
        Assertions.assertEquals(expected, actual);

    }


    @Rollback
    @Test
    public void shouldCreateOrUpdateUser() {
        user5.setEmail("UpdateEmailUser5@email.com");

        userService.createOrUpdateUser(user5);

        User actual = userRepository.findUserByEmail("UpdateEmailUser5@email.com");

        Assertions.assertEquals(user5, actual);

    }


    @Test
    public void shouldDeleteUserById() {
        Long id = user5.getId();
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);
        expected.add(user4);

        userService.deleteUserFromMarathon(user5, marathon2);
        userService.deleteUserFromMarathon(user5, marathon1);
        userService.deleteUserById(id);

        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);

    }

    @Rollback
    @Test
    public void shouldGetAllByRoleTrainee() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user5);

        List<User> actual = userService.getAllByRole("trainee");

        Assertions.assertEquals(expected, actual);
    }


    @Rollback
    @Test
    public void shouldGetAllByRoleMentor() {
        List<User> expected = new ArrayList<>();
        expected.add(user3);
        expected.add(user4);

        List<User> actual = userService.getAllByRole("mentor");

        Assertions.assertEquals(expected, actual);
    }


    @Rollback
    @Test
    public void shouldAddUserToMarathon() {
        boolean expected = marathon1.getUsers().add(user5);

        boolean actual = userService.addUserToMarathon(user5, marathon1);

        Assertions.assertEquals(expected, actual);

    }

    @Rollback
    @Test
    public void shouldDeleteUserFromMarathon() {

        boolean actual = userService.deleteUserFromMarathon(user5, marathon2);

        Assertions.assertEquals(true, actual);

    }

    @Test
    public void shouldThrowExceptionIfEntityNotFound() {
        Long notExistId = 122L;

        Throwable exception = Assertions.assertThrows(EntityNotFoundException.class, () -> {
            userService.getUserById(notExistId);

        });

        assertEquals(exception.getMessage(), "No user /w id " + notExistId);
        assertEquals(exception.getClass(), EntityNotFoundException.class);

    }


    List<Marathon> makeMarathonList(Marathon marathon) {
        List<Marathon> marathonList = new ArrayList<>();
        marathonList.add(marathon);
        return marathonList;
    }
}
