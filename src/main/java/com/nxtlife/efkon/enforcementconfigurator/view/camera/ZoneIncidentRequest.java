package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.List;

import javax.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

public class ZoneIncidentRequest {

	@NotNull
	@Schema(description = "Incident Names ")
	private List<String> incidentNames;

	public List<String> getIncidentNames() {
		return incidentNames;
	}
}
