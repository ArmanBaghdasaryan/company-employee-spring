package am.itspace.company_employee_spring.controller;

import am.itspace.company_employee_spring.dto.CreateUserDto;
import am.itspace.company_employee_spring.entity.User;
import am.itspace.company_employee_spring.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @GetMapping("/user/home")
    public String userPage() {
        return "userPage";
    }

    @GetMapping("/add/user")
    public String addUser(ModelMap model) {
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "addUser";
    }

    @PostMapping("/add/user")
    public String addUser(@ModelAttribute CreateUserDto user,
                          @RequestParam("profPic") MultipartFile file,
                          ModelMap modelMap) throws IOException {
        Optional<User> byEmail = userService.findByEmail(user.getEmail());
        if (byEmail.isPresent()) {
            modelMap.addAttribute("msgEmail", "Email already in use");
            return "addUser";
        }
        if (!file.isEmpty() && file.getSize() > 0) {
            if (file.getContentType() != null && !file.getContentType().contains("image")) {
                modelMap.addAttribute("msgFile", "Please choose only image");
                return "addUser";
            }
        }
        userService.saveUser(user, file);
        return "redirect:/loginPage";
    }


    @GetMapping("/user")
    public String user(ModelMap modelMap) {
        List<User> all = userService.findAllUsers();
        modelMap.addAttribute("users", all);
        return "user";
    }

    @GetMapping(value = "/user/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("fileName") String fileName) throws IOException {
        return userService.getUserImage(fileName);
    }


    @GetMapping("/user/delete")
    public String delete(@RequestParam("byId") int id) {

        userService.deleteById(id);
        return "redirect:/user";
    }

    @GetMapping("/user/verify")
    public String verifyUser(@RequestParam("email") String email,
                             @RequestParam("token") String token) throws Exception {
        userService.verifyUser(email, token);
        return "redirect:/";
    }


}
