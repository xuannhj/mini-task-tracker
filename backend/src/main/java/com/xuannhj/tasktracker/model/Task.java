package com.xuannhj.tasktracker.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//lombok là thư viện Java giúp tiêu diệt toàn bộ các đoạn code lặp đi lặp lại bằng annotation
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //id la primary key va database tu tang so 1,2,3,..
    private Long id;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING) //bat buoc hibernate phai dich thanh chu (dang text)
    @Column(nullable = false, length = 20)
    private Status status;

    @Enumerated(EnumType.STRING)

    @Column(nullable = false, length = 20)
    private Priority priority;

    @Column(name ="created_at",nullable = false, updatable = false)
    private LocalDateTime createdAt;


    /**
     * Tự động chạy ngay trc khi INSERT VÀO DB
     * Auto lấy time now
     * If user chưa chọn status -> default là TODO
     * If user chưa chọn priority -> default là MEDIUM
     */
    @PrePersist
    protected void onCreate(){
        this.createdAt = LocalDateTime.now();
        if (this.status == null){
            this.status = Status.TODO;
        }
        if (this.priority == null){
            this.priority = Priority.MEDIUM;
        }
    }
}
