package com.nxtlife.efkon.enforcementconfigurator.entity.schedule;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.ScheduleLocationZoneIncidentKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;

@SuppressWarnings("serial")
@Entity
@Table(name = "schedule_location_zone_incident")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class ScheduleLocationZoneIncident implements Serializable {

	@EmbeddedId
	private ScheduleLocationZoneIncidentKey scheduleLocationZoneIncidentKey;

	@MapsId("scheduleId")
	@ManyToOne
	private Schedule schedule;

	@MapsId("incidentName")
	@ManyToOne
	private Incident incident;

	public ScheduleLocationZoneIncident() {
		super();
	}

	public ScheduleLocationZoneIncident(ScheduleLocationZoneIncidentKey scheduleLocationZoneIncidentKey,
			Schedule schedule, Incident incident) {
		super();
		this.scheduleLocationZoneIncidentKey = scheduleLocationZoneIncidentKey;
		this.schedule = schedule;
		this.incident = incident;
	}

	public ScheduleLocationZoneIncidentKey getScheduleLocationZoneIncidentKey() {
		return scheduleLocationZoneIncidentKey;
	}

	public void setScheduleLocationZoneIncidentKey(ScheduleLocationZoneIncidentKey scheduleLocationZoneIncidentKey) {
		this.scheduleLocationZoneIncidentKey = scheduleLocationZoneIncidentKey;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Incident getIncident() {
		return incident;
	}

	public void setIncident(Incident incident) {
		this.incident = incident;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
		result = prime * result + ((incident == null) ? 0 : incident.hashCode());
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
		ScheduleLocationZoneIncident other = (ScheduleLocationZoneIncident) obj;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (scheduleLocationZoneIncidentKey == null) {
			if (other.scheduleLocationZoneIncidentKey != null)
				return false;
		} else if (!scheduleLocationZoneIncidentKey.equals(other.scheduleLocationZoneIncidentKey))
			return false;
		return true;
	}
}
