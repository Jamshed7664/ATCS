package com.nxtlife.efkon.enforcementconfigurator.entity.equipment;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentTypeAttribute;

@SuppressWarnings("serial")
@Entity
@Table(name = "equipment_attribute_value", uniqueConstraints = @UniqueConstraint(columnNames = {
		"equipment_type_attribute_id", "equipment_id" }))
public class EquipmentAttributeValue extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "equipment type attribute can't be null")
	@ManyToOne
	private EquipmentTypeAttribute equipmentTypeAttribute;

	@NotNull(message = "equipment can't be null")
	@ManyToOne
	private Equipment equipment;

	@NotEmpty(message="value can't be empty")
	private String value;

	public EquipmentAttributeValue() {
		super();
	}

	public EquipmentAttributeValue(String id, String equipmentTypeAttributeId, String equipmentId, String value){
		super();
		if(equipmentTypeAttributeId!=null){
			this.equipmentTypeAttribute = new EquipmentTypeAttribute();
			this.equipmentTypeAttribute.setId(equipmentTypeAttributeId);
		}
		if(equipmentId!=null){
			this.equipment = new Equipment();
			this.equipment.setId(equipmentId);
		}
		this.id = id;
		this.value = value;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public EquipmentTypeAttribute getEquipmentTypeAttribute() {
		return equipmentTypeAttribute;
	}

	public void setEquipmentTypeAttribute(EquipmentTypeAttribute equipmentTypeAttribute) {
		this.equipmentTypeAttribute = equipmentTypeAttribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
