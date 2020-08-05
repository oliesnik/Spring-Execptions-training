package com.softserve.edu.controller;

import com.softserve.edu.exception.ManyMarathonsException;
import com.softserve.edu.model.Marathon;
import com.softserve.edu.model.User;
import com.softserve.edu.service.MarathonService;
import com.softserve.edu.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
@Data
public class MarathonController {
    private MarathonService marathonService;
    private UserService studentService;

    public MarathonController(MarathonService marathonService, UserService studentService) {
        this.marathonService = marathonService;
        this.studentService = studentService;
    }

    @GetMapping("/create-marathon")
    public String createMarathon(Model model) {
        model.addAttribute("marathon", new Marathon());
        return "create-marathon";
    }

    @PostMapping("/marathons")
    public String createMarathon(@Validated @ModelAttribute Marathon marathon, BindingResult result) {
        if (result.hasErrors()) {
            return "create-marathon";
        }
        marathonService.createOrUpdate(marathon);
        return "redirect:/marathons";
    }

    @GetMapping("/marathons/edit/{id}")
    public String updateMarathon(@PathVariable long id, Model model) {
        Marathon marathon = marathonService.getMarathonById(id);
        model.addAttribute("marathon", marathon);
        return "update-marathon";
    }

    @PostMapping("/marathons/edit/{id}")
    public String updateMarathon(@PathVariable long id, @ModelAttribute Marathon marathon, BindingResult result) {
        if (result.hasErrors()) {
            return "update-marathon";
        }
        marathonService.createOrUpdate(marathon);
        return "redirect:/marathons";
    }

    @GetMapping("/marathons/delete/{id}")
    public String deleteMarathon(@PathVariable long id) {
        marathonService.deleteMarathonById(id);
        return "redirect:/marathons";
    }

    @GetMapping("/students/{marathon_id}")
    public String getStudentsFromMarathon(@PathVariable("marathon_id") long marathonId, Model model) {
        List<User> students = studentService.getAll().stream().filter(
                student -> student.getMarathons().stream().anyMatch(
                        marathon -> marathon.getId() == marathonId)).collect(Collectors.toList());
        Marathon marathon = marathonService.getMarathonById(marathonId);
        model.addAttribute("students", students);
        model.addAttribute("all_students", studentService.getAll());
        model.addAttribute("marathon", marathon);
        return "marathon-students";
    }

    @GetMapping("/marathons")
    public String getAllMarathons(Model model) {
        List<Marathon> marathons = marathonService.getAll();
        if (marathons.size() > 2) {
            throw new ManyMarathonsException("Too many marathons");
        }
        model.addAttribute("marathons", marathons);
        return "marathons";
    }

    @ExceptionHandler(ManyMarathonsException.class)
    public ModelAndView handleTooManyMarathonsException(HttpServletRequest request, Exception ex) {
        log.error("Requested URL = {}", request.getRequestURL());
        log.error("Exception raised = {}", ex.getMessage());
        ModelAndView modelAndView = new ModelAndView("error/error", HttpStatus.METHOD_NOT_ALLOWED);
        modelAndView.addObject("info", ex.getMessage());
 //       modelAndView.addObject("path", request.getRequestURL());
        return modelAndView;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException() {
        log.error("DataIntegrityViolationException");
        return "delete-error";
    }
}
