package com.xuannhj.tasktracker.service;

import com.xuannhj.tasktracker.dto.TaskRequest;
import com.xuannhj.tasktracker.exception.ResourceNotFoundException;
import com.xuannhj.tasktracker.model.Priority;
import com.xuannhj.tasktracker.model.Status;
import com.xuannhj.tasktracker.model.Task;
import com.xuannhj.tasktracker.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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

    @Test
    @DisplayName("Test 2: Lọc danh sách Task theo trạng thái TODO trả về đúng số lượng")
    void testGetAllTasks_ByStatus_ReturnsMatchingTasks() {
        // [A - ARRANGE] Bỏ vào giỏ 2 task
        TaskRequest req1 = TaskRequest.builder().title("Học Unit Test 2").priority(Priority.HIGH).build();
        TaskRequest req2 = TaskRequest.builder().title("Học Unit Test 3").priority(Priority.MEDIUM).build();
        TaskRequest req3 = TaskRequest.builder().title("Task đã hoàn thành").priority(Priority.LOW).build();

        taskService.createTask(req1); // default là TODO
        taskService.createTask(req2); // default là TODO
        Task task3 = taskService.createTask(req3);
        taskService.updateTaskStatus(task3.getId(), Status.DONE); // Đổi task3 thành DONE

        // [A - ACT] Bấm lọc chỉ lấy các task TODO
        List<Task> todoTasks = taskService.getAllTasks(Status.TODO);

        // [A - ASSERT] Đếm đúng 2 cái và kiểm tra từng cái đều là TODO
        assertEquals(2, todoTasks.size(), "Khẳng định: Phải lọc ra đúng 2 task TODO");
        for (Task task : todoTasks) {
            assertEquals(Status.TODO, task.getStatus(), "Khẳng định: Mọi task lọc ra phải có trạng thái TODO");
        }
    }

    @Test
    @DisplayName("Test 3: Cập nhật trạng thái Task từ TODO sang IN_PROGRESS thành công")
    void testUpdateTaskStatus_Success() {
        // [A - ARRANGE] tạo 1 task mới (default là TODO)
        TaskRequest request = TaskRequest.builder()
                .title("Làm bài tập Spring Boot")
                .priority(Priority.MEDIUM)
                .build();
        Task savedTask = taskService.createTask(request);

        // [A - ACT] cập nhật trạng thái của task đó sang IN_PROGRESS
        Task updatedTask = taskService.updateTaskStatus(savedTask.getId(), Status.IN_PROGRESS);

        // [A - ASSERT] kiểm chứng trạng thái đã đổi và thông tin gốc vẫn giữ nguyên
        assertEquals(savedTask.getId(), updatedTask.getId(), "Khẳng định: ID của task không bị thay đổi");
        assertEquals(Status.IN_PROGRESS, updatedTask.getStatus(), "Khẳng định: Trạng thái phải được đổi sang IN_PROGRESS");
        assertEquals("Làm bài tập Spring Boot", updatedTask.getTitle(), "Khẳng định: Tiêu đề vẫn giữ nguyên");
    }



    @Test
    @DisplayName("Test 4: Xóa Task thành công và kiểm tra Task đã biến mất khỏi Database")
    void testDeleteTask_Success() {
        // [A - ARRANGE] tao 1 task để lát nữa xóa
        TaskRequest request = TaskRequest.builder()
                .title("Task cần xóa")
                .priority(Priority.LOW)
                .build();
        Task savedTask = taskService.createTask(request);

        // [A - ACT] goi hàm xóa task theo ID
        taskService.deleteTask(savedTask.getId());

        // [A - ASSERT] check vào DB qua taskRepository xem task đã thật sự biến mất chưa
        assertTrue(
                taskRepository.findById(savedTask.getId()).isEmpty(),
                "Khẳng định: Không được tìm thấy task này trong Database nữa"
        );
    }

    @Test
    @DisplayName("Test 5: Tìm Task với ID không tồn tại -> ném ra ResourceNotFoundException")
    void testGetTaskById_NotFound_ThrowsException() {
        // [A - ARRANGE] cbi mã ID  chắc chắn không tồn tại trong DB
        Long nonExistentId = 999L;

        // [A - ACT & ASSERT] gói hành động vào assertThrows để kiểm chứng lỗi 404 nổ ra đúng loại
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> taskService.getTaskById(nonExistentId),
                "Khẳng định: Phải ném ra ResourceNotFoundException khi không tìm thấy ID"
        );

        // ktra thêm nội dung câu báo lỗi có chứa đúng mã ID 999 hay không
        assertTrue(
                exception.getMessage().contains("999"),
                "Khẳng định: Thông báo lỗi phải chứa đúng mã ID 999"
        );
    }


}