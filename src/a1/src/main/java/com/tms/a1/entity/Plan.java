package com.tms.a1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Table(name = "plan")
public class Plan {
    @Id
    // @NotNull(message = "plan MVP name should not be null")
    // @NotBlank
    @Column(name = "plan_mvp_name", unique = true)
    private String planMVPName;

    @Column(name = "plan_start_date")
    private String planStartDate;

    @Column(name = "plan_end_date")
    private String planEndDate;

    // @NotNull(message = "Plan App Acronym should not be null")
    // @NotBlank
    @Column(name = "plan_app_acronym")
    private String planAppAcronym;

    @Column(name = "plan_color")
    private String planColor;
    
}
