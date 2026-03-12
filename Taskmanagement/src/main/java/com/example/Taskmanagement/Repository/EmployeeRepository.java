package com.example.taskmanagement.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.example.taskmanagement.Model.Employee;

public interface EmployeeRepository extends MongoRepository<Employee, String> {
        Employee findByEmail(String email);
        Employee findByEmployeeId(String employeeId);


}



