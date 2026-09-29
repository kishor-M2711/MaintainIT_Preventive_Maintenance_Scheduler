package com.example.maintainit.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name="maintenance_tasks") public class MaintenanceTask  {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String title;
    private String description;
    @Enumerated(EnumType.STRING) private TaskStatus status=TaskStatus.OPEN;
    private LocalDateTime createdAt,completedAt;
    @Column(length=2000) private String completionNotes;
    @ManyToOne(optional=false) private Machine machine;
    @ManyToOne private Technician technician;
    public MaintenanceTask() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String v) {
        title=v;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String v) {
        description=v;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus v) {
        status=v;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime v) {
        createdAt=v;
    }
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(LocalDateTime v) {
        completedAt=v;
    }
    public String getCompletionNotes() {
        return completionNotes;
    }
    public void setCompletionNotes(String v) {
        completionNotes=v;
    }
    public Machine getMachine() {
        return machine;
    }
    public void setMachine(Machine v) {
        machine=v;
    }
    public Technician getTechnician() {
        return technician;
    }
    public void setTechnician(Technician v) {
        technician=v;
    }
}
