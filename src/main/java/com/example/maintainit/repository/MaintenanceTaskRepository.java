package com.example.maintainit.repository;
import com.example.maintainit.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MaintenanceTaskRepository extends JpaRepository<MaintenanceTask,Long> {
    boolean existsByMachineIdAndStatus(Long machineId,TaskStatus status);
    List<MaintenanceTask> findByStatusOrderByCreatedAtAsc(TaskStatus status);
    List<MaintenanceTask> findByMachineIdOrderByCreatedAtDesc(Long machineId);
}
