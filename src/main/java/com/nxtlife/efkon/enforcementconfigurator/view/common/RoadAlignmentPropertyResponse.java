package com.nxtlife.efkon.enforcementconfigurator.view.common;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;

public class RoadAlignmentPropertyResponse {

	@Schema(description = "Length of the road", example = "111.11")
	private Double roadLength;

	@Schema(description = "Breadth of the road", example = "234.1")
	private Double roadBreadth;

	@Schema(description = "Maximum Distance", example = "234232")
	private BigDecimal maxDistance;

	public RoadAlignmentPropertyResponse(Double roadLength, Double roadBreadth, BigDecimal maxDistance) {
		super();
		this.roadLength = roadLength;
		this.roadBreadth = roadBreadth;
		this.maxDistance = maxDistance;
	}

	public Double getRoadLength() {
		return roadLength;
	}

	public void setRoadLength(Double roadLength) {
		this.roadLength = roadLength;
	}

	public Double getRoadBreadth() {
		return roadBreadth;
	}

	public void setRoadBreadth(Double roadBreadth) {
		this.roadBreadth = roadBreadth;
	}

	public BigDecimal getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(BigDecimal maxDistance) {
		this.maxDistance = maxDistance;
	}
}
