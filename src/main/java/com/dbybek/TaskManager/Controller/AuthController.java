package com.dbybek.TaskManager.Controller;

import com.dbybek.TaskManager.Model.User;
import com.dbybek.TaskManager.Service.AuthService;
import com.dbybek.TaskManager.dtos.ApiResponse;
import com.dbybek.TaskManager.dtos.AuthRequest;
import com.dbybek.TaskManager.dtos.AuthResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService service){
        this.authService = service;
    }

    @PostMapping("/register")
    public ApiResponse<Long> register(@Valid @RequestBody AuthRequest request){
        Long newUserId = authService.register(request);
        return new ApiResponse<>(
                true,
                "User registered successfully with User ID: ",
                newUserId
        );
    }

    @PostMapping("/login")
    public ApiResponse<AuthResponse>  login(@Valid @RequestBody AuthRequest request) {
        AuthResponse token = authService.login(request);
        return new ApiResponse<>(
                true,
                "User Logged in Successfully.",
                token
        );
    }
}
