package com.kimhok.tickets.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimhok.tickets.common.utils.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;


        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request,response);
            return;
        }

        jwt = authHeader.substring(7);

        try {
            userEmail = jwtService.extractUsername(jwt);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    handleAuthenticationError(response, HttpStatus.UNAUTHORIZED,
                            "Invalid Token", "The provided token is invalid or expired", request.getRequestURI());
                    return;
                }
            }
        } catch (ExpiredJwtException e) {
            log.warn("JWT token is expired for request: {}", request.getRequestURI());
            handleAuthenticationError(response, HttpStatus.UNAUTHORIZED,
                    "Token Expired", "Your session has expired. Please login again.", request.getRequestURI());
            return;
        } catch (MalformedJwtException e) {
            log.warn("JWT token is malformed for request: {}", request.getRequestURI());
            handleAuthenticationError(response, HttpStatus.UNAUTHORIZED,
                    "Invalid Token", "The provided token is malformed. Please login again.", request.getRequestURI());
            return;
        } catch (UnsupportedJwtException e) {
            log.warn("JWT token is unsupported for request: {}", request.getRequestURI());
            handleAuthenticationError(response, HttpStatus.UNAUTHORIZED,
                    "Unsupported Token", "The provided token is not supported. Please login again.", request.getRequestURI());
            return;
        } catch (Exception e) {
            log.error("JWT authentication error for request: {}", request.getRequestURI(), e);
            handleAuthenticationError(response, HttpStatus.UNAUTHORIZED,
                    "Authentication Error", "An error occurred during authentication. Please try again.", request.getRequestURI());
            return;
        }

        filterChain.doFilter(request, response);
    }

    private void handleAuthenticationError(HttpServletResponse response,
                HttpStatus status,
                String error,
                String message,
                String path) throws IOException {
            response.setStatus(status.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");

            ApiResponse<?> errorResponse = ApiResponse.error(
                    status.value(),
                    error,
                    message,
                    path
            );

            response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
        }

    }