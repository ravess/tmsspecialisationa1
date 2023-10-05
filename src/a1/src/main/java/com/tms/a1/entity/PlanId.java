package com.tms.a1.entity;

import java.io.Serializable;
import java.util.Objects;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlanId implements Serializable {
    private String planMVPName;

    private String planAppAcronym;

    // default constructor

    public PlanId(String planMVPName, String planAppAcronym) {
        this.planMVPName = planMVPName;
        this.planAppAcronym = planAppAcronym;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlanId accountId = (PlanId) o;
        return Objects.equals(planMVPName, accountId.planMVPName) &&
                Objects.equals(planAppAcronym, accountId.planAppAcronym);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planAppAcronym,planMVPName);
    }
}
