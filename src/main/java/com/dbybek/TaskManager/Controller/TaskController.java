package com.dbybek.TaskManager.Controller;

import com.dbybek.TaskManager.Service.TaskService;
import com.dbybek.TaskManager.dtos.ApiResponse;
import com.dbybek.TaskManager.dtos.TaskDTO;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping
    public ApiResponse<TaskDTO> createTask(@Valid @RequestBody TaskDTO taskDTO){
        TaskDTO newTask = taskService.createTask(taskDTO);
        return new ApiResponse<>(
                true,
                "Task created successfully",
                newTask
        );
    }

    @GetMapping("/{id}")
    public ApiResponse<TaskDTO> getTask(@PathVariable Long id){
        TaskDTO task = taskService.getTaskById(id);
        return new ApiResponse<>(
                true,
                "Task fetched successfully",
                task
        );
    }

    @GetMapping
    public ApiResponse<Page<TaskDTO>> getAllTasks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        Page<TaskDTO> allTasks = taskService.getAllTasks(page, size, sortBy);
        return new ApiResponse<>(
                true,
                "All Tasks fetched successfully",
                allTasks
        );
    }

    @PutMapping("/{id}")
    public ApiResponse<TaskDTO> updateTask(@PathVariable Long id,@Valid @RequestBody TaskDTO taskDTO){
        TaskDTO updatedTask = taskService.updateTask(id, taskDTO);
        return new ApiResponse<>(
                true,
                "Task updated successfully",
                updatedTask
        );
    }

    @DeleteMapping("/{id}")
    public ApiResponse<TaskDTO> deleteTask(@PathVariable Long id){
        taskService.deleteTask(id);
        return new ApiResponse<>(
                true,
                "Task deleted successfully",
                null
        );
    }
}
