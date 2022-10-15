package am.itspace.company_employee_spring;

import am.itspace.company_employee_spring.entity.Role;
import am.itspace.company_employee_spring.entity.User;
import am.itspace.company_employee_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@SpringBootApplication
@EnableAsync
@RequiredArgsConstructor
public class CompanyEmployeeSpringApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(CompanyEmployeeSpringApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void run(String... args) throws Exception {
        Optional<User> byEmail = userRepository.findByEmail("armangr2022@gmail.com");

        if (byEmail.isEmpty()) {
            userRepository.save(User.builder()
                    .name("Arman")
                    .surname("Baghdasaryan")
                    .email("armangr2022@gmail.com")
                    .password(passwordEncoder().encode("admin"))
                    .role(Role.ADMIN)
                    .build());
        }

    }
}
