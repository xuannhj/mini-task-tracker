# 📏 BỘ QUY TẮC & TIÊU CHUẨN DỰ ÁN (CODING & PROJECT CONVENTIONS)

Tài liệu này quy định các tiêu chuẩn viết code (Clean Code), thiết kế API, kiểm thử và quản lý mã nguồn Git cho dự án **Mini Task & Issue Tracker**. Việc tuân thủ bộ quy tắc này giúp dự án đạt chuẩn công nghiệp (Industry Standard) và dễ bảo trì.

---

## 1. Quy Tắc Đặt Tên (Naming Conventions)

### 1.1. Backend (Java / Spring Boot)
| Thành phần | Quy tắc | Ví dụ đúng | Ví dụ sai |
| :--- | :--- | :--- | :--- |
| **Package** | Chữ thường toàn bộ (`lowercase`) | `com.xuannhi.tasktracker.service` | `com.xuannhi.taskTracker.Service` |
| **Class / Interface / Enum** | `PascalCase` (Danh từ) | `TaskController`, `TaskService`, `Priority` | `taskController`, `Task_Service` |
| **Method (Hàm)** | `camelCase` (Động từ đứng đầu) | `createTask()`, `getTaskById()`, `updateTaskStatus()` | `CreateTask()`, `task_by_id()` |
| **Variable (Biến)** | `camelCase` (Có ý nghĩa, tránh viết tắt) | `createdTask`, `existingTask` | `t`, `obj`, `data1` |
| **Enum Values / Constants** | `UPPER_SNAKE_CASE` | `TODO`, `IN_PROGRESS`, `DONE`, `HIGH` | `inProgress`, `Todo` |

### 1.2. Frontend (ReactJS / JavaScript)
| Thành phần | Quy tắc | Ví dụ đúng |
| :--- | :--- | :--- |
| **React Component** | `PascalCase` (`.jsx`) | `App.jsx`, `TaskCard.jsx`, `TaskForm.jsx` |
| **Hàm xử lý sự kiện (Event Handlers)** | Bắt đầu bằng `handle...` | `handleCreateTask`, `handleStatusChange`, `handleDelete` |
| **Biến State (`useState`)** | `[noun, setNoun]` hoặc `[is..., setIs...]` | `[tasks, setTasks]`, `[isLoading, setIsLoading]` |
| **File tiện ích / API service** | `camelCase` (`.js`) | `api.js` |

---

## 2. Quy Tắc Kiến Trúc 3 Lớp (3-Layer Architecture Rules)

```text
[Client / React] ──> [Controller Layer] ──> [Service Layer] ──> [Repository Layer] ──> [Database]
```

1. **Controller Layer (`controller/`):**
   - **CHỈ** làm nhiệm vụ tiếp nhận HTTP Request, kiểm tra tính hợp lệ đầu vào (`@Valid`) và trả về HTTP Response (`ResponseEntity`).
   - **TUYỆT ĐỐI KHÔNG** viết logic nghiệp vụ (if/else phức tạp) hoặc gọi trực tiếp `TaskRepository` trong Controller.
2. **Service Layer (`service/`):**
   - Chứa toàn bộ logic nghiệp vụ, gán giá trị mặc định, kiểm tra điều kiện và ném ngoại lệ (`ResourceNotFoundException`).
   - Sử dụng **Constructor Injection** (`@RequiredArgsConstructor` + `private final`) thay vì `@Autowired` trực tiếp trên thuộc tính để dễ viết Unit Test.
3. **Repository Layer (`repository/`):**
   - Chỉ giao tiếp với cơ sở dữ liệu thông qua `JpaRepository`.
4. **DTO Pattern (`dto/`):**
   - Khi nhận dữ liệu tạo mới từ Client, phải dùng `TaskRequest` (DTO) thay vì dùng trực tiếp Entity `Task` để bảo vệ các trường nhạy cảm (như `id`, `createdAt`).

---

## 3. Tiêu Chuẩn Thiết Kế RESTful API & HTTP Status Codes

### 3.1. Bảng Quy Hoạch Endpoints
| HTTP Method | URI Endpoint | Mục đích | Status Code Thành Công | Status Code Lỗi |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/tasks` | Lấy danh sách task (hỗ trợ `?status=TODO`) | `200 OK` | `400 Bad Request` |
| `GET` | `/api/tasks/{id}` | Lấy chi tiết 1 task theo ID | `200 OK` | `404 Not Found` |
| `POST` | `/api/tasks` | Tạo mới 1 task | `201 Created` | `400 Bad Request` |
| `PUT` | `/api/tasks/{id}/status` | Cập nhật trạng thái task (`?status=DONE`) | `200 OK` | `400` / `404` |
| `DELETE` | `/api/tasks/{id}` | Xóa 1 task theo ID | `204 No Content` | `404 Not Found` |

### 3.2. Chuẩn Hóa Định Dạng JSON Khi Có Lỗi (Error Response Format)
Mọi lỗi `400 Bad Request` hoặc `404 Not Found` bắt qua `@RestControllerAdvice` đều phải trả về cấu trúc JSON đồng nhất:
```json
{
  "timestamp": "2026-11-03T10:15:30.123",
  "status": 400,
  "error": "Tiêu đề phải từ 3 đến 150 ký tự"
}
```

---

## 4. Quy Tắc Viết Unit Test (JUnit 5 Standards)

1. **Tên hàm Test rõ ràng:** Đặt theo mẫu `test<TênHàm>_<KịchBản>_<KếtQuảMongĐợi>` kết hợp `@DisplayName("Mô tả tiếng Việt rõ nghĩa")`.
   - Ví dụ: `testCreateTask_ValidRequest_ReturnsSavedTaskWithTodoStatus()`
2. **Tuân thủ cấu trúc 3 bước AAA:**
   - `// Arrange`: Chuẩn bị dữ liệu đầu vào (`TaskRequest`).
   - `// Act`: Gọi hàm cần test của `TaskService`.
   - `// Assert`: Dùng `assertNotNull`, `assertEquals`, `assertThrows` để kiểm chứng kết quả.
3. **Tính độc lập (Isolation):** Luôn xóa sạch dữ liệu bằng `@BeforeEach` trước mỗi bài test để kết quả bài test trước không làm ảnh hưởng bài test sau.

---

## 5. Quy Tắc Quản Lý Git (Conventional Commits)

Mỗi khi hoàn thành một chặng, commit code theo cú pháp:
```text
<type>(<scope>): <mô tả ngắn gọn bằng tiếng Anh>
```

* **`chore`**: Khởi tạo dự án, cấu hình file, thêm dependencies.
  * *Ví dụ:* `chore: initialize project structure, plan and coding conventions`
* **`feat`**: Thêm tính năng mới ở backend hoặc frontend.
  * *Ví dụ:* `feat(backend): implement Task entity, DTO and repository layer`
  * *Ví dụ:* `feat(frontend): build task dashboard with status filter`
* **`test`**: Thêm hoặc cập nhật JUnit test / Postman test.
  * *Ví dụ:* `test(backend): add JUnit 5 unit tests for TaskService`
* **`docs`**: Cập nhật tài liệu `README.md`, `TEST_CASES.md`, Postman collection.
  * *Ví dụ:* `docs: add API test cases and Postman collection`
* **`fix`**: Sửa lỗi bug.

