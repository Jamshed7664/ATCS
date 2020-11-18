package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class EquipmentMappingResponse {

	@Schema(description = "Id of the location", example = "LOCANOI0001")
	private String locationId;

	@Schema(description = "name of the location", example = "Sharda")
	private String locationName;

	@Schema(description = "type of location", example = "JUNCTION")
	private String locationTypeName;

	@Schema(description = "Id of the arm", example = "ARM1LOCANOI0001")
	private String armId;

	@Schema(description = "name of arm", example = "ARM1")
	private String armName;

	@Schema(description = "Id of the lane", example = "L1A1LOCANOI0001")
	private String laneId;

	@Schema(description = "name of lane", example = "LANE1")
	private String laneName;

	@Schema(description = "Group id of equipment", example = "EQPGRP00001")
	private String groupId;

	@Schema(description = "Equipment id")
	private String equipmentId;

	@Schema(description = "Equipment name")
	private String equipmentName;

	@Schema(description = "Equipment count")
	private Long count;

	public EquipmentMappingResponse(String locationId, String armId, String laneId, String equipmentGroupId,
			String equipmentId, String equipmentName) {
		super();
		this.locationId = locationId;
		this.armId = armId;
		this.laneId = laneId;
		this.groupId = equipmentGroupId;
		this.equipmentId = equipmentId;
		this.equipmentName = equipmentName;
	}

	public static EquipmentMappingResponse get(String locationId, String armId, String laneId, String groupId,
			Long count) {
		EquipmentMappingResponse response = new EquipmentMappingResponse(locationId, armId, laneId, groupId, null,
				null);
		response.count = count;
		return response;
	}

	public String getLocationId() {
		return locationId;
	}

	public void setLocationId(String locationId) {
		this.locationId = locationId;
	}

	public String getArmId() {
		return armId;
	}

	public void setArmId(String armId) {
		this.armId = armId;
	}

	public String getLaneId() {
		return laneId;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	public String getLocationTypeName() {
		return locationTypeName;
	}

	public void setLocationTypeName(String locationTypeName) {
		this.locationTypeName = locationTypeName;
	}

	public String getArmName() {
		return armName;
	}

	public void setArmName(String armName) {
		this.armName = armName;
	}

	public String getLaneName() {
		return laneName;
	}

	public void setLaneName(String laneName) {
		this.laneName = laneName;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
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

}
