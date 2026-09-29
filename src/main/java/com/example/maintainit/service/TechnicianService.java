package com.example.maintainit.service;
import com.example.maintainit.entity.Technician;
import com.example.maintainit.exception.ResourceNotFoundException;
import com.example.maintainit.repository.TechnicianRepository;
import org.springframework.stereotype.Service;
import java.util.*;
@Service public class TechnicianService {
    private final TechnicianRepository r;
    public TechnicianService(TechnicianRepository r) {
        this.r=r;
    }
    public Technician create(Technician x) {
        return r.save(x);
    }
    public List<Technician> all() {
        return r.findAll();
    }
    public Technician one(Long id) {
        return r.findById(id).orElseThrow(()->new ResourceNotFoundException("Technician not found: "+id));
    }
}
