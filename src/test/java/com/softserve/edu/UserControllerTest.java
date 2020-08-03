package com.softserve.edu;

import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.MarathonRepository;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.MarathonService;
import com.softserve.edu.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = Application.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserService userService;
    @Autowired
    private MarathonService marathonService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MarathonRepository marathonRepository;


    @Test
    public void getAllStudentsTest() throws Exception {
        List<User> expected = userService.getAllByRole("Trainee");

        mockMvc.perform(MockMvcRequestBuilders.get("/students"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("students"))
                .andExpect(MockMvcResultMatchers.model().attribute("students", expected));
    }


    @Test
    public void addStudentTest() throws Exception {
        Marathon marathon = new Marathon();
        marathon.setTitle("Java");
        marathonService.createOrUpdate(marathon);
        long mId = marathon.getId();
        mockMvc.perform(MockMvcRequestBuilders.post("/students/" + mId + "/add")
                .param("firstName", "fName")
                .param("lastName", "lName")
                .param("email", "test@mail.com")
                .param("password", "test1pass")
                .param("role", "TRAINEE"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

//    @Test
//    public void editStudentTest() throws Exception {
//        Marathon marathon = new Marathon();
//        marathon.setTitle("C++");
//        marathonService.createOrUpdate(marathon);
//        long mId = marathon.getId();
//        User student = new User();
//        student.setFirstName("firstName");
//        student.setLastName("lastName");
//        student.setEmail("test2@mail.com");
//        student.setPassword("test2pass");
//        student.setRole(User.Role.TRAINEE);
//        userService.createOrUpdateUser(student);
//        long sId = student.getId();
//
//        mockMvc.perform(MockMvcRequestBuilders.post("/students/" + mId + "/edit/" + sId)
//                .param("firstName", "firstName")
//                .param("lastName", "lastName")
//                .param("email", "test2@mail.com")
//                .param("password", "test2pass")
//                .param("role", "TRAINEE"))
//                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
//    }

    @Test
    public void deleteStudentTest() throws Exception {
        Marathon marathon = new Marathon();
        marathon.setTitle("C++");
        marathonService.createOrUpdate(marathon);

        User student = new User();
        student.setFirstName("firstName");
        student.setLastName("lastName");
        student.setEmail("test3@mail.com");
        student.setPassword("test2pass");
        student.setRole(User.Role.TRAINEE);
        userService.createOrUpdateUser(student);
        userService.addUserToMarathon(student, marathon);
        long sId = student.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/students/delete/"+ sId))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection());
    }

}
