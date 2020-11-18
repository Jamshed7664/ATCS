package com.nxtlife.efkon.enforcementconfigurator.view.common;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class RoadAlignmentPropertyRequest {

	@Schema(description = "Length of the road", example = "111.11")
	@NotNull(message = "Road length can't be null")
	private Double roadLength;

	@Schema(description = "Breadth of the road", example = "234.1")
	@NotNull(message = "Road Breadth can't be null")
	private Double roadBreadth;

	@Schema(description = "Maximum Distance", example = "234232")
	@NotNull(message = "Max distance can't be null")
	private BigDecimal maxDistance;

	public Double getRoadLength() {
		return roadLength;
	}

	public Double getRoadBreadth() {
		return roadBreadth;
	}

	public BigDecimal getMaxDistance() {
		return maxDistance;
	}
}
