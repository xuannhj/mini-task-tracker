package com.xuannhj.tasktracker.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Bộ xử lý ngoại lệ tập trung (Global Exception Handler) cho toàn bộ ứng dụng.
 * @RestControllerAdvice: Bắt tất cả các lỗi ném ra từ Controller và tự động chuyển kết quả thành JSON.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. Bắt lỗi không tìm thấy tài nguyên (404 Not Found)
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(), // 404
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * 2. Bắt lỗi dữ liệu đầu vào không hợp lệ từ Bean Validation (@NotBlank, @Size) (400 Bad Request)
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        // Lấy câu thông báo lỗi cụ thể đầu tiên mà ta đã định nghĩa trong TaskRequest DTO
        String errorMessage = ex.getBindingResult().getFieldErrors().isEmpty()
                ? "Dữ liệu đầu vào không hợp lệ"
                : ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();

        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(), // 400
                errorMessage
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * 3. Bắt tất cả các lỗi ngoại lệ chưa lường trước khác để bảo vệ Server không bị sập (500 Internal Server Error)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(), // 500
                "Đã xảy ra lỗi nội bộ trên hệ thống: " + ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

