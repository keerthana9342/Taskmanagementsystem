package com.example.Taskmanagement.Repository;

import com.example.Taskmanagement.Model.Task;


import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface Taskrepo extends MongoRepository<Task,String> {
     List<Task> findByStatus(String status);
     


}