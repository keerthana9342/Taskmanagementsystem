package com.example.taskmanagement.Repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.taskmanagement.Model.Project;
import com.example.taskmanagement.Model.Task;
public interface ProjectRepository extends MongoRepository<Project,String> {
    
     @Query("{ 'mileStones.milestoneId': ?0 }")
    Optional<Project> findByMilestoneId(String milestoneId);

    @Query("{ 'employeeIds': ?0 }")
    Optional<Project> findByEmployeeId(String employeeId);
   @Query("{ $or: [" +
            "{ 'projectId': { $regex: ?0, $options: 'i' } }," +
            "{ 'projectName': { $regex: ?0, $options: 'i' } }," +
            "{ 'employeeIds': { $regex: ?0, $options: 'i' } }," +
            "{ 'mileStones.milestoneId': { $regex: ?0, $options: 'i' } }," +
            "{ 'mileStones.milestoneName': { $regex: ?0, $options: 'i' } }," +
            "{ 'mileStones.status': { $regex: ?0, $options: 'i' } }," +
            "{ 'mileStones.employeeId': { $regex: ?0, $options: 'i' } }," +
            "{ 'mileStones.username': { $regex: ?0, $options: 'i' } }" +
            "] }")
    Page<Project> searchProjects(String keyword, Pageable pageable);
}


