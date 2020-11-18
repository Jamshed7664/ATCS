package com.nxtlife.efkon.enforcementconfigurator.entity.incident;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraIncident;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;

@SuppressWarnings("serial")
@Entity
@Table(name = "incident_attribute")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class IncidentAttribute extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "data type can't be null")
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	private String unit;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incidentAttribute")
	private List<CameraIncident> cameraIncidents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incidentAttribute")
	private List<IncidentAttributeIncident> incidentAttributeIncidents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public List<CameraIncident> getCameraIncidents() {
		return cameraIncidents;
	}

	public void setCameraIncidents(List<CameraIncident> cameraIncidents) {
		this.cameraIncidents = cameraIncidents;
	}
}
