package com.example.maintainit.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
@Entity
@Table(name="machines") public class Machine  {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank private String name;
    private String model;
    @NotNull
    @Enumerated(EnumType.STRING) private IntervalType maintenanceIntervalType;
    @NotNull
    @Positive private Integer maintenanceIntervalValue;
    @PositiveOrZero private Double currentUsageHours=0.0;
    private Double usageHoursAtLastMaintenance=0.0;
    private LocalDateTime lastMaintenanceDate;
    public Machine() {
    }
    public Long getId() {
        return id;
    }
    public void setId(Long v) {
        id=v;
    }
    public String getName() {
        return name;
    }
    public void setName(String v) {
        name=v;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String v) {
        model=v;
    }
    public IntervalType getMaintenanceIntervalType() {
        return maintenanceIntervalType;
    }
    public void setMaintenanceIntervalType(IntervalType v) {
        maintenanceIntervalType=v;
    }
    public Integer getMaintenanceIntervalValue() {
        return maintenanceIntervalValue;
    }
    public void setMaintenanceIntervalValue(Integer v) {
        maintenanceIntervalValue=v;
    }
    public Double getCurrentUsageHours() {
        return currentUsageHours;
    }
    public void setCurrentUsageHours(Double v) {
        currentUsageHours=v;
    }
    public Double getUsageHoursAtLastMaintenance() {
        return usageHoursAtLastMaintenance;
    }
    public void setUsageHoursAtLastMaintenance(Double v) {
        usageHoursAtLastMaintenance=v;
    }
    public LocalDateTime getLastMaintenanceDate() {
        return lastMaintenanceDate;
    }
    public void setLastMaintenanceDate(LocalDateTime v) {
        lastMaintenanceDate=v;
    }
}
