package com.xuannhj.tasktracker.controller;

import com.xuannhj.tasktracker.dto.TaskRequest;
import com.xuannhj.tasktracker.model.Status;
import com.xuannhj.tasktracker.model.Task;
import com.xuannhj.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // Giải quyết lỗi CORS, cho phép Frontend React (port 5173) gọi sang Backend (port 6971)
public class TaskController {

    private final TaskService taskService;

    /**
     * 1. GET /api/tasks (có hỗ trợ lọc theo status: /api/tasks?status=TODO)
     * Mã trả về: 200 OK
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks(@RequestParam(required = false) Status status) {
        List<Task> tasks = taskService.getAllTasks(status);
        return ResponseEntity.ok(tasks);
    }

    /**
     * 2. GET /api/tasks/{id} (Lấy chi tiết 1 task theo ID)
     * Mã trả về: 200 OK (nếu tìm thấy) hoặc 404 Not Found (nếu không thấy)
     */
    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(task);
    }

    /**
     * 3. POST /api/tasks (Tạo mới 1 task từ JSON gửi lên)
     * @Valid kích hoạt kiểm tra Bean Validation (@NotBlank, @Size)
     * Mã trả về: 201 Created
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody TaskRequest request) {
        Task createdTask = taskService.createTask(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * 4. PUT /api/tasks/{id}/status?status=DONE (Cập nhật trạng thái)
     * Mã trả về: 200 OK
     */
    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam Status status) {
        Task updatedTask = taskService.updateTaskStatus(id, status);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * 5. DELETE /api/tasks/{id} (Xóa 1 task theo ID)
     * Mã trả về: 204 No Content
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}

