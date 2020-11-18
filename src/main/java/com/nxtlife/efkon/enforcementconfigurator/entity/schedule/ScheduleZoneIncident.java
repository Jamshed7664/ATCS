package com.nxtlife.efkon.enforcementconfigurator.entity.schedule;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.ScheduleZoneIncidentKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;

@SuppressWarnings("serial")
@Entity
@Table(name = "schedule_zone_incident")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class ScheduleZoneIncident implements Serializable {

	@EmbeddedId
	private ScheduleZoneIncidentKey scheduleZoneIncidentKey;

	@MapsId("scheduleId")
	@ManyToOne
	private Schedule schedule;

	@MapsId("zoneId")
	@ManyToOne
	@JoinColumn(name = "zone_id")
	private CameraImageCoordinate cameraImageCoordinate;

	@MapsId("incidentName")
	@ManyToOne
	private Incident incident;

	public ScheduleZoneIncident() {
		super();
	}

	public ScheduleZoneIncident(ScheduleZoneIncidentKey scheduleZoneIncidentKey, Schedule schedule,
			CameraImageCoordinate cameraImageCoordinate, Incident incident) {
		super();
		this.scheduleZoneIncidentKey = scheduleZoneIncidentKey;
		this.schedule = schedule;
		this.cameraImageCoordinate = cameraImageCoordinate;
		this.incident = incident;
	}

	public ScheduleZoneIncidentKey getScheduleZoneIncidentKey() {
		return scheduleZoneIncidentKey;
	}

	public void setScheduleZoneIncidentKey(ScheduleZoneIncidentKey scheduleZoneIncidentKey) {
		this.scheduleZoneIncidentKey = scheduleZoneIncidentKey;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public CameraImageCoordinate getCameraImageCoordinate() {
		return cameraImageCoordinate;
	}

	public void setCameraImageCoordinate(CameraImageCoordinate cameraImageCoordinate) {
		this.cameraImageCoordinate = cameraImageCoordinate;
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
		result = prime * result + ((cameraImageCoordinate == null) ? 0 : cameraImageCoordinate.hashCode());
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
		ScheduleZoneIncident other = (ScheduleZoneIncident) obj;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (cameraImageCoordinate == null) {
			if (other.cameraImageCoordinate != null)
				return false;
		} else if (!cameraImageCoordinate.equals(other.cameraImageCoordinate))
			return false;
		if (scheduleZoneIncidentKey == null) {
			if (other.scheduleZoneIncidentKey != null)
				return false;
		} else if (!scheduleZoneIncidentKey.equals(other.scheduleZoneIncidentKey))
			return false;
		return true;
	}
}
