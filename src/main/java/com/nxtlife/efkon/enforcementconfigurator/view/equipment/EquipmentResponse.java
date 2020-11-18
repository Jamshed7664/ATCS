package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class EquipmentResponse extends EquipmentGroupByResponse {

	@Schema(description = "Id of equipment", example = "1")
	private String id;

	@Schema(description = "Name of equipment", example = "RSU 1")
	private String name;

	@Schema(description = "Mapping detail of equipment")
	private EquipmentMappingResponse equipmentMapping;

	public EquipmentResponse(String id, String name, String groupId, String equipmentTypeId, String equipmentTypeCode,
			String equipmentTypeName) {
		super(groupId, equipmentTypeId, equipmentTypeCode, equipmentTypeName, null);
		this.id = id;
		this.name = name;
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

	public EquipmentMappingResponse getEquipmentMapping() {
		return equipmentMapping;
	}

	public void setEquipmentMapping(EquipmentMappingResponse equipmentMapping) {
		this.equipmentMapping = equipmentMapping;
	}

}
