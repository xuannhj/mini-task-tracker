package com.xuannhj.tasktracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


//BackendApplication là entry point - là điểm khởi chạy duy nhất chứa hàm Main để bật toàn bộ ứng dụng Backend
@SpringBootApplication
//là nhãn đánh dấu báo cho SpringBoot biết: lấy package hiện tại làm gốc để tự động bật Server/DB(@EnableAutoConfiguration) và tự động quét nạp tất cả Controller, Service, Repo nămd trong các package con (@ComponentScan)
//Configuration: đánh dấu class này là nguồn cung cấp các cấu hình Bean cho Spring
//@EnableAutoConfiguration: tự động cấu hình ứng dụng dựa vào các thư viện trong pom.xml
//@ComponentScan: tự động quét toàn bộ các class nằm bên trong package com.xuannhj.tasktracker (như @RestController, @service, @Repo) để quản lý
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

}
