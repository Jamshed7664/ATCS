package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class EquipmentGroupByResponse implements Response {

	@Schema(description = "Group id of equipment", example = "abc23")
	protected String groupId;

	@Schema(description = "Equipment type id", example = "1")
	protected String equipmentTypeId;

	@Schema(description = "Equipment type code", example = "RSU")
	protected String equipmentTypeCode;

	@Schema(description = "Equipment type name", example = "RSU")
	protected String equipmentTypeName;

	@Schema(description = "Total number of available equipment", example = "170")
	private Long availableCount;

	@Schema(description = "Equipment count")
	private Long count;

	@Schema(description = "Equipment mapping list")
	private List<EquipmentMappingResponse> equipmentMappings;

	@Schema(description = "Attributes and its values if it is assigned")
	protected List<EquipmentAttributeValueResponse> attributeValues;

	@Schema(description = "This attribute used at the time of getting equipment mapping detail")
	private List<EquipmentGroupByResponse> list;

	public EquipmentGroupByResponse(String groupId, String equipmentTypeId, String equipmentTypeCode,
			String equipmentTypeName, Long count) {
		super();
		this.groupId = groupId;
		this.equipmentTypeId = equipmentTypeId;
		this.equipmentTypeCode = equipmentTypeCode;
		this.equipmentTypeName = equipmentTypeName;
		this.count = count;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getEquipmentTypeId() {
		return equipmentTypeId;
	}

	public void setEquipmentTypeId(String equipmentTypeId) {
		this.equipmentTypeId = equipmentTypeId;
	}

	public String getEquipmentTypeCode() {
		return equipmentTypeCode;
	}

	public void setEquipmentTypeCode(String equipmentTypeCode) {
		this.equipmentTypeCode = equipmentTypeCode;
	}

	public String getEquipmentTypeName() {
		return equipmentTypeName;
	}

	public void setEquipmentTypeName(String equipmentTypeName) {
		this.equipmentTypeName = equipmentTypeName;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(Long availableCount) {
		this.availableCount = availableCount;
	}

	public List<EquipmentAttributeValueResponse> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<EquipmentAttributeValueResponse> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public List<EquipmentMappingResponse> getEquipmentMappings() {
		return equipmentMappings;
	}

	public void setEquipmentMappings(List<EquipmentMappingResponse> equipmentMappings) {
		this.equipmentMappings = equipmentMappings;
	}

	public List<EquipmentGroupByResponse> getList() {
		return list;
	}

	public void setList(List<EquipmentGroupByResponse> list) {
		this.list = list;
	}

}
