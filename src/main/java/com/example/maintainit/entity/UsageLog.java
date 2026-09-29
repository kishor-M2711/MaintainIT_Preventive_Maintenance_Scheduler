package com.example.maintainit.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name="usage_logs") public class UsageLog  {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    private LocalDateTime loggedAt;
    @NotNull
    @Positive private Double hoursUsed;
    @ManyToOne(optional=false) private Machine machine;
    public UsageLog() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public LocalDateTime getLoggedAt() {
        return loggedAt;
    }
    public void setLoggedAt(LocalDateTime v) {
        loggedAt=v;
    }
    public Double getHoursUsed() {
        return hoursUsed;
    }
    public void setHoursUsed(Double v) {
        hoursUsed=v;
    }
    public Machine getMachine() {
        return machine;
    }
    public void setMachine(Machine v) {
        machine=v;
    }
}
