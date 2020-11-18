package com.nxtlife.efkon.enforcementconfigurator.view.direction;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.direction.Direction;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class DirectionResponse implements Response {

	@Schema(description = "Id of the direction", example = "1")
	private Long id;

	@Schema(description = "Name of the direction", example = "left")
	private String name;

	public DirectionResponse() {
		super();
	}

	public DirectionResponse(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public DirectionResponse(Direction direction) {
		this.id = direction.getId();
		this.name = direction.getName();
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
