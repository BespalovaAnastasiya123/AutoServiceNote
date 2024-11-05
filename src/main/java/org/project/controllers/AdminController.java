package org.project.controllers;

import org.project.models.Notification;
import org.project.models.User;
import org.project.repo.NotificationRepository;
import org.project.repo.NoteRepository;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private NotificationRepository notificationRepository;

    @GetMapping("/admin")
    public String admin(Model model) {
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        if (isAdmin) {
            Iterable<User> users = userRepository.findAll();
            Iterable<Notification> notifications = notificationRepository.findAll();
            model.addAttribute("users", users);
            model.addAttribute("notifications", notifications);
            return "admin";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/banUser/{id}")
    public String banUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setEnabled(false);
        userRepository.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/unbanUser/{id}")
    public String unbanUser(@PathVariable("id") Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        user.setEnabled(true);
        userRepository.save(user);
        return "redirect:/admin";
    }

    @PostMapping("/requestUnblock")
    public String requestUnblock(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        // Создание уведомления
        Notification notification = new Notification("Запрос на разблокировку от пользователя: " + currentUsername);
        notificationRepository.save(notification);

        model.addAttribute("message", "Запрос на разблокировку отправлен администратору.");
        return "viewNoteError";
    }

    @PostMapping("/deleteNotification/{id}")
    public String deleteNotification(@PathVariable("id") Long id) {
        notificationRepository.deleteById(id);
        return "redirect:/admin";
    }

    public long getNoteCountByUser(User user) {
        return noteRepository.countByUser(user);
    }

    @GetMapping("/forgotPassword")
    public String forgotPasswordForm() {
        return "forgotPassword";
    }

    @PostMapping("/forgotPassword")
    public String forgotPassword(@RequestParam("username") String username, @RequestParam("email") String email, Model model) {
        Notification notification = new Notification();
        notification.setMessage("Запрос на восстановление пароля от пользователя: " + username + " (Email: " + email + ")");
        notificationRepository.save(notification);
        model.addAttribute("message", "Запрос на восстановление пароля отправлен администратору.");
        return "forgotPassword";
    }
}