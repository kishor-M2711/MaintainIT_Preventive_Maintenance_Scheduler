package com.example.maintainit.controller;
import com.example.maintainit.entity.UsageLog;
import com.example.maintainit.service.UsageLogService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController
@RequestMapping("/api/usage-logs") public class UsageLogController {
    private final UsageLogService s;
    public UsageLogController(UsageLogService s) {
        this.s=s;
    }
    @PostMapping("/machine/{machineId}")@ResponseStatus(HttpStatus.CREATED)public UsageLog log(@PathVariable Long machineId,@Valid
    @RequestBody UsageLog x) {
        return s.log(machineId,x);
    }
    @GetMapping("/machine/{machineId}")public List<UsageLog> byMachine(@PathVariable Long machineId) {
        return s.byMachine(machineId);
    }
}
