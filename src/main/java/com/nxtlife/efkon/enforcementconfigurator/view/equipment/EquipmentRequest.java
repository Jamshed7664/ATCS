package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

public class EquipmentRequest implements Request {

	@NotNull(message = "equipment type id can't be null")
	private String typeId;

	@Valid
	@NotEmpty(message = "equipment attributes value can't be empty")
	private List<EquipmentAttributeValueRequest> attributesValue;

	public String getTypeId() {
		return typeId;
	}

	public List<EquipmentAttributeValueRequest> getAttributesValue() {
		return attributesValue;
	}

}
