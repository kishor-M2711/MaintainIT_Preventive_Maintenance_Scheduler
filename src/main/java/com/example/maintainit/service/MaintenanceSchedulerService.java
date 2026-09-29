package com.example.maintainit.service;
import com.example.maintainit.entity.*;
import com.example.maintainit.exception.*;
import com.example.maintainit.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
@Service public class MaintenanceSchedulerService  {
    private static final double THRESHOLD=.80;
    private final MaintenanceTaskRepository tr;
    private final MachineRepository mr;
    private final TechnicianRepository ter;
    public MaintenanceSchedulerService(MaintenanceTaskRepository tr,MachineRepository mr,TechnicianRepository ter) {
        this.tr=tr;
        this.mr=mr;
        this.ter=ter;
    }
    public List<MaintenanceTask> all() {
        return tr.findAll();
    }
    public List<MaintenanceTask> open() {
        return tr.findByStatusOrderByCreatedAtAsc(TaskStatus.OPEN);
    }
    public List<MaintenanceTask> machineTasks(Long id) {
        if(!mr.existsById(id))throw new ResourceNotFoundException("Machine not found: "+id);
        return tr.findByMachineIdOrderByCreatedAtDesc(id);
    }
    public boolean near(Machine m) {
        int n=m.getMaintenanceIntervalValue();
        if(m.getMaintenanceIntervalType()==IntervalType.HOURS) {
            return m.getCurrentUsageHours()-m.getUsageHoursAtLastMaintenance()>=n*THRESHOLD;
        }
        if(m.getLastMaintenanceDate()==null)return true;
        long d=ChronoUnit.DAYS.between(m.getLastMaintenanceDate().toLocalDate(),LocalDate.now());
        return d>=n*THRESHOLD;
    }
    public boolean due(Machine m) {
        int n=m.getMaintenanceIntervalValue();
        if(m.getMaintenanceIntervalType()==IntervalType.HOURS)return m.getCurrentUsageHours()-m.getUsageHoursAtLastMaintenance()>=n;
        if(m.getLastMaintenanceDate()==null)return true;
        return ChronoUnit.DAYS.between(m.getLastMaintenanceDate().toLocalDate(),LocalDate.now())>=n;
    }
    @Transactional public MaintenanceTask auto(Machine m) {
        if(tr.existsByMachineIdAndStatus(m.getId(),TaskStatus.OPEN)||!near(m))return null;
        MaintenanceTask t=new MaintenanceTask();
        t.setTitle("Preventive maintenance - "+m.getName());
        t.setDescription("Auto-generated because the machine is nearing or has reached its maintenance interval.");
        t.setCreatedAt(LocalDateTime.now());
        t.setStatus(TaskStatus.OPEN);
        t.setMachine(m);
        return tr.save(t);
    }
    @Transactional public MaintenanceTask manual(Long mid,String title,String desc) {
        Machine m=mr.findById(mid).orElseThrow(()->new ResourceNotFoundException("Machine not found: "+mid));
        if(tr.existsByMachineIdAndStatus(mid,TaskStatus.OPEN))throw new BusinessRuleException("A maintenance task is already open for this machine.");
        MaintenanceTask t=new MaintenanceTask();
        t.setTitle(title);
        t.setDescription(desc);
        t.setCreatedAt(LocalDateTime.now());
        t.setStatus(TaskStatus.OPEN);
        t.setMachine(m);
        return tr.save(t);
    }
    @Transactional public MaintenanceTask complete(Long tid,Long techId,String notes) {
        MaintenanceTask t=tr.findById(tid).orElseThrow(()->new ResourceNotFoundException("Maintenance task not found: "+tid));
        if(t.getStatus()==TaskStatus.COMPLETED)throw new BusinessRuleException("This maintenance task is already completed.");
        Technician tech=ter.findById(techId).orElseThrow(()->new ResourceNotFoundException("Technician not found: "+techId));
        LocalDateTime now=LocalDateTime.now();
        t.setStatus(TaskStatus.COMPLETED);
        t.setCompletedAt(now);
        t.setCompletionNotes(notes);
        t.setTechnician(tech);
        Machine m=t.getMachine();
        m.setLastMaintenanceDate(now);
        m.setUsageHoursAtLastMaintenance(m.getCurrentUsageHours());
        mr.save(m);
        return tr.save(t);
    }
    public List<MaintenanceTask> overdue() {
        return tr.findByStatusOrderByCreatedAtAsc(TaskStatus.OPEN).stream().filter(t->due(t.getMachine())).toList();
    }
}
