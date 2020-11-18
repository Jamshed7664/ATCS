package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ZoneIncidentKey implements Serializable {

	private Long zoneId;
	private String incidentName;

	public ZoneIncidentKey() {
	}

	public ZoneIncidentKey(Long zoneId, String incidentName) {
		this.zoneId = zoneId;
		this.incidentName = incidentName;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public String getIncidentName() {
		return incidentName;
	}

	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((zoneId == null) ? 0 : zoneId.hashCode());
		result = prime * result + ((incidentName == null) ? 0 : incidentName.hashCode());
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
		ZoneIncidentKey other = (ZoneIncidentKey) obj;
		if (zoneId == null) {
			if (other.zoneId != null)
				return false;
		} else if (!zoneId.equals(other.zoneId))
			return false;
		if (incidentName == null) {
			if (other.incidentName != null)
				return false;
		} else if (!incidentName.equals(other.incidentName))
			return false;

		return true;
	}
}
