package org.project.controllers;

import org.project.models.User;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("register", "Страница регистрации");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(Model model,
                               @RequestParam String username,
                               @RequestParam String password,
                               @RequestParam(value = "password2" , required = true) String password2) {
        User user = new User(username, password);
        if (!user.getPassword().equals(password2)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "register";
        }

        if (userRepository.findByUsername(username) != null) {
            model.addAttribute("error", "Пользователь с таким именем уже существует");
            return "register";
        }
        if ("admin".equalsIgnoreCase(user.getUsername())) {
            model.addAttribute("error", "Вы не можете зарегистрироваться под именем admin");
            return "register";
        }
        userRepository.save(user);
        return "redirect:/registerSuccess";
    }

    @GetMapping("/registerSuccess")
    public String showSuccessPage() {
        return "registerSuccess";
    }

}