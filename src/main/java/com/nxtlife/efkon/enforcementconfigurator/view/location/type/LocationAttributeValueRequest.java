package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationAttributeValueRequest implements Request {

	@Schema(description = "Id of the location type attribute", example = "1")
	@NotNull(message = "location type attribute id can't be null")
	private String locationTypeAttributeId;

	@Schema(description = "value of the location type attribute", example = "zone1")
	private String value;

	public String getLocationTypeAttributeId() {
		return locationTypeAttributeId;
	}

	public String getValue() {
		return value;
	}
}
