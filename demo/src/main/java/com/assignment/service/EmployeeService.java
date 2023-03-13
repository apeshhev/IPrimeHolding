package com.assignment.service;

import com.assignment.model.entity.Employee;
import com.assignment.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //read all Employees
    public List<Employee> getEmployees() {
        return employeeRepository.findAll();
    }

    //read Employee
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Cannot find employee with id = " + id));
    }

    //create employee
    public Employee addNewEmployee(Employee employee) {
        Optional<Employee> employeeOptional =
                employeeRepository.findEmployeeByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("Email taken!");
        }
        employeeRepository.save(employee);
        return employee;
    }

    //delete Employee
    public void deleteEmployee(Long employeeId) {
        employeeRepository.deleteById(employeeId);
    }

    //update Employee
    public void updateEmployee(Long employeeID, Employee updatedEmployee) {
        Employee employee = employeeRepository.findById(employeeID)
                .orElseThrow(() -> new IllegalStateException("Employee with id " + employeeID + " does not exist"));

        String fullName = updatedEmployee.getFullName();
        if (!employee.getFullName().equals(fullName)) {
            employee.setFullName(fullName);
        }

        String email = updatedEmployee.getEmail();
        if (!employee.getEmail().equals(email)) {
            Optional<Employee> employeeOptional =
                    employeeRepository.findEmployeeByEmail(email);
            if (employeeOptional.isPresent()) {
                throw new IllegalStateException("Email is taken!");
            }
            employee.setEmail(email);
        }

        String phoneNumber = updatedEmployee.getPhoneNumber();
        if (!employee.getPhoneNumber().equals(phoneNumber)) {
            Optional<Employee> employeeOptional =
                    employeeRepository.findEmployeeByPhoneNumber(phoneNumber);
            if (employeeOptional.isPresent()) {
                throw new IllegalStateException("Entered phone number belongs to someone");
            }
            employee.setPhoneNumber(phoneNumber);
        }

        double monthlySalary = updatedEmployee.getMonthlySalary();
        if (!employee.getMonthlySalary().equals(monthlySalary)) {
            employee.setMonthlySalary(monthlySalary);
        }

        employeeRepository.save(updatedEmployee);
    }

}
