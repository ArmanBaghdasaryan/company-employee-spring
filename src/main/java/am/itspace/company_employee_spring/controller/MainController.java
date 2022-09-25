package am.itspace.company_employee_spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("index")
    public String mainPage(){
        return "index";
    }

}
