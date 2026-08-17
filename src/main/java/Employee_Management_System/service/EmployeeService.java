package Employee_Management_System.service;
import Employee_Management_System.dto.EmployeeResponseDTO;
import Employee_Management_System.dto.EmployeeRequestDTO;
import Employee_Management_System.entity.Employee;
import Employee_Management_System.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO request) {

        Employee employee = new Employee();

        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setDepartment(request.getDepartment());
        employee.setSalary(request.getSalary());

        Employee savedEmployee = employeeRepository.save(employee);

        return new EmployeeResponseDTO(
                savedEmployee.getId(),
                savedEmployee.getFirstName(),
                savedEmployee.getLastName(),
                savedEmployee.getEmail(),
                savedEmployee.getDepartment(),
                savedEmployee.getSalary()
        );
    }

    public Employee getEmployeeById(Long id){
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Employee not found with id:" + id));
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee){

        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Employee not found with id:" + id
        ));

        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id){

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Employee not found with id:" + id
        ));
        employeeRepository.delete(employee);
    }
}
