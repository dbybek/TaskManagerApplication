package com.dbybek.TaskManager.Service;

import com.dbybek.TaskManager.Exception.TaskNotFoundException;
import com.dbybek.TaskManager.Mapper.TaskMapper;
import com.dbybek.TaskManager.Model.Task;
import com.dbybek.TaskManager.Model.User;
import com.dbybek.TaskManager.Repository.TaskRepository;
import com.dbybek.TaskManager.Repository.UserRepository;
import com.dbybek.TaskManager.dtos.TaskDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(TaskService.class);

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

        User loggedInUser = user.orElseThrow(() -> { log.warn("User not found.");
                return new UsernameNotFoundException("User not found");});

        log.info("Creating task for user: {}", loggedInUser.getId());

        Task task = TaskMapper.toEntity(dto);

        task.setUser(loggedInUser);

        Task savedTask = taskRepository.save(task);

        return TaskMapper.toDTO(savedTask);
    }

    public TaskDTO getTaskById(Long id){
        log.info("Fetching task with id: {}", id);
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<User> user = userRepository.findByUsername(username);

        User loggedInUser = user.orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        log.info("Fetching task for user: {}", loggedInUser.getId());

        Task task = taskRepository.findById(id)
                .orElseThrow(() -> { log.warn("Task not found: {}", id);
                    return  new TaskNotFoundException(id);});

        // 🔒 Ownership check
        if(!loggedInUser.getRole().equals("ROLE_ADMIN") && !loggedInUser.equals(task.getUser())){
            log.warn("Access Denied for user: {}",loggedInUser.getId());
            throw new AccessDeniedException("Access Denied");
        }

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

        log.info("Fetching all tasks for user: {}", loggedInUser.getId());

        // 👑 ADMIN → get all tasks
        if (loggedInUser.getRole().equals("ROLE_ADMIN")) {
            return taskRepository.findAll(pageable)
                    .map(TaskMapper::toDTO);
        }

        return taskRepository.findByUserUsername(loggedInUser.getUsername(), pageable)
                    .map(TaskMapper::toDTO);
    }

    public TaskDTO updateTask(Long id, TaskDTO updatedTaskDTO) {
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<User> user = userRepository.findByUsername(username);

        User loggedInUser = user.orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        log.info("Updating task for user: {}", loggedInUser.getId());

        Task existing = taskRepository.findById(id)
                .orElseThrow(() -> { log.warn("Task to update not found, taskId: {}", id);
                    return new TaskNotFoundException(id);});

        // 🔒 Ownership check
        if(!loggedInUser.getRole().equals("ROLE_ADMIN") && !loggedInUser.equals(existing.getUser())){
            throw new AccessDeniedException("Access Denied");
        }

        existing.setTitle(updatedTaskDTO.getTitle());
        existing.setDescription(updatedTaskDTO.getDescription());
        existing.setCompleted(updatedTaskDTO.isCompleted());

        Task updated = taskRepository.save(existing);

        return TaskMapper.toDTO(updated);
    }

    public void deleteTask(Long id){
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Optional<User> user = userRepository.findByUsername(username);

        User loggedInUser = user.orElseThrow(() ->
                new UsernameNotFoundException("User not found"));

        if (!taskRepository.existsById(id)) {
            throw new TaskNotFoundException(id);
        }

        // 🔒 Ownership check
        if(!loggedInUser.getRole().equals("ROLE_ADMIN") && !loggedInUser.equals(taskRepository.findById(id).get().getUser())){
            throw new AccessDeniedException("Access Denied");
        }

        taskRepository.deleteById(id);
    }
}
