package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentAttributeResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CameraIncidentAttributeValueResponse implements Response {

	@JsonIgnore
	@Schema(description = "Name of the incident", example = "Congestion In Traffic")
	private String incidentName;

	@Schema(description = "Id of the incident attribute", example = "1")
	private Long incidentAttributeId;

	@Schema(description = "Incident attribute detail")
	private IncidentAttributeResponse attribute;

	@Schema(description = "value of the incident attribute", example = "23")
	private String value;

	public CameraIncidentAttributeValueResponse(String incidentName, Long incidentAttributeId, String value) {
		super();
		this.incidentName = incidentName;
		this.incidentAttributeId = incidentAttributeId;
		this.value = value;
	}

	public String getIncidentName() {
		return incidentName;
	}

	public void setIncidentName(String incidentName) {
		this.incidentName = incidentName;
	}

	public Long getIncidentAttributeId() {
		return mask(incidentAttributeId);
	}

	public void setIncidentAttributeId(Long incidentAttributeId) {
		this.incidentAttributeId = incidentAttributeId;
	}

	public IncidentAttributeResponse getAttribute() {
		return attribute;
	}

	public void setAttribute(IncidentAttributeResponse attribute) {
		this.attribute = attribute;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
