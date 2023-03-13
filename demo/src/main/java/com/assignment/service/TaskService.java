package com.assignment.service;

import com.assignment.model.entity.Employee;
import com.assignment.model.entity.Task;
import com.assignment.repository.EmployeeRepository;
import com.assignment.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public TaskService(TaskRepository taskRepository, EmployeeRepository employeeRepository) {

        this.taskRepository = taskRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElseThrow(() -> new IllegalStateException("Entity not found."));
    }

    public Task updateTask(Long taskId, Task updatedTask) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalStateException("Task with id " + taskId + " does not exist"));

        String title = updatedTask.getTitle();
        if (!task.getTitle().equals(title)) {
            task.setTitle(title);
        }

        String description = updatedTask.getDescription();
        if(!task.getDescription().equals(description)) {
            task.setDescription(description);
        }

//        Long empId = updatedTask.getEmployee().getId();
//        if(!empId.equals(taskId)) {
//
//        }

        LocalDate dueDate = updatedTask.getDueDate();
        if(!task.getDueDate().equals(dueDate)) {
            task.setDueDate(dueDate);
        }

        return task;
    }

    //delete Task
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    //create Task
    public Task createTask(Task task, Long employeeId) {
        Optional<Task> taskOptional =
                taskRepository.findByTitle(task.getTitle());
        if (taskOptional.isPresent()) {
            throw new IllegalStateException("Task already exists");
        }

        Optional<Employee> employeeOptional =
                employeeRepository.findById(employeeId);
        if (employeeOptional.isEmpty()) {
            throw new IllegalStateException("Employee with id " + employeeId + " does not exist");
        }

        task.setEmployee(employeeOptional.get());
        taskRepository.save(task);
        return task;
    }

    //Find top 5 employee for the last month
    public List<Employee> topEmployees() {
        Map<Integer, Set<Employee>> employeesGroupedByTasksCount = groupEmployeesByTasksCount();

        List<Employee> topEmployees = new ArrayList<>();
        int numberOfTopEmployees = 0;
        for (Map.Entry<Integer, Set<Employee>> e : employeesGroupedByTasksCount.entrySet()) {
            if (numberOfTopEmployees >= 5) {
                break;
            }
            Set<Employee> employeesSet = e.getValue();
            topEmployees.addAll(employeesSet);
            numberOfTopEmployees += employeesSet.size();
        }
        return topEmployees;

    }


    private Map<Employee, List<Task>> tasksForEmployeeForPeriod() {
        LocalDate today = LocalDate.now();

        LocalDate startOfPreviousMonth = today.minusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
        LocalDate endOfPreviousMonth = today.with(TemporalAdjusters.firstDayOfMonth())
                .minusDays(1);

        return taskRepository.findByDueDateBetweenOrderByEmployeeAsc(startOfPreviousMonth, endOfPreviousMonth).stream()
                .collect(Collectors.groupingBy(Task::getEmployee));
    }

    private Map<Integer, Set<Employee>> groupEmployeesByTasksCount() {
        Map<Employee, List<Task>> tasksForEmployee = tasksForEmployeeForPeriod();

        return tasksForEmployee.entrySet()
                .stream()
                .collect(Collectors.groupingBy(e -> e.getValue().size(),
                        TreeMap::new,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toSet())))
                .descendingMap();
    }
}
