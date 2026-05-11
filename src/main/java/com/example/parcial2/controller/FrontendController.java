package com.example.parcial2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    @GetMapping("/")
    public String landing() {
        return "landing";
    }

    @GetMapping("/app/login")
    public String login() {
        return "login";
    }

    @GetMapping("/app/register")
    public String register() {
        return "register";
    }

    @GetMapping("/app/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/app/productos")
    public String products() {
        return "products";
    }

    @GetMapping("/app/inventario")
    public String inventory() {
        return "inventory";
    }

    @GetMapping("/app/ventas")
    public String sales() {
        return "sales";
    }

    @GetMapping("/app/reports")
    public String reports() {
        return "reports";
    }

    @GetMapping("/app/predictions")
    public String predictions() {
        return "predictions";
    }

    @GetMapping("/app/setup-error")
    public String setupError() {
        return "setup_error";
    }
}