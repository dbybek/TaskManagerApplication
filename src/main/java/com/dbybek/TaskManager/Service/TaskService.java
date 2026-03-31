package com.dbybek.TaskManager.Service;

import com.dbybek.TaskManager.Exception.TaskNotFoundException;
import com.dbybek.TaskManager.Mapper.TaskMapper;
import com.dbybek.TaskManager.Model.Task;
import com.dbybek.TaskManager.Repository.TaskRepository;
import com.dbybek.TaskManager.dtos.TaskDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(TaskDTO dto){
        Task task = TaskMapper.toEntity(dto);

        Task savedTask = taskRepository.save(task);

        return TaskMapper.toDTO(savedTask);
    }

    public TaskDTO getTaskById(Long id){
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        return TaskMapper.toDTO(task);
    }

//    public List<TaskDTO> getAllTask(){
//        return taskRepository.findAll()
//                .stream()
//                .map(TaskMapper::toDTO)
//                .toList();
//    }

    public Page<TaskDTO> getAllTasks(int page, int size, String sortBy) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        return taskRepository.findAll(pageable)
                .map(TaskMapper::toDTO);
    }

    public TaskDTO updateTask(Long id, TaskDTO updatedTaskDTO) {
        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

        existing.setTitle(updatedTaskDTO.getTitle());
        existing.setDescription(updatedTaskDTO.getDescription());
        existing.setCompleted(updatedTaskDTO.isCompleted());

        Task updated = taskRepository.save(existing);

        return TaskMapper.toDTO(updated);
    }

    public void deleteTask(Long id){
        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }
        taskRepository.deleteById(id);
    }
}
