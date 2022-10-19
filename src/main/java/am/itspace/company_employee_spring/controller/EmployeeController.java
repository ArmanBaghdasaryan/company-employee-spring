package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.dto.CreateEmployeeDto;
import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.service.CompanyService;
import am.itspace.company_employee_spring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeController {


    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @GetMapping("/add")
    public String addEmployee(ModelMap model) {
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "addEmployee";
    }

    @PostMapping("/add")
    public String addEmployee(@ModelAttribute CreateEmployeeDto employee,
                              @RequestParam("profPic") MultipartFile file) throws IOException {
        employeeService.saveEmployees(employee, file);
        return "redirect:/employee";
    }


    @GetMapping("")
    public String employee(@RequestParam("page") Optional<Integer> page,
                           @RequestParam("size") Optional<Integer> size,
                           ModelMap modelMap) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Employee> employeesPage = employeeService.findEmployeesWithPage(PageRequest.of(currentPage - 1, pageSize));
        modelMap.addAttribute("employee", employeesPage);
        int totalPages = employeesPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }
        return "employee";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        return employeeService.getEmployeeImage(fileName);
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        employeeService.deleteById(id);
        return "redirect:/employee";
    }

    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable int id,
                               ModelMap modelMap) {
        Optional<Employee> employeeOptional = employeeService.findEmployeesById(id);
        if (employeeOptional.isEmpty()) {
            return "redirect:/admin/home";
        }
        modelMap.addAttribute("employee", employeeOptional.get());
        List<Company> companies = companyService.findAllCompanies();
        modelMap.addAttribute("companies", companies);
        return "editEmployee";

    }

    @PostMapping("/edit")
    public String editEmployee(@ModelAttribute Employee employee,
                               @RequestParam("profPic") MultipartFile file) throws IOException {
        employeeService.EmployeesEdit(employee, file);
        return "redirect:/employee";
    }


}
