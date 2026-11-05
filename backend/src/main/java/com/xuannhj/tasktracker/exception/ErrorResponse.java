package com.xuannhj.tasktracker.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    // Thời điểm chính xác xảy ra lỗi
    private LocalDateTime timestamp;

    // Mã HTTP Status (404, 400...)
    private int status;

    // Câu thông báo lỗi cụ thể
    private String error;
}