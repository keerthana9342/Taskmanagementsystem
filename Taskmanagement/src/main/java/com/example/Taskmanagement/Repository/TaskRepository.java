package com.example.taskmanagement.Repository;
import com.example.taskmanagement.Model.Task;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findByMilestoneId(String milestoneId);
    List<Task> findByProjectId(String projectId);
    void deleteByMilestoneId(String milestoneId);
        @Query("{ 'assignedTo.employeeId': ?0 }")
    List<Task> findByAssignedToEmployeeId(String employeeId);

    // delete by assignedTo.employeeId
    @Query(value = "{ 'assignedTo.employeeId': ?0 }", delete = true)
    void deleteByAssignedToEmployeeId(String employeeId);
    List<Task> findByStatus(String status);
    @Query("{ $or: [" +
        "{ 'title': { $regex: ?0, $options: 'i' } }," +
        "{ 'description': { $regex: ?0, $options: 'i' } }," +
        "{ 'status': { $regex: ?0, $options: 'i' } }," +
        "{ 'projectId': { $regex: ?0, $options: 'i' } }," +
        "{ 'milestoneId': { $regex: ?0, $options: 'i' } }," +
        "{ 'remarks': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedTo.employeeId': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedTo.username': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedTo.designation': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedBy.employeeId': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedBy.username': { $regex: ?0, $options: 'i' } }," +
        "{ 'assignedBy.designation': { $regex: ?0, $options: 'i' } }" +
        "] }")
Page<Task> searchTasks(String keyword, Pageable pageable);

     


}