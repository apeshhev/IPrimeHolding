package com.assignment.controller;

import com.assignment.model.entity.Employee;
import com.assignment.model.entity.Task;
import com.assignment.service.TaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/task")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //read Task
    @GetMapping(path = "/{id}")
    public Task getTask(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    //read all Tasks
    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getTasks();
    }

    //update Task
    @PutMapping(path = "/{id}")
    public Task updateTask(@PathVariable Long id,@RequestBody Task updatedTask) {
        return taskService.updateTask(id, updatedTask);
    }

    //delete Task
    @DeleteMapping(path = "/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    //create Task
    @PostMapping
    public Task createTask(@RequestBody Task task, @RequestParam Long employeeId){
        return taskService.createTask(task, employeeId);
    }

    //find top 5 Employee for the last month
    @GetMapping(path = "/top")
    public List<Employee> getTopEmployees() {
        return taskService.topEmployees();
    }

}
