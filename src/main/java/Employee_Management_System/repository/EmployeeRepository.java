package Employee_Management_System.repository;

import Employee_Management_System.entity.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

   Page<Employee> findByDepartment(String department, Pageable pageable);
}
