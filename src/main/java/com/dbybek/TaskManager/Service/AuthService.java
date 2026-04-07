package com.dbybek.TaskManager.Service;

import com.dbybek.TaskManager.Model.User;
import com.dbybek.TaskManager.Repository.UserRepository;
import com.dbybek.TaskManager.dtos.AuthRequest;
import com.dbybek.TaskManager.dtos.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String register(AuthRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole("ROLE_USER"); // default
        }

        userRepository.save(user);

        return "User registered successfully";
    }

    public AuthResponse login(AuthRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        Optional<User> user = userRepository.findByUsername(request.getUsername());

        if(user.isEmpty()){
            throw new UsernameNotFoundException("User not found");
        }

        String token = jwtService.generateToken(user.get().getUsername(), user.get().getRole());

        return new AuthResponse(token);
    }
}
