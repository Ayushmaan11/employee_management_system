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

    public List<EmployeeResponseDTO> getAllEmployees(){
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream()
                .map(employee -> new EmployeeResponseDTO(
                        employee.getId(),
                        employee.getFirstName(),
                        employee.getLastName(),
                        employee.getEmail(),
                        employee.getDepartment(),
                        employee.getSalary()
                ))
                .toList();
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

    public EmployeeResponseDTO getEmployeeById(Long id){
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Employee with id " + id + " not found!"
        ));

        return new EmployeeResponseDTO(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }

    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO request) {

        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Employee not found with id:" + id
        ));

        existingEmployee.setFirstName(request.getFirstName());
        existingEmployee.setLastName(request.getLastName());
        existingEmployee.setEmail(request.getEmail());
        existingEmployee.setDepartment(request.getDepartment());
        existingEmployee.setSalary(request.getSalary());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return new EmployeeResponseDTO(
                updatedEmployee.getId(),
                updatedEmployee.getFirstName(),
                updatedEmployee.getLastName(),
                updatedEmployee.getEmail(),
                updatedEmployee.getDepartment(),
                updatedEmployee.getSalary()
        );

    }

    public void deleteEmployee(Long id){

        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException(
                "Employee not found with id:" + id
        ));
        employeeRepository.delete(employee);
    }
}
