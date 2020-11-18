package com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.StringSetConverter;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.EquipmentAttributeValue;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;

@SuppressWarnings("serial")
@Entity
@Table(name = "equipment_type_attribute_mst")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class EquipmentTypeAttribute extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "data type can't be null")
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	@NotNull(message = "specific can't be null")
	@Column(columnDefinition = "boolean default false")
	private Boolean isSpecific;

	@Convert(converter = StringSetConverter.class)
	@Column(length = 1000)
	private Set<String> options;

	@NotNull(message = "fixed can't be null")
	private Boolean fixed;

	@NotNull(message = "equipment type can't be null")
	@ManyToOne
	private EquipmentType equipmentType;

	@NotNull(message = "organization can't be null")
	@ManyToOne
	private Organization organization;

	@Transient
	private String tEquipmentTypeId;

	@Transient
	private Long tOrganizationId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "equipmentTypeAttribute")
	private List<EquipmentAttributeValue> equipmentTypeAttributeValues;

	public EquipmentTypeAttribute() {
		super();
	}

	public EquipmentTypeAttribute(@NotNull(message = "id can't be null") String id,
			@NotNull(message = "name can't be null") String name,
			@NotNull(message = "data type can't be null") DataType dataType, Boolean fixed, Boolean isSpecific,String tEquipmentTypeId,
			Long tOrganizationId) {
		super();
		this.setId(id);
		this.name = name;
		this.dataType = dataType;
		this.fixed = fixed;
		this.isSpecific=isSpecific;
		if (tOrganizationId != null) {
			this.organization = new Organization();
			this.organization.setId(tOrganizationId);
		}
		if (tEquipmentTypeId != null) {
			this.equipmentType = new EquipmentType();
			this.equipmentType.setId(tEquipmentTypeId);
		}
		this.tOrganizationId = tOrganizationId;
		this.tEquipmentTypeId = tEquipmentTypeId;
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

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Boolean getIsSpecific() {
		return isSpecific;
	}

	public void setIsSpecific(Boolean isSpecific) {
		this.isSpecific = isSpecific;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
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

	public Organization getOrganization() {
		return organization;
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

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public List<EquipmentAttributeValue> getEquipmentTypeAttributeValues() {
		return equipmentTypeAttributeValues;
	}

	public void setEquipmentTypeAttributeValues(List<EquipmentAttributeValue> equipmentTypeAttributeValues) {
		this.equipmentTypeAttributeValues = equipmentTypeAttributeValues;
	}
}
