package com.xuannhj.tasktracker.service;

import com.xuannhj.tasktracker.dto.TaskRequest;
import com.xuannhj.tasktracker.model.Priority;
import com.xuannhj.tasktracker.model.Status;
import com.xuannhj.tasktracker.model.Task;
import com.xuannhj.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
//turn on springboot &h2 db tren Ram to nap Bean
class TaskServiceTest {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskRepository taskRepository;

    //clean db before each test
    @BeforeEach
    void setUp(){
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("Test 1: Tạo Task mới thành công và kiểm tra trạng thái mặc định là TODO")
    //first test: create a new task theo dung quy tac 3 buoc - AAA
    void taskCreateTask_Success(){
        //[A - ARRANGE: PREPARE] Tạo dữ liệu mẫu gửi vào
        TaskRequest request = TaskRequest.builder().title("Học Unit Test cùng JUnit 5").description("Learn about AAA").priority(Priority.HIGH).build();

        //[A - ACT] Gọi hàm thực tế trogn Service
        Task createdTask = taskService.createTask(request);

        //[A - ASSERT] KIEM CHUNG
        assertNotNull(createdTask.getId(), "Khẳng định: ID phải được Database tự sinh ra (không được null)");
        assertEquals("Học Unit Test cùng JUnit 5", createdTask.getTitle(), "Khẳng định: Tiêu đề phải khớp 100%");
        assertEquals(Status.TODO, createdTask.getStatus(), "Khẳng định: Trạng thái mặc định phải tự gán là TODO");
        assertEquals(Priority.HIGH, createdTask.getPriority(), "Khẳng định: Độ ưu tiên phải là HIGH");
        assertNotNull(createdTask.getCreatedAt(), "Khẳng định: Ngày tạo phải tự động có (không được null)");
    }


}