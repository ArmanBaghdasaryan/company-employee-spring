package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.dto.CreateEmployeeDto;
import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.entity.Employee;
import am.itspace.company_employee_spring.service.CompanyService;
import am.itspace.company_employee_spring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
public class EmployeeController {


    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @GetMapping("/add/employee")
    public String addEmployee(ModelMap model) {
        List<Company> companies = companyService.findAllCompanies();
        model.addAttribute("companies", companies);
        return "addEmployee";
    }

    @PostMapping("/add/employee")
    public String addEmployee(@ModelAttribute CreateEmployeeDto employee,
                              @RequestParam("profPic") MultipartFile file) throws IOException {
        employeeService.saveEmployees(employee, file);
        return "redirect:/employee";
    }


    @GetMapping("/employee")
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

    @GetMapping(value = "/employee/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        return employeeService.getEmployeeImage(fileName);
    }

    @GetMapping("/employee/delete")
    public String delete(@RequestParam("byId") int id) {
        employeeService.deleteById(id);
        return "redirect:/employee";

    }

}
