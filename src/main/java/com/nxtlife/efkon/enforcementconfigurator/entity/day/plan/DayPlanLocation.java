package com.nxtlife.efkon.enforcementconfigurator.entity.day.plan;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.DayPlanLocationKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "day_plan_location")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class DayPlanLocation implements Serializable {

	@EmbeddedId
	private DayPlanLocationKey dayPlanLocationKey;

	@MapsId("dayPlanId")
	@ManyToOne
	private DayPlan dayPlan;

	@MapsId("locationId")
	@ManyToOne
	private Location location;

	public DayPlanLocation() {
		super();
	}

	public DayPlanLocation(DayPlanLocationKey dayPlanLocationKey, DayPlan dayPlan, Location location) {
		super();
		this.dayPlanLocationKey = dayPlanLocationKey;
		this.dayPlan = dayPlan;
		this.location = location;
	}

	public DayPlanLocationKey getDayPlanLocationKey() {
		return dayPlanLocationKey;
	}

	public void setDayPlanLocationKey(DayPlanLocationKey dayPlanLocationKey) {
		this.dayPlanLocationKey = dayPlanLocationKey;
	}

	public DayPlan getDayPlan() {
		return dayPlan;
	}

	public void setDayPlan(DayPlan dayPlan) {
		this.dayPlan = dayPlan;
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
		result = prime * result + ((dayPlan == null) ? 0 : dayPlan.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((dayPlanLocationKey == null) ? 0 : dayPlanLocationKey.hashCode());
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
		DayPlanLocation other = (DayPlanLocation) obj;
		if (dayPlan == null) {
			if (other.dayPlan != null)
				return false;
		} else if (!dayPlan.equals(other.dayPlan))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (dayPlanLocationKey == null) {
			if (other.dayPlanLocationKey != null)
				return false;
		} else if (!dayPlanLocationKey.equals(other.dayPlanLocationKey))
			return false;
		return true;
	}

}
