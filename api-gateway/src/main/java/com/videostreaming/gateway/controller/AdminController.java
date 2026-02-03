package com.videostreaming.gateway.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("appName", "Video Streaming Microservices");
        model.addAttribute("version", "1.0.0");
        model.addAttribute("eurekaUrl", "http://localhost:8761");
        model.addAttribute("swaggerUrl", "https://swagger.io/docs/");
        return "index"; // Returns templates/index.html
    }

    @GetMapping("/admin")
    public String admin(Model model) {
        model.addAttribute("services", getServiceStatus());
        return "admin"; // Returns templates/admin.html
    }

    private String[][] getServiceStatus() {
        return new String[][] {
                {"Eureka Server", "8761", "Running"},
                {"Account Service", "8081", "Running"},
                {"Video Service", "8082", "Running"},
                {"API Gateway", "8080", "Running"}
        };
    }
}
