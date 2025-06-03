package com.swapapp.swapappmockserver.security;

import com.swapapp.swapappmockserver.model.User;
import com.swapapp.swapappmockserver.repository.user.IUserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        
        System.out.println("✅ JwtAuthenticationFilter activado para: " + request.getRequestURI());


        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            String email = jwtUtil.extractEmail(token);
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null && jwtUtil.validateToken(token)) {
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            System.out.println("Authorization: " + authHeader);
            System.out.println("Email extraído del token: " + email);
            System.out.println("Usuario en BD: " + user);
            System.out.println("Token válido: " + jwtUtil.validateToken(token));

        }

        filterChain.doFilter(request, response);
    }
}
