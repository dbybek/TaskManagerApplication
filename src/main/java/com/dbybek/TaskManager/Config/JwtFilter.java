package com.dbybek.TaskManager.Config;

import com.dbybek.TaskManager.Service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;
        String role = null;

        // ✅ Extract token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            username = jwtService.extractUsername(jwt);
            role = jwtService.extractRole(jwt);
        }

        // ✅ Validate token
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

//            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                    username,
                    "",
                    List.of(new SimpleGrantedAuthority(role))
            );

            if (jwtService.isTokenValid(jwt, userDetails.getUsername())) {

                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            System.out.println("Username: " + username);
            System.out.println("Role from token: " + role);
            System.out.println("Authorities: " + userDetails.getAuthorities());
        }

        filterChain.doFilter(request, response);
    }
}
