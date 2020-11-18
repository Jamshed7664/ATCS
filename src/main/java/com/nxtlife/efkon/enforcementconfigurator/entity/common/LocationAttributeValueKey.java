package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class LocationAttributeValueKey implements Serializable {

	private String locationTypeFieldId;
	private String locationId;

	public LocationAttributeValueKey() {
	}

	public LocationAttributeValueKey(String locationTypeFieldId, String locationId) {
		this.locationTypeFieldId = locationTypeFieldId;
		this.locationId = locationId;
	}

	public String getLocationTypeFieldId() {
		return locationTypeFieldId;
	}

	public void setLocationTypeFieldId(String locationTypeFieldId) {
		this.locationTypeFieldId = locationTypeFieldId;
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
		result = prime * result + ((locationId == null) ? 0 : locationId.hashCode());
		result = prime * result + ((locationTypeFieldId == null) ? 0 : locationTypeFieldId.hashCode());
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
		LocationAttributeValueKey other = (LocationAttributeValueKey) obj;
		if (locationId == null) {
			if (other.locationId != null)
				return false;
		} else if (!locationId.equals(other.locationId))
			return false;
		if (locationTypeFieldId == null) {
			if (other.locationTypeFieldId != null)
				return false;
		} else if (!locationTypeFieldId.equals(other.locationTypeFieldId))
			return false;

		return true;
	}
}
