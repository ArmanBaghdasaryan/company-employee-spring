package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.repository.CompanyRepository;
import am.itspace.company_employee_spring.repository.EmployeeRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class EmployeeController {

    @Value("${company.employee.images.folder}")
    private String folderPath;
    @Autowired
    private CompanyRepository companyRepo;
    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/add/employee")
    public String addEmployee(ModelMap model) {
        List<Company> companies = companyRepo.findAll();
        model.addAttribute("companies", companies);
        return "addEmployee";
    }

    @PostMapping("/add/employee")
    public String addEmployee(@ModelAttribute Employee employee,
                              @RequestParam("profPic") MultipartFile file) throws IOException {
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            employee.setProfilePic(fileName);
        }
        employee.getCompany().setSize(employee.getCompany().getSize() + 1);
        employeeRepo.save(employee);
        return "redirect:/employee";
    }


    @GetMapping("/employee")
    public String employee(ModelMap modelMap) {
        List<Employee> all = employeeRepo.findAll();
        modelMap.addAttribute("employee", all);
        return "employee";
    }

    @GetMapping(value = "/employee/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    @GetMapping("/employee/delete")
    public String delete(@RequestParam("byId") int id) {
        Optional<Employee> empById = employeeRepo.findById(id);
        if (empById.isPresent()) {
            Employee employee = empById.get();
            Optional<Company> byId = companyRepo.findById(employee.getCompany().getId());
            if (byId.isPresent()) {
                Company company = byId.get();
                company.setSize(company.getSize() - 1);
                companyRepo.save(company);
            }
        }
        employeeRepo.deleteById(id);
        return "redirect:/employee";

    }

}
