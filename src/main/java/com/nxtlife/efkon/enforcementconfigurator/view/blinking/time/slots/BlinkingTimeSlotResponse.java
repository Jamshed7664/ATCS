package com.nxtlife.efkon.enforcementconfigurator.view.blinking.time.slots;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class BlinkingTimeSlotResponse implements Response {

	@Schema(description = "Id of the blinking time slot", example = "1")
	private Long id;

	@Schema(description = "Blinking start time", example = "11:00:00")
	private LocalTime startTime;

	@Schema(description = "Blinking stop time", example = "2:00:00")
	private LocalTime endTime;

	public BlinkingTimeSlotResponse(Long id, LocalTime startTime, LocalTime endTime) {
		super();
		this.id = id;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}
}
