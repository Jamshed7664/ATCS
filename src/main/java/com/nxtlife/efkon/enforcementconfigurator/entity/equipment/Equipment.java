package com.nxtlife.efkon.enforcementconfigurator.entity.equipment;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentType;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;

@SuppressWarnings("serial")
@Entity
@Table(name = "equipment_mst")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Equipment extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "group id can't be null")
	private String groupId;

	@ManyToOne
	private EquipmentType equipmentType;

	@Transient
	private String tEquipmentTypeId;

	@ManyToOne
	@NotNull(message = "organization can't be null")
	private Organization organization;

	@Transient
	private Long tOrganizationId;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "equipment")
	private Camera camera;
	
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "equipment")
	private EquipmentMapping equipmentMapping;

	public Equipment() {
		super();
	}

	public Equipment(String id, @NotNull(message = "name can't be null") String name,
			@NotNull(message = "group id can't be null") String groupId, String tEquipmentTypeId,
			Long tOrganizationId) {
		super();
		this.id = id;
		this.name = name;
		this.groupId = groupId;
		this.settEquipmentTypeId(tEquipmentTypeId);
		this.settOrganizationId(tOrganizationId);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public String gettEquipmentTypeId() {
		return tEquipmentTypeId;
	}

	public void settEquipmentTypeId(String tEquipmentTypeId) {
		if (tEquipmentTypeId != null) {
			this.equipmentType = new EquipmentType();
			this.equipmentType.setId(tEquipmentTypeId);
		}
		this.tEquipmentTypeId = tEquipmentTypeId;
	}

	public Long gettOrganizationId() {
		return tOrganizationId;
	}

	public void settOrganizationId(Long tOrganizationId) {
		if (tOrganizationId != null) {
			this.organization = new Organization();
			this.organization.setId(tOrganizationId);
		}
		this.tOrganizationId = tOrganizationId;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public EquipmentMapping getEquipmentMapping() {
		return equipmentMapping;
	}

	public void setEquipmentMapping(EquipmentMapping equipmentMapping) {
		this.equipmentMapping = equipmentMapping;
	}

}
