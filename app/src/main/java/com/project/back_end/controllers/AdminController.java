package com.project.back_end.controllers;

import com.project.back_end.models.Admin;
import com.project.back_end.services.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("${api.path}admin")  // Base URL for admin endpoints
public class AdminController {

    private final Service service;

    // ---------------------------------------------------
    // Constructor Injection for Service
    // ---------------------------------------------------
    public AdminController(Service service) {
        this.service = service;
    }

    // ---------------------------------------------------
    // Admin Login Endpoint
    // ---------------------------------------------------
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> adminLogin(@RequestBody Admin admin) {
        return service.validateAdmin(admin);
    }
}
