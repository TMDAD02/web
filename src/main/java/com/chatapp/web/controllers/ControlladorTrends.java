package com.chatapp.web.controllers;

import com.chatapp.web.services.ServicioTrending;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ControlladorTrends {

    @Autowired
    public ServicioTrending servicioTrending;

    @GetMapping("/trend")
    public String listTrends(Model model, @RequestParam("currentTo") String destino) {

        model.addAttribute("trends", servicioTrending.sorting());

        return "redirect:/chat?&to=" + destino;
    }


}
