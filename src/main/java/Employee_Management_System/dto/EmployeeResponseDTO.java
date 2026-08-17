package Employee_Management_System.dto;

public class EmployeeResponseDTO {


    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String department;
    private Double salary;

    public EmployeeResponseDTO(){

    }

    public EmployeeResponseDTO(Long id, String firstName, String lastName, String email, String department, Double salary){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
    public String getDepartment() {
        return department;
    }
    public Double getSalary() {
        return salary;
    }
}

