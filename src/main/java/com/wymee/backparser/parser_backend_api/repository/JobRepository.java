package com.wymee.backparser.parser_backend_api.repository;

import com.wymee.backparser.parser_backend_api.model.Job;
import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface JobRepository extends CrudRepository<Job, Long> {

}
