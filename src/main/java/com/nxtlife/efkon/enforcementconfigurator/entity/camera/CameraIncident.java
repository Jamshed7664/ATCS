package com.nxtlife.efkon.enforcementconfigurator.entity.camera;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttribute;

@SuppressWarnings("serial")
@Entity
@Table(name = "camera_incident", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "camera_id", "incident_name", "incident_attribute_id" }) })
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class CameraIncident extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@NotNull(message = "camera id can't be null")
	private Camera camera;

	@ManyToOne
	@NotNull(message = "incident name can't be null")
	private Incident incident;

	@ManyToOne
	private IncidentAttribute incidentAttribute;

	@Transient
	private String tCameraId;

	@Transient
	private String tIncidentName;

	@Transient
	private Long tIncidentAttributeId;

	public CameraIncident() {
		super();
	}

	public CameraIncident(String tCameraId, String tIncidentName, Long tIncidentAttributeId, String value) {
		super();
		if (tCameraId != null) {
			this.camera = new Camera();
			this.camera.setId(tCameraId);
		}
		this.tCameraId = tCameraId;
		if (tIncidentName != null) {
			this.incident = new Incident();
			this.incident.setName(tIncidentName);
		}
		this.tIncidentName = tIncidentName;
		if (tIncidentAttributeId != null) {
			this.incidentAttribute = new IncidentAttribute();
			this.incidentAttribute.setId(tIncidentAttributeId);
		}
		this.tIncidentAttributeId = tIncidentAttributeId;
		this.value = value;
	}

	private String value;

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public Incident getIncident() {
		return incident;
	}

	public void setIncident(Incident incident) {
		this.incident = incident;
	}

	public IncidentAttribute getIncidentAttribute() {
		return incidentAttribute;
	}

	public void setIncidentAttribute(IncidentAttribute incidentAttribute) {
		this.incidentAttribute = incidentAttribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String gettCameraId() {
		return tCameraId;
	}

	public void settCameraId(String tCameraId) {
		this.tCameraId = tCameraId;
	}

	public String gettIncidentName() {
		return tIncidentName;
	}

	public void settIncidentName(String tIncidentName) {
		this.tIncidentName = tIncidentName;
	}

	public Long gettIncidentAttributeId() {
		return tIncidentAttributeId;
	}

	public void settIncidentAttributeId(Long tIncidentAttributeId) {
		this.tIncidentAttributeId = tIncidentAttributeId;
	}
}
