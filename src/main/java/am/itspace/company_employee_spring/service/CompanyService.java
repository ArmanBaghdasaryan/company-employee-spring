package am.itspace.company_employee_spring.service;

import am.itspace.company_employee_spring.entity.Company;
import am.itspace.company_employee_spring.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;


    public void saveCompanies(Company company) {
        companyRepository.save(company);

    }

    public List<Company> findAllCompanies() {
        return companyRepository.findAll();
    }

}
