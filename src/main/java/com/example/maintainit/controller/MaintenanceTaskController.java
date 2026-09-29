package com.example.maintainit.controller;
import com.example.maintainit.entity.MaintenanceTask;
import com.example.maintainit.service.MaintenanceSchedulerService;
import jakarta.validation.constraints.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/maintenance-tasks") public class MaintenanceTaskController {
    private final MaintenanceSchedulerService s;
    public MaintenanceTaskController(MaintenanceSchedulerService s) {
        this.s=s;
    }
    @GetMapping public List<MaintenanceTask> all() {
        return s.all();
    }
    @GetMapping("/open")public List<MaintenanceTask> open() {
        return s.open();
    }
    @GetMapping("/overdue")public List<MaintenanceTask> overdue() {
        return s.overdue();
    }
    @GetMapping("/machine/{machineId}")public List<MaintenanceTask> machine(@PathVariable Long machineId) {
        return s.machineTasks(machineId);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)public MaintenanceTask create(@RequestBody CreateRequest r) {
        if(r.machineId()==null||r.title()==null||r.title().isBlank())throw new com.example.maintainit.exception.BusinessRuleException("machineId and title are required.");
        return s.manual(r.machineId(),r.title(),r.description());
    }
    @PutMapping("/{taskId}/complete")public MaintenanceTask complete(@PathVariable Long taskId,@RequestBody CompleteRequest r) {
        if(r.technicianId()==null)throw new com.example.maintainit.exception.BusinessRuleException("technicianId is required.");
        return s.complete(taskId,r.technicianId(),r.notes());
    }
    public record CreateRequest(Long machineId,String title,String description) {
    }
    public record CompleteRequest(Long technicianId,String notes) {
    }
}
