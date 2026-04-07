package com.dbybek.TaskManager.Mapper;

import com.dbybek.TaskManager.Model.Task;
import com.dbybek.TaskManager.dtos.TaskDTO;

public class TaskMapper {

    private TaskMapper() {}

    public static Task toEntity(TaskDTO dto) {
        Task task = new Task();
        task.setId(dto.getId());
        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setCompleted(dto.isCompleted());
        return task;
    }

    public static TaskDTO toDTO(Task task) {
        TaskDTO dto = new TaskDTO();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        // ✅ Map username safely
        dto.setUsername(task.getUser().getUsername());
        dto.setCompleted(task.isCompleted());
        return dto;
    }

}
