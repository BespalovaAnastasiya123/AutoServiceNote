package org.project.controllers;


import org.project.models.User;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class MainController {

    @Autowired
    private UserRepository userRepository;
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Главная страница");
        return "home";
    }

    @GetMapping("/addNote")
    public String note(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername);
        // Проверка, забанен ли пользователь
        if (!currentUser.isEnabled()) {
            return "redirect:/viewNoteError";
        }
        // Добавление имени пользователя в модель
        model.addAttribute("username", currentUsername);
        model.addAttribute("addNote", "Добавление заметки");
        return "addNote";
    }

}