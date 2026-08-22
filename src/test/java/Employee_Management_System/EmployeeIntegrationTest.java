package Employee_Management_System;

import Employee_Management_System.entity.Employee;
import Employee_Management_System.repository.EmployeeRepository;

import org.springframework.http.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class EmployeeIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Test
    void getEmployeeById_shouldReturnEmployeeFromDatabase()
            throws Exception {

        Employee employee = new Employee(
                null,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        Employee savedEmployee =
                employeeRepository.save(employee);

        mockMvc.perform(
                        get("/api/employees/" + savedEmployee.getId())
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ayush"))
                .andExpect(jsonPath("$.lastName").value("Pandey"))
                .andExpect(jsonPath("$.email")
                        .value("ayush@example.com"));
    }

    @Test
    void getEmployeeById_shouldReturn404_whenEmployeeDoesNotExist()
            throws Exception {

        mockMvc.perform(
                        get("/api/employees/9999")
                )
                .andExpect(status().isNotFound());
    }
    @Test
    void createEmployee_shouldReturn201_andSaveToDatabase()
            throws Exception {

        String request = """
            {
                "firstName": "Rahul",
                "lastName": "Sharma",
                "email": "rahul@example.com",
                "department": "Engineering",
                "salary": 65000
            }
            """;

        mockMvc.perform(
                        post("/api/employees")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Rahul"))
                .andExpect(jsonPath("$.email")
                        .value("rahul@example.com"));

        assertEquals(
                1,
                employeeRepository.count()
        );
    }

    @Test
    void updateEmployee_shouldReturn200_andUpdateDatabase()
            throws Exception {

        Employee employee = new Employee(
                null,
                "Old",
                "Name",
                "old@example.com",
                "HR",
                40000.0
        );

        Employee savedEmployee =
                employeeRepository.save(employee);

        String request = """
            {
                "firstName": "Ayush",
                "lastName": "Pandey",
                "email": "ayush@example.com",
                "department": "Engineering",
                "salary": 70000
            }
            """;

        mockMvc.perform(
                        put("/api/employees/" + savedEmployee.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Ayush"))
                .andExpect(jsonPath("$.salary").value(70000.0));

        Employee updatedEmployee =
                employeeRepository
                        .findById(savedEmployee.getId())
                        .orElseThrow();

        assertEquals("Ayush", updatedEmployee.getFirstName());
        assertEquals("Pandey", updatedEmployee.getLastName());
        assertEquals("Engineering", updatedEmployee.getDepartment());
        assertEquals(70000.0, updatedEmployee.getSalary());
    }

    @Test
    void deleteEmployee_shouldReturn204_andDeleteFromDatabase()
            throws Exception {

        Employee employee = new Employee(
                null,
                "Ayush",
                "Pandey",
                "ayush@example.com",
                "Engineering",
                60000.0
        );

        Employee savedEmployee =
                employeeRepository.save(employee);

        mockMvc.perform(
                        delete("/api/employees/" + savedEmployee.getId())
                )
                .andExpect(status().isNoContent());

        assertTrue(
                employeeRepository
                        .findById(savedEmployee.getId())
                        .isEmpty()
        );
    }
}