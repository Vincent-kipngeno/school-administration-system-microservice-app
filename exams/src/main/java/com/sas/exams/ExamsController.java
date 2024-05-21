package com.sas.exams;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exams")
public class ExamsController {


    @GetMapping("/home")
    public String greetingMessage(@RequestHeader("loggedInUser") String userName) {
        System.out.println("Logged in User: - " + userName);
        return "Home Exams";
    }
}