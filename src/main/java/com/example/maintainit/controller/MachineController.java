package com.example.maintainit.controller;
import com.example.maintainit.entity.Machine;
import com.example.maintainit.service.MachineService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/machines") public class MachineController {
    private final MachineService s;
    public MachineController(MachineService s) {
        this.s=s;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) public Machine create(@Valid
    @RequestBody Machine x) {
        return s.create(x);
    }
    @GetMapping public List<Machine> all() {
        return s.all();
    }
    @GetMapping("/{id}")public Machine one(@PathVariable Long id) {
        return s.one(id);
    }
    @PutMapping("/{id}")public Machine update(@PathVariable Long id,@Valid
    @RequestBody Machine x) {
        return s.update(id,x);
    }
    @DeleteMapping("/{id}")@ResponseStatus(HttpStatus.NO_CONTENT)public void delete(@PathVariable Long id) {
        s.delete(id);
    }
}
