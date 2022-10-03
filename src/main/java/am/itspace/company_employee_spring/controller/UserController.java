package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.dto.CreateUserDto;
import am.itspace.company_employee_spring.entity.Role;
import am.itspace.company_employee_spring.entity.User;
import am.itspace.company_employee_spring.repository.UserRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${company.employee.images.folder}")
    private String folderPath;

    @GetMapping("/add/user")
    public String addUser(ModelMap model) {
        List<User> users = userRepo.findAll();
        model.addAttribute("users", users);
        return "addUser";
    }

    @PostMapping("/add/user")
    public String addUser(@ModelAttribute CreateUserDto dto,
                          @RequestParam("profPic") MultipartFile file,
                          ModelMap modelMap) throws IOException {

        if (userRepo.existsByEmailIgnoreCase(dto.getEmail())) {
            modelMap.addAttribute("msgEmail", "Email already in use");
            return "addUser";
        } else {
            User user = createUser(dto);
            if (!file.isEmpty() && file.getSize() > 0) {
                if (file.getContentType() != null && !file.getContentType().contains("image")) {
                    modelMap.addAttribute("msgFile", "Please choose only image");
                    return "addUser";
                }
                String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
                File newFile = new File(folderPath + File.separator + fileName);
                file.transferTo(newFile);
                user.setPicture(fileName);
            }
            userRepo.save(user);
            return "redirect:/user";
        }

    }

    @GetMapping("/user")
    public String user(ModelMap modelMap) {
        List<User> all = userRepo.findAll();
        modelMap.addAttribute("users", all);
        return "user";
    }

    @GetMapping(value = "/user/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(folderPath + File.separator + fileName);
        return IOUtils.toByteArray(inputStream);
    }


    @GetMapping("/user/delete")
    public String delete(@RequestParam("byId") int id) {

        userRepo.deleteById(id);
        return "redirect:/user";
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
