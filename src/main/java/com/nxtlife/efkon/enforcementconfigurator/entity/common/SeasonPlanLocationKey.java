package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class SeasonPlanLocationKey implements Serializable {

	private Long seasonPlanId;
	private String locationId;

	public SeasonPlanLocationKey() {
		super();
	}

	public SeasonPlanLocationKey(Long seasonPlanId, String locationId) {
		super();
		this.seasonPlanId = seasonPlanId;
		this.locationId = locationId;
	}

	public Long getSeasonPlanId() {
		return seasonPlanId;
	}

	public void setSeasonPlanId(Long seasonPlanId) {
		this.seasonPlanId = seasonPlanId;
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
		result = prime * result + ((seasonPlanId == null) ? 0 : seasonPlanId.hashCode());
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
		SeasonPlanLocationKey other = (SeasonPlanLocationKey) obj;
		if (seasonPlanId == null) {
			if (other.seasonPlanId != null)
				return false;
		} else if (!seasonPlanId.equals(other.seasonPlanId))
			return false;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;

		return true;
	}
}
