package com.example.maintainit.repository;
import com.example.maintainit.entity.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TechnicianRepository extends JpaRepository<Technician,Long> {
}
