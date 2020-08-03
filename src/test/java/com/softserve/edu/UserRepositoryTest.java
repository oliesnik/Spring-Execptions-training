package com.softserve.edu;

import com.softserve.edu.model.User;
import com.softserve.edu.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager entityManager;

    private User user1;
    private User user2;
    private User user3;
    private User user4;
    private User user5;

    @BeforeEach
    void setUp() {
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

    @Test
    public void newUserTest() {
        User user = new User();
        user.setRole(User.Role.TRAINEE);
        user.setEmail("newUser@email.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPassword("pass");

        userRepository.save(user);
        User actual = userRepository.findUserByEmail("newUser@email.com");

        Assertions.assertEquals("firstName", actual.getFirstName());
    }

    @Test
    public void getAllUsersByRoleTrainee() {

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user5);

        List<User> actual = userRepository.getAllByRole(User.Role.TRAINEE);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void getAllUsersByRoleMentor() {

        List<User> expected = new ArrayList<>();
        expected.add(user3);
        expected.add(user4);

        List<User> actual = userRepository.getAllByRole(User.Role.MENTOR);

        Assertions.assertEquals(expected, actual);
    }


    @Test
    public void getAllUsers() {

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);
        expected.add(user4);
        expected.add(user5);

        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void findUserByEmail() {

        User actual = userRepository.findUserByEmail("newUser5@email.com");

        Assertions.assertEquals(user5, actual);
    }

    @Test
    public void findUserById() {
        long id = user5.getId();

        User actual = userRepository.getOne(id);

        Assertions.assertEquals(user5, actual);
    }

    @Rollback
    @Test
    public void deleteUser() {
        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);
        expected.add(user4);

        userRepository.delete(user5);

        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }

    @Rollback
    @Test
    public void deleteUserById() {
        long id = user5.getId();

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        expected.add(user3);
        expected.add(user4);

        userRepository.deleteById(id);

        List<User> actual = userRepository.findAll();

        Assertions.assertEquals(expected, actual);
    }
}
