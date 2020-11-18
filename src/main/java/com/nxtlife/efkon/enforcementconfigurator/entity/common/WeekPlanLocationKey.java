package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class WeekPlanLocationKey implements Serializable {

	private Long weekPlanId;
	private String locationId;

	public WeekPlanLocationKey() {
		super();
	}

	public WeekPlanLocationKey(Long weekPlanId, String locationId) {
		super();
		this.weekPlanId = weekPlanId;
		this.locationId = locationId;
	}

	public Long getWeekPlanId() {
		return weekPlanId;
	}

	public void setWeekPlanId(Long weekPlanId) {
		this.weekPlanId = weekPlanId;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((weekPlanId == null) ? 0 : weekPlanId.hashCode());
		result = prime * result + ((locationId == null) ? 0 : locationId.hashCode());
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
		WeekPlanLocationKey other = (WeekPlanLocationKey) obj;
		if (weekPlanId == null) {
			if (other.weekPlanId != null)
				return false;
		} else if (!weekPlanId.equals(other.weekPlanId))
			return false;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;

		return true;
	}
}
