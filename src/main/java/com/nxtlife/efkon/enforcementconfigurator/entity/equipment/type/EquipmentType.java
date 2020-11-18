package com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.Equipment;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;

@SuppressWarnings("serial")
@Entity
@Table(name = "equipment_type_fx", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "code", "organization_id" }) })
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class EquipmentType extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "code can't be null")
	private String code;

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "quantity can't be null")
	private Integer quantity;

	@ManyToOne
	private Organization organization;
	
	@Transient
	private Long tOrganizationId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentType")
	private List<Equipment> equipments;

	public EquipmentType() {
		super();
	}

	public EquipmentType(String id, @NotNull String code, @NotNull String name, Integer quantity, Long organizationId) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.quantity=  quantity;
		if (organizationId != null) {
			this.organization = new Organization();
			this.organization.setId(organizationId);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Long gettOrganizationId() {
		return tOrganizationId;
	}

	public void settOrganizationId(Long tOrganizationId) {
		if(tOrganizationId!=null) {
			this.organization = new Organization();
			this.organization.setId(tOrganizationId);
		}
		this.tOrganizationId = tOrganizationId;
	}

	public List<Equipment> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<Equipment> equipments) {
		this.equipments = equipments;
	}

}
