package com.xuannhj.tasktracker.repository;

import com.xuannhj.tasktracker.model.Status;
import com.xuannhj.tasktracker.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/** @Repository Annotation để:
 * đánh dấu Bean cho Spring - báo cho Spring Boot biết đây là 1 thành phần chueyen truy xuâts dữ liệu (Data Access Object - DAO) để Spring tự nạp vào thùng chứa Bean
 * translate exception: vì mỗi database mỗi hãng lại ném ra lỗi khác nhau(mySQL ném ra SQLException, Hibernate ném ra HibernateException,,,) -> nhãn Repository sẽ tự động bắt các lỗi khó hiểu đó và dịch thành các ngoại lệ chuẩn, đồng nhất của Spring -> giúp code tầng trên xử lý lỗi dễ dàng hơn
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
                                                    //Kiểu Entity, kiểu khóa chính

    //dùng wrapper class Long thay vì long là vì
    //khi 1 task được new và chưa được lưu vào DB nó chưa hề có số ID -> id nó mang gtri null
    //-> Long mới chứa dc null, còn long thì ko.
    List<Task> findByStatus(Status status);
    //= WHERE status = ?
    //trả về nhiều công việc mang status đó
}
