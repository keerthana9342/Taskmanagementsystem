package com.example.taskmanagement.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.Project;
public interface ProjectRepository extends MongoRepository<Project,String> {

}
