package com.nxtlife.efkon.enforcementconfigurator.entity.incident;

import static com.nxtlife.efkon.enforcementconfigurator.service.BaseService.getUser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraIncident;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.ZoneIncident;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;
import com.nxtlife.efkon.enforcementconfigurator.util.DateUtil;

@SuppressWarnings("serial")
@Entity
@Table(name = "incident_fx")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Incident implements Serializable {

	@Id
	private String name;

	private Boolean active;

	private Integer locationRelated;

	private Integer locationTypeRelated;

	@NotNull
	@Enumerated(EnumType.STRING)
	private IncidentType type;

	@NotNull(message = "created_at can't be null")
	@Column(name = "created_at")
	private String createdAt;

	@Column(name = "modified_at")
	private String modifiedAt;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "modified_by")
	private User modifiedBy;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incident")
	private List<CameraIncident> cameraIncidents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incident")
	private List<ZoneIncident> zoneIncidents = new ArrayList<>();

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "incident")
	private List<IncidentAttributeIncident> incidentAttributeIncidents;

	/**
	 * this method will work whenever we call save method in repository. It will
	 * set following property
	 *
	 * <ul>
	 * <li>createdAt to current date and time</li>
	 * <li>active default value (true)</li>
	 * <li>createdBy to whoever user is login it will set according to that</li>
	 * </ul>
	 *
	 */
	@PrePersist
	protected void prePersist() {
		this.createdAt = DateUtil.today();
		this.active = true;
		Long userId;
		if ((userId = getUser().getUserId()) != null) {
			this.createdBy = new User();
			this.createdBy.setId(userId);
		}
	}

	/**
	 * this method will work whenever we call update method in repository. It
	 * will set following property
	 *
	 * <ul>
	 * <li>createdAt to current date and time</li>
	 * <li>active default value (true)</li>
	 * <li>createdBy to whoever user is login it will set according to that</li>
	 * </ul>
	 *
	 */
	@PreUpdate
	protected void preUpdate() {
		this.createdAt = DateUtil.today();
		this.active = true;
		Long userId;
		if ((userId = getUser().getUserId()) != null) {
			this.createdBy = new User();
			this.createdBy.setId(userId);
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public IncidentType getType() {
		return type;
	}

	public void setIncidentType(IncidentType type) {
		this.type = type;
	}

	public Integer getLocationRelated() {
		return locationRelated;
	}

	public void setLocationRelated(Integer locationRelated) {
		this.locationRelated = locationRelated;
	}

	public Integer getLocationTypeRelated() {
		return locationTypeRelated;
	}

	public void setLocationTypeRelated(Integer locationTypeRelated) {
		this.locationTypeRelated = locationTypeRelated;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}

	public String getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public List<CameraIncident> getCameraIncidents() {
		return cameraIncidents;
	}

	public void setCameraIncidents(List<CameraIncident> cameraIncidents) {
		this.cameraIncidents = cameraIncidents;
	}

	public void setType(IncidentType type) {
		this.type = type;
	}

	public List<ZoneIncident> getZoneIncidents() {
		return zoneIncidents;
	}

	public void setZoneIncidents(List<ZoneIncident> zoneIncidents) {
		this.zoneIncidents = zoneIncidents;
	}

	public List<IncidentAttributeIncident> getIncidentAttributeIncidents() {
		return incidentAttributeIncidents;
	}

	public void setIncidentAttributeIncidents(List<IncidentAttributeIncident> incidentAttributeIncidents) {
		this.incidentAttributeIncidents = incidentAttributeIncidents;
	}
}
