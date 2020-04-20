package ru.cifrak.telecomit.backend.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @RequestMapping
    public String main(Model model) {
        return "/admin/index.html";
    }

    @RequestMapping("/")
    public String main2(Model model) {
        return "/admin/index.html";
    }

}
