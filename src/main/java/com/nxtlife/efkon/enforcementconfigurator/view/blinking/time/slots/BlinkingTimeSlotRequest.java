package com.nxtlife.efkon.enforcementconfigurator.view.blinking.time.slots;

import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class BlinkingTimeSlotRequest implements Request {

	@Schema(description = "Id of the blinking time slot", example = "1")
	private Long id;

	@Schema(description = "Blinking start time", example = "11:00:00")
	@NotNull(message = "start_time can't be null")
	private String startTime;

	@Schema(description = "Blinking stop time", example = "2:00:00")
	@NotNull(message = "end_time can't be null")
	private String endTime;

	public Long getId() {
		return unmask(id);
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}
}
