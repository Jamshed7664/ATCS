package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ScheduleLocationZoneIncidentKey implements Serializable {

	private Long scheduleId;
	private Integer zoneNumber;
	private String incidentName;

	public ScheduleLocationZoneIncidentKey() {
		super();
	}

	public ScheduleLocationZoneIncidentKey(Long scheduleId, Integer zoneNumber, String incidentName) {
		super();
		this.scheduleId = scheduleId;
		this.zoneNumber = zoneNumber;
		this.incidentName = incidentName;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public Integer getZoneNumber() {
		return zoneNumber;
	}

	public void setZoneNumber(Integer zoneNumber) {
		this.zoneNumber = zoneNumber;
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
		result = prime * result + ((scheduleId == null) ? 0 : scheduleId.hashCode());
		result = prime * result + ((zoneNumber == null) ? 0 : zoneNumber.hashCode());
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
		ScheduleLocationZoneIncidentKey other = (ScheduleLocationZoneIncidentKey) obj;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
			return false;
		if (zoneNumber == null) {
			if (other.zoneNumber != null)
				return false;
		} else if (!zoneNumber.equals(other.zoneNumber))
			return false;
		if (incidentName == null) {
			if (other.incidentName != null)
				return false;
		} else if (!incidentName.equals(other.incidentName))
			return false;
		return true;
	}
}
