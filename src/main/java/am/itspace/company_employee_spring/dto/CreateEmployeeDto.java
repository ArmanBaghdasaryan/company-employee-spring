package am.itspace.company_employee_spring.dto;

import am.itspace.company_employee_spring.entity.Company;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEmployeeDto {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Double salary;
    private String position;
    private String profilePic;
    private Company company;

}
