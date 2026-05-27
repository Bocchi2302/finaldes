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

    @GetMapping("/app/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/app/productos")
    public String productos() {
        return "products";
    }

    @GetMapping("/app/ventas")
    public String ventas() {
        return "sales";
    }

    @GetMapping("/app/inventario")
    public String inventario() {
        return "inventory";
    }

    @GetMapping("/app/proveedores")
    public String proveedores() {
        return "suppliers";
    }

    @GetMapping("/app/compras")
    public String compras() {
        return "purchases";
    }

    @GetMapping("/app/reportes")
    public String reportes() {
        return "reports";
    }

    @GetMapping("/app/predicciones")
    public String predicciones() {
        return "predictions";
    }
}
