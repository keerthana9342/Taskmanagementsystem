package com.example.taskmanagement.Repository;
import com.example.taskmanagement.Model.Task;
import java.util.List;
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

     


}