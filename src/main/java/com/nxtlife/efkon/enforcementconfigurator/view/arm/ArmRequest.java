package com.nxtlife.efkon.enforcementconfigurator.view.arm;

import java.util.Set;

import javax.validation.Valid;

import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class ArmRequest {

	@Schema(description = "Id of the arm", nullable = true)
	private String id;
	
	@Schema(description = "Direction of the arm")
	private String direction;
	
	@Valid
	@Schema(description = "lanes in an arm ")
	private Set<LaneRequest> lanes;

	public String getId() {
		return id;
	}

	public String getDirection() {
		return direction;
	}

	public Set<LaneRequest> getLanes() {
		return lanes;
	}
}
