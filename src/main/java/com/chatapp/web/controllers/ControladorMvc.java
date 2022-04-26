package com.chatapp.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class ControladorMvc {

    @GetMapping("/")
    public String setRootPath(Model model) throws IOException {
        return "redirect:/chat";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/chat")
    public String chat() {
        return "chat";
    }
}
