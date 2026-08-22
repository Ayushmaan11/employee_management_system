package Employee_Management_System.controller;

import Employee_Management_System.dto.EmployeeResponseDTO;
import Employee_Management_System.service.EmployeeService;
import Employee_Management_System.dto.EmployeeRequestDTO;
import Employee_Management_System.exception.ResourceNotFoundException;

import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @MockitoBean
    private EmployeeService employeeService;

    @Test
    void getEmployeeById_shouldReturn200_whenEmployeeExists()
            throws Exception {

        EmployeeResponseDTO employee =
                new EmployeeResponseDTO(
                        1L,
                        "Ayush",
                        "Pandey",
                        "ayush@example.com",
                        "Engineering",
                        60000.0
                );

        when(employeeService.getEmployeeById(1L))
                .thenReturn(employee);

        mockMvc.perform(
                        get("/api/employees/1")
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ayush"))
                .andExpect(jsonPath("$.lastName").value("Pandey"))
                .andExpect(jsonPath("$.email").value("ayush@example.com"));

        verify(employeeService)
                .getEmployeeById(1L);
    }

    @Test
    void createEmployee_shouldReturn201_whenRequestIsValid()
            throws Exception {

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("Ayush");
        request.setLastName("Pandey");
        request.setEmail("ayush@example.com");
        request.setDepartment("Engineering");
        request.setSalary(60000.0);

        EmployeeResponseDTO response =
                new EmployeeResponseDTO(
                        1L,
                        "Ayush",
                        "Pandey",
                        "ayush@example.com",
                        "Engineering",
                        60000.0
                );

        when(employeeService.createEmployee(any(EmployeeRequestDTO.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Ayush"))
                .andExpect(jsonPath("$.email").value("ayush@example.com"));

        verify(employeeService)
                .createEmployee(any(EmployeeRequestDTO.class));
    }

    @Test
    void createEmployee_shouldReturn400_whenRequestIsInvalid()
            throws Exception {

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("");
        request.setLastName("");
        request.setEmail("hello");
        request.setDepartment("");
        request.setSalary(-50.0);

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());

        verifyNoInteractions(employeeService);
    }

    @Test
    void updateEmployee_shouldReturn200_whenRequestIsValid()
            throws Exception {

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("Ayush");
        request.setLastName("Pandey");
        request.setEmail("ayush@example.com");
        request.setDepartment("Engineering");
        request.setSalary(70000.0);

        EmployeeResponseDTO response =
                new EmployeeResponseDTO(
                        1L,
                        "Ayush",
                        "Pandey",
                        "ayush@example.com",
                        "Engineering",
                        70000.0
                );

        when(employeeService.updateEmployee(
                eq(1L),
                any(EmployeeRequestDTO.class)
        )).thenReturn(response);

        mockMvc.perform(
                        put("/api/employees/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.salary").value(70000.0))
                .andExpect(jsonPath("$.department").value("Engineering"));

        verify(employeeService)
                .updateEmployee(
                        eq(1L),
                        any(EmployeeRequestDTO.class)
                );
    }

    @Test
    void updateEmployee_shouldReturn404_whenEmployeeDoesNotExist()
            throws Exception {

        EmployeeRequestDTO request = new EmployeeRequestDTO();

        request.setFirstName("Ayush");
        request.setLastName("Pandey");
        request.setEmail("ayush@example.com");
        request.setDepartment("Engineering");
        request.setSalary(70000.0);

        when(employeeService.updateEmployee(
                eq(999L),
                any(EmployeeRequestDTO.class)
        )).thenThrow(
                new ResourceNotFoundException(
                        "Employee with id 999 not found!"
                )
        );

        mockMvc.perform(
                        put("/api/employees/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isNotFound());

        verify(employeeService)
                .updateEmployee(
                        eq(999L),
                        any(EmployeeRequestDTO.class)
                );
    }

    @Test
    void deleteEmployee_shouldReturn204_whenEmployeeExists()
            throws Exception {

        doNothing()
                .when(employeeService)
                .deleteEmployee(1L);

        mockMvc.perform(
                        delete("/api/employees/1")
                )
                .andExpect(status().isNoContent());

        verify(employeeService)
                .deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_shouldReturn404_whenEmployeeDoesNotExist()
            throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Employee with id 999 not found!"
                )
        )
                .when(employeeService)
                .deleteEmployee(999L);

        mockMvc.perform(
                        delete("/api/employees/999")
                )
                .andExpect(status().isNotFound());

        verify(employeeService)
                .deleteEmployee(999L);
    }
}