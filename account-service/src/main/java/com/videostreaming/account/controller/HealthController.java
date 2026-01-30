package com.videostreaming.account.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<?> health(){
        return ResponseEntity.ok(
                Map.of(
                        "status", "UP",
                        "service", "account-service"
                )
        );
    }
}
