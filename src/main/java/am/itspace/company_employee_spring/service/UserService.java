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

import javax.mail.MessagingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    public void saveUser(CreateUserDto dto, MultipartFile file) throws IOException, MessagingException {
        User user = createUser(dto);
        if (!file.isEmpty() && file.getSize() > 0) {
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File newFile = new File(folderPath + File.separator + fileName);
            file.transferTo(newFile);
            user.setPicture(fileName);
        }
        user.setEnable(false);
        user.setVerifyToken(UUID.randomUUID().toString());
        userRepository.save(user);
        mailService.sendHtmlEmail(user.getEmail(), "Please verify your email",
                "Hi " + user.getName() + "\n" +
                        "Please verify your account by clicking on this link " +
                        "<a href=\"http://localhost:8080/user/verify?email=" + user.getEmail() + "&token=" + user.getVerifyToken() + "\">Activate</a>"
        );
    }


    public byte[] getUserImage(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }

    public void verifyUser(String email, String token) throws Exception {
        Optional<User> userOptional = userRepository.findByEmailAndVerifyToken(email, token);

        if (userOptional.isEmpty()) {
            throw new Exception("User Does not exists with email and token");
        }
        User user = userOptional.get();
        if (user.isEnable()) {
            throw new Exception("User already enabled");
        }
        user.setEnable(true);
        user.setVerifyToken(null);
        userRepository.save(user);
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
