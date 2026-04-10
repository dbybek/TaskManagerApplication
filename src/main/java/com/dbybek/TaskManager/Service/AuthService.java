package com.dbybek.TaskManager.Service;

import com.dbybek.TaskManager.Model.User;
import com.dbybek.TaskManager.Repository.UserRepository;
import com.dbybek.TaskManager.dtos.AuthRequest;
import com.dbybek.TaskManager.dtos.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Long register(AuthRequest request) {

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // 🔐
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        } else {
            user.setRole("ROLE_USER"); // default
        }

        userRepository.save(user);

        return user.getId();
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
            log.warn("Invalid login attempt: {}", request.getUsername());
            throw new UsernameNotFoundException("User not found");
        }

        log.info("Login attempt for user: {}", user.get().getUsername());

        String token = jwtService.generateToken(user.get().getUsername(), user.get().getRole());

        return new AuthResponse(token);
    }
}
