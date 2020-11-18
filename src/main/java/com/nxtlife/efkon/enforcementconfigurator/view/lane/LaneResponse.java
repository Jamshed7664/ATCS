package com.nxtlife.efkon.enforcementconfigurator.view.lane;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.direction.DirectionResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentGroupByResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class LaneResponse implements Response {

	@Schema(description = "Id of the lane", example = "1")
	private String id;

	@Schema(description = "Name of the lane", example = "LANE00001")
	private String name;

	@Schema(description = "Arm id for the lane", example = "ARM1ALK00001")
	private String armId;

	@Schema(description = "Directions of the lane", example = "{Left,Right}")
	private List<DirectionResponse> directions;

	@Schema(description = "Detail of equipments mapped with this location. This detail will be fetch when you will fetch equipmennt mappings")
	private List<EquipmentGroupByResponse> equipments;

	@JsonIgnore
	@Schema(description = "Id of the zone coordinate", example = "1")
	private Long cameraImageCoordinateId;

	public LaneResponse(String id, String name, String armId, Long cameraImageCoordinateId) {
		super();
		this.id = id;
		this.name = name;
		this.armId = armId;
		this.cameraImageCoordinateId = cameraImageCoordinateId;
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

	public List<DirectionResponse> getDirections() {
		return directions;
	}

	public void setDirections(List<DirectionResponse> directions) {
		this.directions = directions;
	}

	public String getArmId() {
		return armId;
	}

	public void setArmId(String armId) {
		this.armId = armId;
	}


	public List<EquipmentGroupByResponse> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentGroupByResponse> equipments) {
		this.equipments = equipments;
	}

	public Long getCameraImageCoordinateId() {
		return cameraImageCoordinateId;
	}

	public void setCameraImageCoordinateId(Long cameraImageCoordinateId) {
		this.cameraImageCoordinateId = cameraImageCoordinateId;
	}

	public static LaneResponse get(Lane lane) {
		return new LaneResponse(lane.getId(), lane.getName(), lane.gettArmId(),
				lane.getCameraImageCoordinate().getId());
	}
}
