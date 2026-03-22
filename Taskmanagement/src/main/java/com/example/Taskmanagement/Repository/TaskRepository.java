package com.example.taskmanagement.Repository;
import com.example.taskmanagement.Model.Task;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface TaskRepository extends MongoRepository<Task,String> {
     List<Task> findByStatus(String status);

     


}