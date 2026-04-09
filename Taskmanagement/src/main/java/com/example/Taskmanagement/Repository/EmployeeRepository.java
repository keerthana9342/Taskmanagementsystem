package com.example.taskmanagement.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import com.example.taskmanagement.Model.Employee;
import java.util.Optional;


public interface EmployeeRepository extends MongoRepository<Employee, String> {
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByEmployeeId(String employeeId);
        @Query("{ $or: [" +
            "{ 'username': { $regex: ?0, $options: 'i' } }," +
            "{ 'firstName': { $regex: ?0, $options: 'i' } }," +
            "{ 'lastName': { $regex: ?0, $options: 'i' } }," +
            "{ 'email': { $regex: ?0, $options: 'i' } }," +
            "{ 'employeeId': { $regex: ?0, $options: 'i' } }," +
            "{ 'departmentId': { $regex: ?0, $options: 'i' } }," +
            "{ 'designation': { $regex: ?0, $options: 'i' } }," +
            "{ 'status': { $regex: ?0, $options: 'i' } }," +
            "{ 'roleId': { $regex: ?0, $options: 'i' } }," +
            "{ 'phone': { $regex: ?0, $options: 'i' } }" +
            "] }")
    Page<Employee> searchEmployees(String keyword, Pageable pageable);
}
    
    
