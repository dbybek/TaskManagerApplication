package com.dbybek.TaskManager.Controller;

import com.dbybek.TaskManager.Service.AuthService;
import com.dbybek.TaskManager.dtos.AuthRequest;
import com.dbybek.TaskManager.dtos.AuthResponse;
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
    public String register(@RequestBody AuthRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        return authService.login(request);
    }
}
