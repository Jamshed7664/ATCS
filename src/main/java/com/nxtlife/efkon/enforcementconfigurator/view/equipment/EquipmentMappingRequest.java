package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class EquipmentMappingRequest implements Request {

	@Schema(description = "Id of the arm", example = "A1LOCANOI000001")
	private String armId;

	@Schema(description = "Id of the lane", example = "L1A1LOCANOI000001")
	private String laneId;

	@Schema(description = "Id of the equipment group", example = "EQTMSTGRP0001")
	@NotEmpty(message = "equipment group id can't be  null")
	private String equipmentGroupId;

	@Schema(description = "If user want to map specific equipment then it will be used", example = "[EQP0000001]")
	private List<String> equipmentIds;

	@Schema(description = "Equipment count", example = "LOCANOI000001")
	@NotNull(message = "count can't be null")
	@Min(value = 1, message = "count should be greater than 1")
	private Long count;

	public String getArmId() {
		return armId;
	}

	public void setArmId(String armId) {
		this.armId = armId;
	}

	public String getLaneId() {
		return laneId;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public String getEquipmentGroupId() {
		return equipmentGroupId;
	}

	public void setEquipmentGroupId(String equipmentGroupId) {
		this.equipmentGroupId = equipmentGroupId;
	}

	public List<String> getEquipmentIds() {
		return equipmentIds;
	}

	public void setEquipmentIds(List<String> equipmentIds) {
		this.equipmentIds = equipmentIds;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}
}
