package am.itspace.company_employee_spring.service;

import am.itspace.company_employee_spring.dto.CreateEmployeeDto;
import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.repository.CompanyRepository;
import am.itspace.company_employee_spring.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {


    @Value("${company.employee.images.folder}")
    private String folderPath;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;


    public void saveEmployees(CreateEmployeeDto dto, MultipartFile file) throws IOException {
        Employee employee = crateEmployee(dto);
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);

        }
        employee.getCompany().setSize(employee.getCompany().getSize() + 1);
        employeeRepository.save(employee);
    }


    public Page<Employee> findEmployeesWithPage(Pageable pageable) {
        return employeeRepository.findAll(pageable);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public byte[] getEmployeeImage(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    public void deleteById(int id) {
        Optional<Employee> empById = employeeRepository.findById(id);
        if (empById.isPresent()) {
            Employee employee = empById.get();
            Optional<Company> byId = companyRepository.findById(employee.getCompany().getId());
            if (byId.isPresent()) {
                Company company = byId.get();
                company.setSize(company.getSize() - 1);
                companyRepository.save(company);
            }
            employeeRepository.deleteById(id);
        }
    }

    private Employee crateEmployee(CreateEmployeeDto dto) {

        return Employee.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .salary(dto.getSalary())
                .position(dto.getPosition())
                .profilePic(dto.getProfilePic())
                .company(dto.getCompany())
                .build();
    }

}
