package com.xuannhj.tasktracker.service;

import com.xuannhj.tasktracker.dto.TaskRequest;
import com.xuannhj.tasktracker.exception.ResourceNotFoundException;
import com.xuannhj.tasktracker.model.Priority;
import com.xuannhj.tasktracker.model.Status;
import com.xuannhj.tasktracker.model.Task;
import com.xuannhj.tasktracker.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    // Tiêm Repository bằng Constructor Injection (@RequiredArgsConstructor + private final)
    private final TaskRepository taskRepository;

    /**
     * 1. Lấy danh sách task (nếu có truyền status thì lọc theo status, ngược lại lấy tất cả)
     */
    public List<Task> getAllTasks(Status status) {
        if (status != null) {
            return taskRepository.findByStatus(status);
        }
        return taskRepository.findAll();
    }

    /**
     * 2. Lấy chi tiết 1 task theo ID. Nếu không tìm thấy trong DB thì ném ngoại lệ 404
     */
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy công việc với mã ID: " + id));
    }

    /**
     * 3. Tạo mới một task từ dữ liệu Client gửi lên (TaskRequest DTO)
     */
    public Task createTask(TaskRequest request) {
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                // Nếu người dùng không chọn độ ưu tiên thì mặc định đặt là MEDIUM
                .priority(request.getPriority() != null ? request.getPriority() : Priority.MEDIUM)
                .build();

        return taskRepository.save(task);
    }

    /**
     * 4. Cập nhật trạng thái của công việc (TODO -> IN_PROGRESS -> DONE)
     */
    public Task updateTaskStatus(Long id, Status newStatus) {
        Task task = getTaskById(id); // Tái sử dụng hàm trên: nếu ID sai sẽ tự ném 404
        task.setStatus(newStatus);
        return taskRepository.save(task);
    }

    /**
     * 5. Xóa 1 task theo ID
     */
    public void deleteTask(Long id) {
        Task task = getTaskById(id); // Kiểm tra xem task có tồn tại không trước khi xóa
        taskRepository.delete(task);
    }
}

