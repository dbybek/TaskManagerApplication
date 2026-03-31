package com.dbybek.TaskManager.Controller;

import com.dbybek.TaskManager.Model.Task;
import com.dbybek.TaskManager.Service.TaskService;
import com.dbybek.TaskManager.dtos.TaskDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public TaskDTO createTask(@Valid @RequestBody TaskDTO taskDTO){
        return taskService.createTask(taskDTO);
    }

    @GetMapping("/{id}")
    public TaskDTO getTask(@PathVariable Long id){
        return taskService.getTaskById(id);
    }

//    @GetMapping
//    public List<TaskDTO> getAllTasks(){
//        return taskService.getAllTask();
//    }

    @GetMapping
    public Page<TaskDTO> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {

        return taskService.getAllTasks(page, size, sortBy);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(@PathVariable Long id,@Valid @RequestBody TaskDTO taskDTO){
        return taskService.updateTask(id, taskDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
    }
}
