package com.softserve.edu.controller;

import com.softserve.edu.exception.StudentAlreadyExistsException;
import com.softserve.edu.exception.StudentNotFoundException;
import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.repository.UserRepository;
import com.softserve.edu.service.MarathonService;
import com.softserve.edu.service.UserService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@Data
public class StudentController {
    Logger logger = LoggerFactory.getLogger(StudentController.class);

    private UserService userService;
    private MarathonService marathonService;

    public StudentController(UserService userService, MarathonService marathonService) {
        this.userService = userService;
        this.marathonService = marathonService;
    }

    @GetMapping("/create-student")
    public String createStudent(Model model) {
        logger.info("Creating a new student");
        model.addAttribute("user", new User());
        return "create-student";
    }

    @PostMapping("/students/{marathon_id}/add")
    public String createStudent(@PathVariable("marathon_id") long marathonId, @Validated @ModelAttribute User user, BindingResult result) {
        logger.info("Adding student to marathon");
        User existed = userService.findUserByEmail(user.getEmail());
        if (existed!=null){
            throw new StudentAlreadyExistsException("Student with such email already exists!");
        }
        if (result.hasErrors()) {
            return "create-student";
        }
        userService.addUserToMarathon(
                userService.createOrUpdateUser(user),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students/{marathon_id}/add")
    public String createStudent(@RequestParam("user_id") long userId, @PathVariable("marathon_id") long marathonId) {
        userService.addUserToMarathon(
                userService.getUserById(userId),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students/{marathon_id}/edit/{student_id}")
    public String updateStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId, Model model) {

        User user = userService.getUserById(studentId);
        model.addAttribute("user", user);
        return "update-student";
    }

    @PostMapping("/students/{marathon_id}/edit/{student_id}")
    public String updateStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId, @Validated @ModelAttribute User user, BindingResult result) {
        logger.info("Editing a student");
        if (result.hasErrors()) {
            return "update-marathon";
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students/{marathon_id}/delete/{student_id}")
    public String deleteStudent(@PathVariable("marathon_id") long marathonId, @PathVariable("student_id") long studentId) {
        userService.deleteUserFromMarathon(
                userService.getUserById(studentId),
                marathonService.getMarathonById(marathonId));
        return "redirect:/students/" + marathonId;
    }

    @GetMapping("/students")
    public String getAllStudents(Model model) {
        logger.info("Get all students method");
        List<User> students = userService.getAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        if (user == null) {
            throw new StudentNotFoundException("Student not found");
        }
        return "update-student";
    }

    @PostMapping("/students/edit/{id}")
    public String updateStudent(@PathVariable long id, @Validated @ModelAttribute User user, BindingResult result) {
        if (result.hasErrors()) {
            return "update-marathon";
        }
        userService.createOrUpdateUser(user);
        return "redirect:/students";
    }

    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable long id) {
        logger.info("Deleting a student");
        User student = userService.getUserById(id);
        for (Marathon marathon : student.getMarathons()) {
            userService.deleteUserFromMarathon(student, marathon);
        }
        userService.deleteUserById(id);
        return "redirect:/students";
    }
}
