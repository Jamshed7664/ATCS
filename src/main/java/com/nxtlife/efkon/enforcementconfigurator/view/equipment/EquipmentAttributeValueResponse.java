package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class EquipmentAttributeValueResponse implements Response {

	@Schema(description = "Equipment attribute detail")
	public EquipmentTypeAttributeResponse attribute;

	@Schema(description = "Value of equipment attribute", example = "1")
	public String value;

	@Schema(description = "Is mapped with location")
	public String mappedId;

	@Schema(description = "Specific equipment values for equipment type attribute")
	private List<EquipmentAttributeValueResponse> attributeValues;

	@Schema(description = "Equipment id will be set when attribute value is specific for equipment")
	private String equipmentId;

	@Schema(description = "Equipment name will be set when attribute value is specific for equipment")
	private String equipmentName;

	public EquipmentAttributeValueResponse(EquipmentTypeAttributeResponse attribute, String value) {
		super();
		this.attribute = attribute;
		this.value = value;
	}

	public EquipmentAttributeValueResponse(EquipmentTypeAttributeResponse attribute,
			List<EquipmentAttributeValueResponse> attributeValues) {
		super();
		this.attribute = attribute;
		this.attributeValues = attributeValues;
	}

	public EquipmentAttributeValueResponse(String equipmentId, String equipmentName, String value) {
		super();
		this.equipmentId = equipmentId;
		this.equipmentName = equipmentName;
		this.value = value;
	}

	public EquipmentAttributeValueResponse(String equipmentId, String equipmentName, String value, String mappedId) {
		super();
		this.equipmentId = equipmentId;
		this.equipmentName = equipmentName;
		this.value = value;
		this.mappedId = mappedId;
	}

	public EquipmentTypeAttributeResponse getAttribute() {
		return attribute;
	}

	public void setAttribute(EquipmentTypeAttributeResponse attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<EquipmentAttributeValueResponse> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<EquipmentAttributeValueResponse> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getMappedId() {
		return mappedId;
	}

	public void setMappedId(String mappedId) {
		this.mappedId = mappedId;
	}

	

}
