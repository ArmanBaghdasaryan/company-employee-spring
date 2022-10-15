package am.itspace.company_employee_spring.service;

import am.itspace.company_employee_spring.dto.CreateUserDto;
import am.itspace.company_employee_spring.entity.Role;
import am.itspace.company_employee_spring.entity.User;
import am.itspace.company_employee_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${company.employee.images.folder}")
    private String folderPath;
    private final MailService mailService;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void saveUser(CreateUserDto dto, MultipartFile file) throws IOException {
        User user = createUser(dto);
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            user.setPicture(fileName);
        }
        userRepository.save(user);
        mailService.sendEmail(user.getEmail(), "Welcome", "Hi " + user.getName() + " \n" +
                "You have successfully registered!");

    }


    public byte[] getUserImage(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    private User createUser(CreateUserDto dto) {
        return User.builder()
                .name(dto.getName())
                .surname(dto.getSurname())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .role(Role.USER)
                .build();
    }
}
