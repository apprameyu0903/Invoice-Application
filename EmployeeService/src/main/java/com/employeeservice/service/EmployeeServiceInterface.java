package com.employeeservice.service;

import java.util.List;

import com.employeeservice.model.Employee;

public interface EmployeeServiceInterface {
    Employee createEmployee(Employee employee);
    Employee updateEmployee(Employee employee);
    void deleteEmployee(String empId);
    Employee getEmployeeById(String empId);
    List<Employee> getAllEmployees();
    boolean employeeExists(String empId);
    
}