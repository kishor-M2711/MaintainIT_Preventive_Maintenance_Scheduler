package com.example.maintainit.controller;
import com.example.maintainit.entity.Technician;
import com.example.maintainit.service.TechnicianService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/technicians") public class TechnicianController {
    private final TechnicianService s;
    public TechnicianController(TechnicianService s) {
        this.s=s;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)public Technician create(@Valid
    @RequestBody Technician x) {
        return s.create(x);
    }
    @GetMapping public List<Technician> all() {
        return s.all();
    }
    @GetMapping("/{id}")public Technician one(@PathVariable Long id) {
        return s.one(id);
    }
}
