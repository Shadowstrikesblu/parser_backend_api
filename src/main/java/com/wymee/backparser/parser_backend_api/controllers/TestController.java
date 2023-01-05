package com.wymee.backparser.parser_backend_api.controllers;

import com.wymee.backparser.parser_backend_api.data.JobsDAO;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContext;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class TestController {

    private final JobsDAO jobsDAO;

    final
    ServletContext context;

    public TestController(JobsDAO jobsDAO, ServletContext context) {
        this.jobsDAO = jobsDAO;
        this.context = context;
    }
    //private HttpServletRequest request;

    @GetMapping("/test/jobs")
    public String index(Model model){
        model.addAttribute("list jobs", jobsDAO.findAll());

        return model.toString();
    }


}
