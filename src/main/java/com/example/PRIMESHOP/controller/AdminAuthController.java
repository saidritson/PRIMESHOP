package com.example.PRIMESHOP.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Jesús Rodrigo Villegas Argüelles - 261186
 */
@Controller
public class AdminAuthController {

    @GetMapping("/admin/login")
    public String mostrarLogin() {
        return "AdminLogin";
    }
}
