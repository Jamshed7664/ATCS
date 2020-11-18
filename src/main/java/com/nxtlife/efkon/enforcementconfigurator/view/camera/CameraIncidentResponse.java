package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CameraIncidentResponse {

	@Schema(description = "Name of the incident", example = "Congestion In Traffic")
	private String incidentName;

	@Schema(description = "Incident attribute values  details")
	private List<CameraIncidentAttributeValueResponse> attributeValues;

	@Schema(description = "If incident define for camera then true else false")
	private Boolean checked;

	public CameraIncidentResponse() {
		super();
	}

	public CameraIncidentResponse(String incidentName, List<CameraIncidentAttributeValueResponse> attributeValues,
			Boolean checked) {
		super();
		this.incidentName = incidentName;
		this.attributeValues = attributeValues;
		this.checked = checked;
	}

	public String getIncidentName() {
		return incidentName;
	}

	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public List<CameraIncidentAttributeValueResponse> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<CameraIncidentAttributeValueResponse> attributeValues) {
		this.attributeValues = attributeValues;
	}
}
