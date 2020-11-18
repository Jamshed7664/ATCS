package com.nxtlife.efkon.enforcementconfigurator.entity.week.plan;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.WeekPlanLocationKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "week_plan_location")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class WeekPlanLocation implements Serializable {

	@EmbeddedId
	private WeekPlanLocationKey weekPlanLocationKey;

	@MapsId("weekPlanId")
	@ManyToOne
	private WeekPlan weekPlan;

	@MapsId("locationId")
	@ManyToOne
	private Location location;

	public WeekPlanLocation() {
		super();
	}

	public WeekPlanLocation(WeekPlanLocationKey weekPlanLocationKey, WeekPlan weekPlan, Location location) {
		super();
		this.weekPlanLocationKey = weekPlanLocationKey;
		this.weekPlan = weekPlan;
		this.location = location;
	}

	public WeekPlanLocationKey getWeekPlanLocationKey() {
		return weekPlanLocationKey;
	}

	public void setWeekPlanLocationKey(WeekPlanLocationKey weekPlanLocationKey) {
		this.weekPlanLocationKey = weekPlanLocationKey;
	}

	public WeekPlan getWeekPlan() {
		return weekPlan;
	}

	public void setWeekPlan(WeekPlan weekPlan) {
		this.weekPlan = weekPlan;
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
		result = prime * result + ((weekPlan == null) ? 0 : weekPlan.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((weekPlanLocationKey == null) ? 0 : weekPlanLocationKey.hashCode());
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
		WeekPlanLocation other = (WeekPlanLocation) obj;
		if (weekPlan == null) {
			if (other.weekPlan != null)
				return false;
		} else if (!weekPlan.equals(other.weekPlan))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (weekPlanLocationKey == null) {
			if (other.weekPlanLocationKey != null)
				return false;
		} else if (!weekPlanLocationKey.equals(other.weekPlanLocationKey))
			return false;
		return true;
	}
}
