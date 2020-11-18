package com.nxtlife.efkon.enforcementconfigurator.view.incident;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentAttributeValueRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class IncidentRequest {

	@Schema(description = "Name of the incident", example = "No Helmet")
	private String name;

	@Schema(description = "This will be used when we are incident in database (internal use only).", example = "Vehicle_Incident", allowableValues = {
			"VEHICLE_INCIDENT", "TRAFFIC_INCIDENT", "WEATHER_INCIDENT", "OTHER" })
	private String type;

	@Schema(description = "Incident related to arm or lanes. This will be used when we are adding incident in db (internal use only)", defaultValue = "0")
	private Integer locationRelated;

	@Schema(description = "Incident related to highway or junction. This will be used when we are adding incident in db (internal use only)", defaultValue = "0")
	private Integer locationTypeRelated;

	@Valid
	private List<IncidentAttributeRequest> attributes;

	@Valid
	private List<CameraIncidentAttributeValueRequest> attributeValues;

	public Incident toEntity() {
		Incident incident = new Incident();
		incident.setName(name);
		return incident;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public Integer getLocationRelated() {
		return locationRelated;
	}

	public Integer getLocationTypeRelated() {
		return locationTypeRelated;
	}

	public List<IncidentAttributeRequest> getAttributes() {
		if (attributes == null) {
			attributes = new ArrayList<>();
		}
		return attributes;
	}

	public List<CameraIncidentAttributeValueRequest> getAttributeValues() {
		if (attributeValues == null) {
			attributeValues = new ArrayList<>();
		}
		return attributeValues;
	}
}
