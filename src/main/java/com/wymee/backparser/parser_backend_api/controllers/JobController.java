package com.wymee.backparser.parser_backend_api.controllers;

import com.wymee.backparser.parser_backend_api.model.Job;
import com.wymee.backparser.parser_backend_api.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/job")
@CrossOrigin
public class JobController {

    @Autowired
    private JobRepository jobRepository;

    @GetMapping
    public List<Job> findAllJobs() {
        return (List<Job>) jobRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Job> findJobById(@PathVariable(value = "id") long id) {
        Optional<Job> job = jobRepository.findById(id);

        if (job.isPresent()) {
            return ResponseEntity.ok().body(job.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Job saveJob(@Validated @RequestBody Job job) {
        return jobRepository.save(job);
    }

}
