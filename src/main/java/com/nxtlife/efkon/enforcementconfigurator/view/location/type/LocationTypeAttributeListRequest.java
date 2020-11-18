package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationTypeAttributeListRequest {

	@Schema(description = "List of location type attributes", required = true)
	@NotEmpty(message = "Location type attributes can't be empty")
	@Valid
	private List<LocationTypeAttributeRequest> list;

	public List<LocationTypeAttributeRequest> getList() {
		return list;
	}

}
