package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class DayPlanLocationKey implements Serializable {

	private Long dayPlanId;
	private String locationId;

	public DayPlanLocationKey() {
		super();
	}

	public DayPlanLocationKey(Long dayPlanId, String locationId) {
		super();
		this.dayPlanId = dayPlanId;
		this.locationId = locationId;
	}

	public Long getDayPlanId() {
		return dayPlanId;
	}

	public void setDayPlanId(Long dayPlanId) {
		this.dayPlanId = dayPlanId;
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
		result = prime * result + ((dayPlanId == null) ? 0 : dayPlanId.hashCode());
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
		DayPlanLocationKey other = (DayPlanLocationKey) obj;
		if (dayPlanId == null) {
			if (other.dayPlanId != null)
				return false;
		} else if (!dayPlanId.equals(other.dayPlanId))
			return false;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;

		return true;
	}

}
