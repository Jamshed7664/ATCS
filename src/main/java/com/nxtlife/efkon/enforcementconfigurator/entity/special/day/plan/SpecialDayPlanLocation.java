package com.nxtlife.efkon.enforcementconfigurator.entity.special.day.plan;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.SpecialDayPlanLocationKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "special_day_plan_location")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class SpecialDayPlanLocation implements Serializable {

    @EmbeddedId
    private SpecialDayPlanLocationKey specialDayPlanLocationKey;

    @MapsId("specialDayPlanId")
    @ManyToOne
    private SpecialDayPlan specialDayPlan;

    @MapsId("locationId")
    @ManyToOne
    private Location location;

    public SpecialDayPlanLocation() {
        super();
    }

    public SpecialDayPlanLocation(SpecialDayPlanLocationKey specialDayPlanLocationKey, SpecialDayPlan specialDayPlan, Location location) {
        super();
        this.specialDayPlanLocationKey = specialDayPlanLocationKey;
        this.specialDayPlan = specialDayPlan;
        this.location = location;
    }

    public SpecialDayPlanLocationKey getSpecialDayPlanLocationKey() {
        return specialDayPlanLocationKey;
    }

    public void setSpecialDayPlanLocationKey(SpecialDayPlanLocationKey specialDayPlanLocationKey) {
        this.specialDayPlanLocationKey = specialDayPlanLocationKey;
    }

    public SpecialDayPlan getSpecialDayPlan() {
        return specialDayPlan;
    }

    public void setSpecialDayPlan(SpecialDayPlan specialDayPlan) {
        this.specialDayPlan = specialDayPlan;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((specialDayPlan == null) ? 0 : specialDayPlan.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((specialDayPlanLocationKey == null) ? 0 : specialDayPlanLocationKey.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SpecialDayPlanLocation other = (SpecialDayPlanLocation) obj;
        if (specialDayPlan == null) {
            if (other.specialDayPlan != null)
                return false;
        } else if (!specialDayPlan.equals(other.specialDayPlan))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (specialDayPlanLocationKey == null) {
            if (other.specialDayPlanLocationKey != null)
                return false;
        } else if (!specialDayPlanLocationKey.equals(other.specialDayPlanLocationKey))
            return false;
        return true;
    }

}
