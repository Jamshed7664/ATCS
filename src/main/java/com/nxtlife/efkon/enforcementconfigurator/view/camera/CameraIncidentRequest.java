package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.List;

import javax.validation.Valid;

import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class CameraIncidentRequest {

	@Schema(description = "Id of the that camera", example = "EQMST000001")
	private String cameraId;

	@Schema(description = "Type of the incident", example = "VEHICLE_INCIDENT", allowableValues = {
			"VEHICLE_INCIDENT,TRAFFIC_INCIDENT,WEATHER_INCIDENT,OTHER" })
	private String incidentType;

	@Schema(description = "Incident list request")
	@Valid
	private List<IncidentRequest> incidents;

	public String getCameraId() {
		return cameraId;
	}

	public String getIncidentType() {
		return incidentType;
	}

	public List<IncidentRequest> getIncidents() {
		return incidents;
	}
}
