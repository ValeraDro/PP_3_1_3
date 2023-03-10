package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.dao.RoleRepo;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;

@Controller
public class UserController {

    private final UserServiceImpl users;
    private final RoleRepo roleRepo;

    @Autowired
    public UserController(UserServiceImpl users, RoleRepo roleRepo) {
        this.users = users;
        this.roleRepo = roleRepo;
    }

    @GetMapping(value = "/")
    public String printWelcome() {
        return "redirect:/user";
    }

    @GetMapping("/admin")
    public String index(Model model) {
        model.addAttribute("users", users.index());
        return "admin/list";
    }

    @GetMapping("/admin/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", users.show(id));
        return "admin/show";
    }

    @GetMapping("/admin/new")
    public String newUser(ModelMap model) {
        User user = new User();
        model.addAttribute("user", user);
        model.addAttribute("roles", roleRepo.findAll());
        return "admin/new";
    }

    @PostMapping("/admin")
    public String create(@ModelAttribute("user") User user) {
        users.save(user);
        return "redirect:/admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("roles", roleRepo.findAll());
        model.addAttribute("user", users.show(id));
        return "admin/edit";
    }

    @PatchMapping("/admin/{id}")
    public String update(@ModelAttribute("user") User user,
                         @PathVariable("id") int id) {
        users.update(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin/{id}")
    public String delete(@PathVariable("id") int id) {
        users.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String showUser(ModelMap model, Principal principal) {
        User user = users.getUserByUsername(principal.getName());
        System.out.println(user);
        model.addAttribute("user", user);
        return "admin/show";
    }
}

