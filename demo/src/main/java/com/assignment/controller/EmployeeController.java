package com.assignment.controller;

import com.assignment.model.entity.Employee;
import com.assignment.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //read Employee
    @GetMapping
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    //read Employee
    @GetMapping(path = "/{id}")
    public Employee getEmployeeById(@PathVariable("id") Long id) {
        return employeeService.getEmployeeById(id);
    }

    //create Employee
    @PostMapping
    public Employee registerNewEmployee(@RequestBody Employee employee) {
        return employeeService.addNewEmployee(employee);
    }

    //delete Employee
    @DeleteMapping(path = "/{id}")
    public void deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
    }

    //update Employee
    @PutMapping(path = "/{id}")
    public void updateEmployee(
            @PathVariable("id") Long id,
            @RequestBody Employee updatedEmployee) {
        employeeService.updateEmployee(id, updatedEmployee);
    }
}
