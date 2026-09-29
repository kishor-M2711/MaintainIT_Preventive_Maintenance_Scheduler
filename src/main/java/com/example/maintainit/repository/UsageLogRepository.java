package com.example.maintainit.repository;
import com.example.maintainit.entity.UsageLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface UsageLogRepository extends JpaRepository<UsageLog,Long> {
    List<UsageLog> findByMachineIdOrderByLoggedAtDesc(Long machineId);
}
