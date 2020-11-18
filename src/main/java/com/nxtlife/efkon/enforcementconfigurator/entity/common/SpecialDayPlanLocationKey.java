package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class SpecialDayPlanLocationKey implements Serializable {

	private Long specialDayPlanId;
	private String locationId;

	public SpecialDayPlanLocationKey() {
		super();
	}

	public SpecialDayPlanLocationKey(Long specialDayPlanId, String locationId) {
		super();
		this.specialDayPlanId = specialDayPlanId;
		this.locationId = locationId;
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
		result = prime * result + ((specialDayPlanId == null) ? 0 : specialDayPlanId.hashCode());
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
		SpecialDayPlanLocationKey other = (SpecialDayPlanLocationKey) obj;
		if (specialDayPlanId == null) {
			if (other.specialDayPlanId != null)
				return false;
		} else if (!specialDayPlanId.equals(other.specialDayPlanId))
			return false;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;

		return true;
	}
}
