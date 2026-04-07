package com.dbybek.TaskManager.Service;

import com.dbybek.TaskManager.Exception.TaskNotFoundException;
import com.dbybek.TaskManager.Mapper.TaskMapper;
import com.dbybek.TaskManager.Model.Task;
import com.dbybek.TaskManager.Model.User;
import com.dbybek.TaskManager.Repository.TaskRepository;
import com.dbybek.TaskManager.Repository.UserRepository;
import com.dbybek.TaskManager.dtos.TaskDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskDTO createTask(TaskDTO dto){
        // 🔐 Get logged-in username
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // 👤 Fetch user from DB
        Optional<User> user = userRepository.findByUsername(username);

        User loggedInUser = user.orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        Task task = TaskMapper.toEntity(dto);

        task.setUser(loggedInUser);

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

        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<User> user = userRepository.findByUsername(username);

        User loggedInUser = user.orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        // 👑 ADMIN → get all tasks
        if (loggedInUser.getRole().equals("ROLE_ADMIN")) {
            return taskRepository.findAll(pageable)
                    .map(TaskMapper::toDTO);
        }

        return taskRepository.findByUserUsername(loggedInUser.getUsername(), pageable)
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
