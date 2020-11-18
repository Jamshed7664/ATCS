package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class EquipmentAttributeValueKey implements Serializable {

	private Long equipmentTypeFieldId;
	private Long equipmentId;

	public EquipmentAttributeValueKey() {
	}

	public EquipmentAttributeValueKey(Long equipmentTypeFieldId, Long equipmentId) {
		this.equipmentTypeFieldId = equipmentTypeFieldId;
		this.equipmentId = equipmentId;
	}

	public Long getEquipmentTypeFieldId() {
		return equipmentTypeFieldId;
	}

	public void setEquipmentTypeFieldId(Long equipmentTypeFieldId) {
		this.equipmentTypeFieldId = equipmentTypeFieldId;
	}

	public Long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(Long equipmentId) {
		this.equipmentId = equipmentId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((equipmentTypeFieldId == null) ? 0 : equipmentTypeFieldId.hashCode());
		result = prime * result + ((equipmentId == null) ? 0 : equipmentId.hashCode());
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
		EquipmentAttributeValueKey other = (EquipmentAttributeValueKey) obj;

		if (equipmentTypeFieldId == null) {
			if (other.equipmentTypeFieldId != null)
				return false;
		} else if (!equipmentTypeFieldId.equals(other.equipmentTypeFieldId))
			return false;
		if (equipmentId == null) {
			if (other.equipmentId != null)
				return false;
		} else if (!equipmentId.equals(other.equipmentId))
			return false;
		return true;
	}
}
