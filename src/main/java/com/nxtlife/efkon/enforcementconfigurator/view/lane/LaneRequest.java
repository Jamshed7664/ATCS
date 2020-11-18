package com.nxtlife.efkon.enforcementconfigurator.view.lane;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class LaneRequest implements Request {

	@Schema(description = "Id of the lane", nullable = true)
	private String id;

	@Schema(description = "Ids of the direction", example = "[1,2]")
	@NotEmpty(message = "Direction ids can't be null or empty")
	private Set<Long> directionIds;

	public String getId() {
		return id;
	}

	public Set<Long> getDirectionIds() {
		if (directionIds != null) {
			return directionIds.stream().map(id -> unmask(id)).collect(Collectors.toSet());
		}
		return new HashSet<>();
	}
}
