package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class ZoneIncidentResponse {

	@Schema(description = "Name of the incident", example = "Crossed The Red Signal")
	private String incidentName;

	@Schema(description = "Status of incident whether they are active or not in that zone", example = "true")
	private Boolean checked;

	public ZoneIncidentResponse(String incidentName, Boolean checked) {
		super();
		this.incidentName = incidentName;
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
}
