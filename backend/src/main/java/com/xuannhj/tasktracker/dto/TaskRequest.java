package com.xuannhj.tasktracker.dto;


import com.xuannhj.tasktracker.model.Priority;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * Entity - talk with DB
 * DTO talk with (Client/Postman)
 * Bên DTO này mình chỉ mở đúng 3 ô cho ng dùng nhập thôi là: title, description, priority
 * Còn ID(tự tăng), status, createdAt (do backend quyết định)
 *
 * -> DECOUPLING

 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskRequest {
    @NotBlank(message = "Tiêu đề công việc không được để trống")
    @Size(min = 3, max = 150, message = "Tiêu đề phải có độ dài từ 3 đến 150 kí tự")
    private String title;
    private String description;
    private Priority priority;
    //ng dùng chọn, nếu hok chọn hệ thống tự gán MEDIUM;
}
