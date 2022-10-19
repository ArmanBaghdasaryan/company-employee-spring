package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.service.CompanyService;
import am.itspace.company_employee_spring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/company")
public class CompanyController {

    private final CompanyService companyService;
    private final EmployeeService employeeService;

    @GetMapping("/add")
    public String addCompany(ModelMap model) {
        List<Employee> employees = employeeService.findAllEmployees();
        model.addAttribute("employee", employees);
        return "addCompany";
    }

    @PostMapping("/add")
    public String addCompany(@ModelAttribute Company company) {
        companyService.saveCompanies(company);
        return "redirect:/company";
    }

    @GetMapping("")
    public String Companies(ModelMap modelMap) {
        List<Company> all = companyService.findAllCompanies();
        modelMap.addAttribute("companies", all);
        return "company";
    }
}
