package com.kimhok.tickets.exception;

import com.kimhok.tickets.common.utils.ApiResponse;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.http.HttpServletRequest;
import kh.gov.nbc.bakong_khqr.exception.KHQRException;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<ErrorResponse> buildError(
            HttpStatus status,
            String error,
            String message,
            HttpServletRequest request,
            Exception ex
    ) {
        log.error("❌ [{}] {} at {} - {}", status.value(), error, request.getRequestURI(), ex.getMessage(), ex);
        ErrorResponse response = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(status.value())
                .error(error)
                .message(message)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(status).body(response);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "Resource Not Found", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<ErrorResponse> handleExpiredJwt(ExpiredJwtException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Token Expired", "Your session has expired. Please login again.", request, ex);
    }

    @ExceptionHandler(MalformedJwtException.class)
    public ResponseEntity<ErrorResponse> handleMalformedJwt(MalformedJwtException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid Token", "Malformed token. Please login again.", request, ex);
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity<ErrorResponse> handleSignature(SignatureException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Invalid Token Signature", "The token signature is invalid. Please login again.", request, ex);
    }

    @ExceptionHandler(UnsupportedJwtException.class)
    public ResponseEntity<ErrorResponse> handleUnsupportedJwt(UnsupportedJwtException ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Unsupported Token", "The provided token type is not supported.", request, ex);
    }

    @ExceptionHandler({BadCredentialsException.class, AuthenticationException.class})
    public ResponseEntity<ErrorResponse> handleAuthentication(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.UNAUTHORIZED, "Authentication Failed", ex.getMessage(), request, ex);
    }

    @ExceptionHandler({AccessDeniedException.class, AuthorizationDeniedException.class})
    public ResponseEntity<ApiResponse<Object>> handleAccessDenied(Exception ex, HttpServletRequest request) {
        log.error("❌ [403] Forbidden at {} - {}", request.getRequestURI(), ex.getMessage());

        ApiResponse<Object> response = ApiResponse.error(
                HttpStatus.FORBIDDEN.value(),
                "Access Denied",
                "You do not have permission to perform this action",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        String message = "Database constraint violation";
        String causeMsg = ex.getMostSpecificCause() != null ? ex.getMostSpecificCause().getMessage() : ex.getMessage();

        if (causeMsg.contains("email")) message = "Email address already exists";
        else if (causeMsg.contains("username")) message = "Username already exists";
        else if (causeMsg.contains("user_roles_user_id")) message = "User already has a role assigned";

        return buildError(HttpStatus.CONFLICT, "Data Conflict", message, request, ex);
    }

    // ⚠️ Bad request
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalRequest(IllegalArgumentException ex, HttpServletRequest request) {
        return buildError(HttpStatus.BAD_REQUEST, "Illegal Exception", ex.getMessage(), request, ex);
    }

    // 📝 Validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<ErrorResponse.FieldValidationError> fieldErrors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            fieldErrors.add(ErrorResponse.FieldValidationError.builder()
                    .field(error.getField())
                    .message(error.getDefaultMessage())
                    .rejectedValue(error.getRejectedValue())
                    .code(error.getCode())
                    .build());
        });

        ex.getBindingResult().getGlobalErrors().forEach(error -> {
            fieldErrors.add(ErrorResponse.FieldValidationError.builder()
                    .field(error.getObjectName())
                    .message(error.getDefaultMessage())
                    .code(error.getCode())
                    .build());
        });

        String summaryMessage = fieldErrors.size() + " validation error(s) found";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Validation Failed")
                .message(summaryMessage)
                .path(request.getRequestURI())
                .traceId(UUID.randomUUID().toString())
                .fieldErrors(fieldErrors)
                .build();

        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflict(ConflictException ex,HttpServletRequest request){
        return buildError(HttpStatus.CONFLICT,"Data Conflict", ex.getMessage(),request,ex);
    }

    // 💥 Catch-all
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", "Something went wrong", request, ex);
    }

    @ExceptionHandler(TicketSoldOutException.class)
    public ResponseEntity<ErrorResponse> handleTicketSoldOutException(TicketSoldOutException ex, HttpServletRequest request) {
        return buildError(HttpStatus.NOT_FOUND, "Tickets Sold Out", ex.getMessage(), request, ex);
    }
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex ,HttpServletRequest request){
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,"No path match API please check",ex.getMessage(),request,ex);
    }

    @ExceptionHandler(KHQRException.class)
    public ResponseEntity<ErrorResponse> handleKhQrException(KHQRException ex, HttpServletRequest request){
        return buildError(HttpStatus.BAD_REQUEST,"Unable to create KHQR",ex.getMessage(),request,ex);
    }

}
