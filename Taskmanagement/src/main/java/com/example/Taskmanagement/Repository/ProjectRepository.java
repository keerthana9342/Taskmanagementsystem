package com.example.taskmanagement.Repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.taskmanagement.Model.Project;
public interface ProjectRepository extends MongoRepository<Project,String> {
    
     @Query("{ 'mileStones.milestoneId': ?0 }")
    Optional<Project> findByMilestoneId(String milestoneId);

    @Query("{ 'employeeIds': ?0 }")
    Optional<Project> findByEmployeeId(String employeeId);

}
