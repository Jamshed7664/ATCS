package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class ScheduleZoneIncidentKey implements Serializable {

	private Long scheduleId;
	private Long zoneId;
	private String incidentName;

	public ScheduleZoneIncidentKey() {
		super();
	}

	public ScheduleZoneIncidentKey(Long scheduleId, Long zoneId, String incidentName) {
		super();
		this.scheduleId = scheduleId;
		this.zoneId = zoneId;
		this.incidentName = incidentName;
	}

	public Long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(Long scheduleId) {
		this.scheduleId = scheduleId;
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
		result = prime * result + ((scheduleId == null) ? 0 : scheduleId.hashCode());
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
		ScheduleZoneIncidentKey other = (ScheduleZoneIncidentKey) obj;
		if (scheduleId == null) {
			if (other.scheduleId != null)
				return false;
		} else if (!scheduleId.equals(other.scheduleId))
			return false;
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
