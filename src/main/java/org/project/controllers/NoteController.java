package org.project.controllers;

import org.project.models.Note;
import org.project.models.User;
import org.project.repo.NoteRepository;
import org.project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Controller
public class NoteController {
    @Autowired
    private NoteRepository noteRepository;
    @Autowired
    private UserRepository userRepository;

    @PostMapping("/addNote")
    public String noteUser(Model model,
                           @RequestParam String date,
                           @RequestParam String carBrand,
                           @RequestParam String carModel,
                           @RequestParam String maintenanceWork,
                           @RequestParam Double maintenanceCost) {

        // Проверка на пустые поля
        if (date.isEmpty() || carBrand.isEmpty() || carModel.isEmpty() || maintenanceWork.isEmpty() || maintenanceCost == null) {
            model.addAttribute("message", "Все поля обязательны для заполнения!");
            return "addNote";
        }

        // Проверка формата даты
        LocalDate parsedDate;
        try {
            parsedDate = LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            model.addAttribute("message", "Неверный формат даты. Используйте формат ГГГГ-ММ-ДД.");
            return "addNote";
        }

        // Проверка диапазона стоимости ТО
        if (maintenanceCost < 0 || maintenanceCost > 1000000) {
            model.addAttribute("message", "Стоимость ТО должна быть в диапазоне от 0 до 1,000,000.");
            return "addNote";
        }

        // Получение текущего пользователя
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        // Сохранение заметки
        Note note = new Note(date, carBrand, carModel, maintenanceWork, maintenanceCost, currentUser);
        noteRepository.save(note);
        model.addAttribute("username", currentUsername);
        model.addAttribute("message1", "Заметка успешно добавлена!");
        return "addNote";
    }

    @GetMapping("/viewNote")
    public String viewNote(Model model) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        // Проверка, забанен ли пользователь
        if (!currentUser.isEnabled()) {
            return "redirect:/viewNoteError";
        }

        Iterable<Note> note = noteRepository.findByUser(currentUser);
        model.addAttribute("note", note);
        model.addAttribute("username", currentUsername);
        return "viewNote";

    }

    @GetMapping("/viewNoteError")
    public String viewNoteError(Model model) {
        return "viewNoteError";
    }

    @GetMapping("/editNote/{id}")
    public String editNoteForm(@PathVariable("id") Long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid note Id:" + id));
        model.addAttribute("note", note);
        return "editNote";
    }

    @PostMapping("/editNote/{id}")
    public String editNote(@PathVariable("id") Long id,
                           @RequestParam String date,
                           @RequestParam String carBrand,
                           @RequestParam String carModel,
                           @RequestParam String maintenanceWork,
                           @RequestParam Double maintenanceCost,
                           Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid note Id:" + id));
        note.setDate(date);
        note.setCarBrand(carBrand);
        note.setCarModel(carModel);
        note.setMaintenanceWork(maintenanceWork);
        note.setMaintenanceCost(maintenanceCost);
        noteRepository.save(note);
        return "redirect:/viewNote";
    }

    @GetMapping("/deleteNote/{id}")
    public String deleteNote(@PathVariable("id") Long id, Model model) {
        Note note = noteRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid note Id:" + id));
        noteRepository.delete(note);
        return "redirect:/viewNote";
    }
}