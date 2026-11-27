import axios from 'axios';

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || 'http://localhost:6971/api/tasks';

const apiClient = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    'Content-Type': 'application/json',
  },
  timeout: 8000,
});

/**
 * Lấy danh sách công việc (có thể lọc theo status: TODO | IN_PROGRESS | DONE)
 */
export async function getTasks(status = null) {
  const params = status ? { status } : {};
  const response = await apiClient.get('', { params });
  return response.data;
}

/**
 * Tạo mới một công việc
 */
export async function createTask(payload) {
  const response = await apiClient.post('', payload);
  return response.data;
}

/**
 * Cập nhật trạng thái công việc (PUT /api/tasks/{id}/status?status=...)
 */
export async function updateTaskStatus(id, status) {
  const response = await apiClient.put(`/${id}/status`, null, {
    params: { status },
  });
  return response.data;
}

/**
 * Xóa công việc theo ID
 */
export async function deleteTask(id) {
  await apiClient.delete(`/${id}`);
}

/**
 * Trích xuất thông báo lỗi thân thiện từ ErrorResponse của Spring Boot GlobalExceptionHandler
 */
export function extractErrorMessage(error) {
  if (error?.response?.data?.error) {
    return error.response.data.error;
  }
  if (error?.code === 'ERR_NETWORK' || !error?.response) {
    return 'Không thể kết nối tới Backend (port 6971). Hãy đảm bảo Spring Boot đang chạy.';
  }
  return 'Đã xảy ra lỗi không mong muốn. Vui lòng thử lại.';
}
