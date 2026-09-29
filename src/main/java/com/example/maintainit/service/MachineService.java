package com.example.maintainit.service;
import com.example.maintainit.entity.Machine;
import com.example.maintainit.exception.ResourceNotFoundException;
import com.example.maintainit.repository.MachineRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class MachineService  {
    private final MachineRepository r;
    public MachineService(MachineRepository r) {
        this.r=r;
    }
    public Machine create(Machine m) {
        if(m.getCurrentUsageHours()==null)m.setCurrentUsageHours(0.0);
        if(m.getUsageHoursAtLastMaintenance()==null)m.setUsageHoursAtLastMaintenance(0.0);
        return r.save(m);
    }
    public List<Machine> all() {
        return r.findAll();
    }
    public Machine one(Long id) {
        return r.findById(id).orElseThrow(()->new ResourceNotFoundException("Machine not found: "+id));
    }
    public Machine update(Long id,Machine x) {
        Machine m=one(id);
        m.setName(x.getName());
        m.setModel(x.getModel());
        m.setMaintenanceIntervalType(x.getMaintenanceIntervalType());
        m.setMaintenanceIntervalValue(x.getMaintenanceIntervalValue());
        return r.save(m);
    }
    public void delete(Long id) {
        r.delete(one(id));
    }
}
