package com.nxtlife.efkon.enforcementconfigurator.entity.schedule;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.ScheduleLocationKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "schedule_location")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class ScheduleLocation implements Serializable {

	@EmbeddedId
	private ScheduleLocationKey scheduleLocationKey;

	@MapsId("scheduleId")
	@ManyToOne
	private Schedule schedule;

	@MapsId("locationId")
	@ManyToOne
	private Location location;

	public ScheduleLocation() {
		super();
	}

	public ScheduleLocation(ScheduleLocationKey scheduleLocationKey, Schedule schedule, Location location) {
		super();
		this.scheduleLocationKey = scheduleLocationKey;
		this.schedule = schedule;
		this.location = location;
	}

	public ScheduleLocationKey getScheduleLocationKey() {
		return scheduleLocationKey;
	}

	public void setScheduleLocationKey(ScheduleLocationKey scheduleLocationKey) {
		this.scheduleLocationKey = scheduleLocationKey;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((schedule == null) ? 0 : schedule.hashCode());
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((scheduleLocationKey == null) ? 0 : scheduleLocationKey.hashCode());
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
		ScheduleLocation other = (ScheduleLocation) obj;
		if (schedule == null) {
			if (other.schedule != null)
				return false;
		} else if (!schedule.equals(other.schedule))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (scheduleLocationKey == null) {
			if (other.scheduleLocationKey != null)
				return false;
		} else if (!scheduleLocationKey.equals(other.scheduleLocationKey))
			return false;
		return true;
	}

}
