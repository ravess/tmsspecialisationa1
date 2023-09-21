package com.tms.a1.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "task")
public class Task {
    @Id
    @NotNull(message = "Task id should not be null")
    @NotBlank
    @Column(name = "task_id", unique = true)
    private String taskID;

    // @NotNull(message = "Task name should not be null")
    // @NotBlank
    // @Column(name = "task_name")
    // private String taskName;

    // @NotNull(message = "RNumber should not be null")
    // @Column(name = "task_", unique = true)
    // private long taskDescription;

    // @Column(name = "app_start_date")
    // private String taskNotes;

    // @Column(name = "app_end_date")
    // private int taskPlan;

    // @Column(name = "app_permit_open")
    // private String taskAppAcronym;

    // @Column(name = "app_permit_open")
    // private String taskState;

    // @Column(name = "app_permit_open")
    // private String taskCreator;

    // @Column(name = "app_permit_open")
    // private String taskOwner;

    // @Column(name = "app_permit_open")
    // private String taskCreateDate;
}

