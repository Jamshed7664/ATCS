package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.enforcementconfigurator.view.common.RoadAlignmentPropertyRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class CameraImageCoordinateListRequest {

	@Schema(description = "List of camera image coordinates", required = true)
	@NotEmpty(message = "image coordinates can't be empty")
	@Valid
	private List<CameraImageCoordinateRequest> imageCoordinates;

	@Schema(description = "If user want to schedule the incident ", defaultValue = "false")
	private Boolean incidentScheduling;

	@Schema(description = "Road alignment property details")
	@Valid
	private RoadAlignmentPropertyRequest roadAlignmentProperties;

	public Boolean getIncidentScheduling() {
		return incidentScheduling;
	}

	public RoadAlignmentPropertyRequest getRoadAlignmentProperties() {
		return roadAlignmentProperties;
	}

	public List<CameraImageCoordinateRequest> getImageCoordinates() {
		return imageCoordinates;
	}
}
