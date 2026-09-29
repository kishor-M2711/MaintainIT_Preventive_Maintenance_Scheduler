package com.example.maintainit.service;
import com.example.maintainit.entity.*;
import com.example.maintainit.exception.ResourceNotFoundException;
import com.example.maintainit.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.*;
@Service public class UsageLogService  {
    private final UsageLogRepository ur;
    private final MachineRepository mr;
    private final MaintenanceSchedulerService sr;
    public UsageLogService(UsageLogRepository ur,MachineRepository mr,MaintenanceSchedulerService sr) {
        this.ur=ur;
        this.mr=mr;
        this.sr=sr;
    }
    @Transactional public UsageLog log(Long id,UsageLog x) {
        Machine m=mr.findById(id).orElseThrow(()->new ResourceNotFoundException("Machine not found: "+id));
        x.setId(null);
        if(x.getLoggedAt()==null)x.setLoggedAt(LocalDateTime.now());
        x.setMachine(m);
        m.setCurrentUsageHours(m.getCurrentUsageHours()+x.getHoursUsed());
        mr.save(m);
        UsageLog saved=ur.save(x);
        sr.auto(m);
        return saved;
    }
    public List<UsageLog> byMachine(Long id) {
        if(!mr.existsById(id))throw new ResourceNotFoundException("Machine not found: "+id);
        return ur.findByMachineIdOrderByLoggedAtDesc(id);
    }
}
