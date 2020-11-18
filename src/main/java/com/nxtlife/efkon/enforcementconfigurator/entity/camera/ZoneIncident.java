package com.nxtlife.efkon.enforcementconfigurator.entity.camera;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.ZoneIncidentKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;

@SuppressWarnings("serial")
@Entity
@Table(name = "zone_incident")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class ZoneIncident implements Serializable {

	@EmbeddedId
	private ZoneIncidentKey zoneIncidentKey;

	@MapsId("zoneId")
	@ManyToOne
	@JoinColumn(name = "zone_id")
	private CameraImageCoordinate cameraImageCoordinate;

	@MapsId("incidentName")
	@ManyToOne
	private Incident incident;

	public ZoneIncidentKey getZoneIncidentKey() {
		return zoneIncidentKey;
	}

	public void setZoneIncidentKey(ZoneIncidentKey zoneIncidentKey) {
		this.zoneIncidentKey = zoneIncidentKey;
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
		result = prime * result + ((cameraImageCoordinate == null) ? 0 : cameraImageCoordinate.hashCode());
		result = prime * result + ((incident == null) ? 0 : incident.hashCode());
		result = prime * result + ((zoneIncidentKey == null) ? 0 : zoneIncidentKey.hashCode());
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
		ZoneIncident other = (ZoneIncident) obj;
		if (cameraImageCoordinate == null) {
			if (other.cameraImageCoordinate != null)
				return false;
		} else if (!cameraImageCoordinate.equals(other.cameraImageCoordinate))
			return false;
		if (incident == null) {
			if (other.incident != null)
				return false;
		} else if (!incident.equals(other.incident))
			return false;
		if (zoneIncidentKey == null) {
			if (other.zoneIncidentKey != null)
				return false;
		} else if (!zoneIncidentKey.equals(other.zoneIncidentKey))
			return false;
		return true;
	}

}
