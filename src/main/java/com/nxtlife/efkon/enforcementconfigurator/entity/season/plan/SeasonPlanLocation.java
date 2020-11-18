package com.nxtlife.efkon.enforcementconfigurator.entity.season.plan;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.SeasonPlanLocationKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "season_plan_location")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class SeasonPlanLocation implements Serializable {

    @EmbeddedId
    private SeasonPlanLocationKey seasonPlanLocationKey;

    @MapsId("seasonPlanId")
    @ManyToOne
    private SeasonPlan seasonPlan;

    @MapsId("locationId")
    @ManyToOne
    private Location location;

    public SeasonPlanLocation() {
        super();
    }

    public SeasonPlanLocation(SeasonPlanLocationKey seasonPlanLocationKey, SeasonPlan seasonPlan, Location location) {
        super();
        this.seasonPlanLocationKey = seasonPlanLocationKey;
        this.seasonPlan = seasonPlan;
        this.location = location;
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
        result = prime * result + ((seasonPlan == null) ? 0 : seasonPlan.hashCode());
        result = prime * result + ((location == null) ? 0 : location.hashCode());
        result = prime * result + ((seasonPlanLocationKey == null) ? 0 : seasonPlanLocationKey.hashCode());
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
        SeasonPlanLocation other = (SeasonPlanLocation) obj;
        if (seasonPlan == null) {
            if (other.seasonPlan != null)
                return false;
        } else if (!seasonPlan.equals(other.seasonPlan))
            return false;
        if (location == null) {
            if (other.location != null)
                return false;
        } else if (!location.equals(other.location))
            return false;
        if (seasonPlanLocationKey == null) {
            if (other.seasonPlanLocationKey != null)
                return false;
        } else if (!seasonPlanLocationKey.equals(other.seasonPlanLocationKey))
            return false;
        return true;
    }

}

