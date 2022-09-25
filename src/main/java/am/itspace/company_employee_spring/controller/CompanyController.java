package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.repository.CompanyRepo;
import am.itspace.company_employee_spring.repository.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    private CompanyRepo companyRepo;
    @Autowired
    private EmployeeRepo employeeRepo;

    @GetMapping("/add/company")
    public String addCompany(ModelMap model) {
        List<Employee> employees = employeeRepo.findAll();
        model.addAttribute("employee", employees);

        return "addCompany";
    }

    @PostMapping("/add/company")
    public String addCompany(@ModelAttribute Company company) {

        companyRepo.save(company);
        return "redirect:/company";
    }

    @GetMapping("/company")
    public String Companies(ModelMap modelMap) {
        List<Company> all = companyRepo.findAll();
        modelMap.addAttribute("companies", all);
        return "company";
    }
}
