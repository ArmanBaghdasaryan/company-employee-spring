package am.itspace.company_employee_spring.repository;

import am.itspace.company_employee_spring.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company,Integer> {
}
