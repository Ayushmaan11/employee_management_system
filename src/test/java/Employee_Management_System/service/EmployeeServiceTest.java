package Employee_Management_System.service;

import Employee_Management_System.dto.EmployeeRequestDTO;
import Employee_Management_System.dto.EmployeeResponseDTO;
import Employee_Management_System.entity.Employee;
import Employee_Management_System.exception.ResourceNotFoundException;
import Employee_Management_System.repository.EmployeeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @Mock // create a fake version of EmployeeRepository
    private EmployeeRepository employeeRepository;

    @InjectMocks // Create the real EmployeeService and inject repository into it
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_shouldThrowException_whenEmployeeDoesNotExist() {

        when(employeeRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> employeeService.getEmployeeById(999L)
                );

        assertEquals(
                "Employee with id 999 not found!",
                exception.getMessage()
        );

        verify(employeeRepository).findById(999L);
    }

    @Test
    void getEmployeeById_shouldReturnEmployee_whenEmployeeExists() {
        Employee employee = new Employee(
                1L,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeResponseDTO result =  employeeService.getEmployeeById(1L);

        assertEquals(1L, result.getId());
        assertEquals("Ayush", result.getFirstName());
        assertEquals("Pandey", result.getLastName());

        verify(employeeRepository).findById(1L);

    }

    @Test
    void createEmployee_shouldCreateAndReturnEmployee() {

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("Ayush");
        request.setLastName("Pandey");
        request.setEmail("ayush@example.com");
        request.setDepartment("Engineering");
        request.setSalary(60000.0);

        Employee savedEmployee = new Employee(
                10L,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        when(employeeRepository.save(any(Employee.class)))
                .thenReturn(savedEmployee);

        EmployeeResponseDTO result =
                employeeService.createEmployee(request);

        assertEquals(10L, result.getId());
        assertEquals("Ayush", result.getFirstName());
        assertEquals("Pandey", result.getLastName());
        assertEquals("ayush@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(60000.0, result.getSalary());

        verify(employeeRepository).save(any(Employee.class));
    }

    @Test
    void updateEmployee_shouldUpdateAndReturnEmployee() {

        Employee existingEmployee = new Employee(
                1L,
                "Old",
                "Name",
                "old@example.com",
                "HR",
                40000.0
        );

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("Ayush");
        request.setLastName("Pandey");
        request.setEmail("ayush@example.com");
        request.setDepartment("Engineering");
        request.setSalary(60000.0);

        Employee updatedEmployee = new Employee(
                1L,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(existingEmployee));

        when(employeeRepository.save(existingEmployee))
                .thenReturn(updatedEmployee);

        EmployeeResponseDTO result =
                employeeService.updateEmployee(1L, request);

        assertEquals(1L, result.getId());
        assertEquals("Ayush", result.getFirstName());
        assertEquals("Pandey", result.getLastName());
        assertEquals("ayush@example.com", result.getEmail());
        assertEquals("Engineering", result.getDepartment());
        assertEquals(60000.0, result.getSalary());

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).save(existingEmployee);
    }

    @Test
    void deleteEmployee_shouldDeleteEmployee_whenEmployeeExists() {

        Employee employee = new Employee(
                1L,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository).findById(1L);
        verify(employeeRepository).delete(employee);
    }

    @Test
    void getEmployees_shouldFilterByDepartment() {

        Employee employee = new Employee(
                1L,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        Page<Employee> employeePage =
                new PageImpl<>(List.of(employee));

        Pageable pageable =
                org.springframework.data.domain.PageRequest.of(0, 10);

        when(employeeRepository.findByDepartment(
                "Engineering",
                pageable
        )).thenReturn(employeePage);

        Page<EmployeeResponseDTO> result =
                employeeService.getEmployees(
                        "Engineering",
                        pageable
                );

        assertEquals(1, result.getTotalElements());

        assertEquals(
                "Ayush",
                result.getContent().get(0).getFirstName()
        );

        assertEquals(
                "Engineering",
                result.getContent().get(0).getDepartment()
        );

        verify(employeeRepository)
                .findByDepartment("Engineering", pageable);
    }
}
