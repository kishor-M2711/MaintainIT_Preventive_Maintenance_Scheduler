package com.example.maintainit.repository;
import com.example.maintainit.entity.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MachineRepository extends JpaRepository<Machine,Long> {
}
