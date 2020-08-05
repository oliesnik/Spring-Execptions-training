package com.softserve.edu.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({IllegalArgumentException.class})
    public String handleIllegalArgumentException(HttpServletRequest request, Exception ex) {
        log.info("Exception raised = {}::URL={}", ex.getMessage(), request.getRequestURL());
        return "Illegal-argument-exception";
    }

//    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "EntityNotFoundException occurred")
//    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
//    public void handleEntityNotFoundException() {
//        log.warn("Entity not found Exception raised");
//    }

//    @ExceptionHandler(RuntimeException.class)
//    public ModelAndView handleTooManyMarathonsException(RuntimeException ex) {
//        ModelAndView modelAndView = new ModelAndView("error/403");
//        modelAndView.addObject("info", ex.getMessage());
//        modelAndView.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
//        return modelAndView;
//    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ModelAndView handleStudentNotFoundException(Exception ex) {
        ModelAndView modelAndView = new ModelAndView("error/error", HttpStatus.NOT_FOUND);
        modelAndView.addObject("info", ex.getMessage());
        return modelAndView;
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ModelAndView handleStudentExistsException(Exception exception){
        ModelAndView model = new ModelAndView("error/error", HttpStatus.BAD_REQUEST);
        model.addObject("info", exception.getMessage());
        return model;
    }
}
