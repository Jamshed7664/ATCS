package com.nxtlife.efkon.enforcementconfigurator.view.arm;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentGroupByResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class ArmResponse implements Response {

	@Schema(description = "Id of the arm", example = "1")
	private String id;

	@Schema(description = "Name of the arm", example = "ARM00001")
	private String name;
	
	@Schema(description = "Direction of the arm", example = "ABC")
	private String direction;

	@Schema(description = "Lanes in that arm")
	private List<LaneResponse> lanes;

	@Schema(description = "Detail of equipments mapped with this location. This detail will be fetch when you will fetch equipmennt mappings")
	private List<EquipmentGroupByResponse> equipments;
	
	public ArmResponse(String id, String name, String direction) {
		super();
		this.id = id;
		this.name = name;
		this.direction = direction;
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

	public List<LaneResponse> getLanes() {
		return lanes;
	}

	public void setLanes(List<LaneResponse> lanes) {
		this.lanes = lanes;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public List<EquipmentGroupByResponse> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentGroupByResponse> equipments) {
		this.equipments = equipments;
	}

	public static ArmResponse get(Arm arm) {
		if (arm != null) {
			ArmResponse armResponse = new ArmResponse(arm.getId(), arm.getName(), arm.getDirection());
			return armResponse;
		}
		return null;
	}
}
