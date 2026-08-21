package Employee_Management_System.controller;

import Employee_Management_System.dto.EmployeeRequestDTO;
import Employee_Management_System.dto.EmployeeResponseDTO;
import Employee_Management_System.entity.Employee;
import Employee_Management_System.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Operation(
            summary = "Get all employees",
            description = "Returns a paginated and sortable list of employees"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employees retrieved successfully"
            )
    })
    @GetMapping
    public ResponseEntity<Page<EmployeeResponseDTO>> getEmployees(

            @Parameter(
                    name = "department",
                    description = "Filter employee by department",
                    example = "Engineering"
            )
            @RequestParam(required = false) String department,
            Pageable pageable) {

        return ResponseEntity.ok(
                employeeService.getEmployees(
                        department,
                        pageable
                )
        );
    }

    @Operation(
            summary = "Create a new employee",
            description = "Creates a new employee and saves it to the database"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Employee created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = " Validation failed"
            )
    })
    @PostMapping
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO request) {

        EmployeeResponseDTO employee = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(employee);
    }

    @Operation(
            summary = "Get employee by ID",
            description = "Returns a single employee using their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Employee retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @Parameter(
                    name = "id",
                    description = "Unique ID of the employee",
                    example = "1"
            )
            @PathVariable("id") Long id){
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(
            summary = "Update employee",
            description = "Updates an existing employee using their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "employee updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validtion failed"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(

            @Parameter(
                    name = "id",
                    description = "unique Id of the employee",
                    example = "1"
            )
            @PathVariable("id") Long id,
            @Valid @RequestBody EmployeeRequestDTO request) {

        EmployeeResponseDTO employee = employeeService.updateEmployee(id, request);
        return ResponseEntity.ok(employee);
    }

    @Operation(
            summary = "Delete employee",
            description = "Deletes an existing employee using their ID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Employee deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Employee not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(

            @Parameter(
                    name = "id",
                    description = "Unique ID of the employee",
                    example = "1"
            )
            @PathVariable("id") Long id){

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }

}
