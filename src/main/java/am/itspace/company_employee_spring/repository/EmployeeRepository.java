package am.itspace.company_employee_spring.repository;

import am.itspace.company_employee_spring.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {



}
