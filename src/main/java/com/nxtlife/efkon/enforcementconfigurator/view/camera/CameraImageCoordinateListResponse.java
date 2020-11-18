package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.enforcementconfigurator.view.common.RoadAlignmentPropertyResponse;

import io.swagger.v3.oas.annotations.media.Schema;

public class CameraImageCoordinateListResponse {

	@Schema(description = "List of camera image coordinates")
	@NotEmpty(message = "image coordinates can't be empty")
	private List<CameraImageCoordinateResponse> imageCoordinates;

	@Schema(description = "If user want to schedule the incident ", defaultValue = "false")
	private Boolean incidentScheduling;

	@Schema(description = "Road alignment property details if road alignments are available")
	private RoadAlignmentPropertyResponse roadAlignmentProperties;

	public List<CameraImageCoordinateResponse> getImageCoordinates() {
		return imageCoordinates;
	}

	public void setImageCoordinates(List<CameraImageCoordinateResponse> imageCoordinates) {
		this.imageCoordinates = imageCoordinates;
	}

	public Boolean getIncidentScheduling() {
		return incidentScheduling;
	}

	public void setIncidentScheduling(Boolean incidentScheduling) {
		this.incidentScheduling = incidentScheduling;
	}

	public RoadAlignmentPropertyResponse getRoadAlignmentProperties() {
		return roadAlignmentProperties;
	}

	public void setRoadAlignmentProperties(RoadAlignmentPropertyResponse roadAlignmentProperties) {
		this.roadAlignmentProperties = roadAlignmentProperties;
	}

}
