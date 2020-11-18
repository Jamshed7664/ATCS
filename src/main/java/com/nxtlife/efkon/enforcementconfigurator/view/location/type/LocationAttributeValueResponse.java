package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationAttributeValueResponse implements Response {

	@Schema(description = "locationTypeAttributeId of the location type attribute")
	private String locationTypeAttributeId;

	@Schema(description = "Location type attribute detail")
	private LocationTypeAttributeResponse locationTypeAttributeResponse;

	@Schema(description = "Value of the Location type attribute")
	private String value;

	public LocationAttributeValueResponse(String locationTypeAttributeId, String value) {
		super();
		this.locationTypeAttributeId = locationTypeAttributeId;
		this.value = value;
	}

	public String getLocationTypeAttributeId() {
		return locationTypeAttributeId;
	}

	public void setLocationTypeAttributeId(String locationTypeAttributeId) {
		this.locationTypeAttributeId = locationTypeAttributeId;
	}

	public LocationTypeAttributeResponse getLocationTypeAttributeResponse() {
		return locationTypeAttributeResponse;
	}

	public void setLocationTypeAttributeResponse(LocationTypeAttributeResponse locationTypeAttributeResponse) {
		this.locationTypeAttributeResponse = locationTypeAttributeResponse;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public static LocationAttributeValueResponse get(LocationTypeAttributeResponse attribute, String value){
		LocationAttributeValueResponse response = new LocationAttributeValueResponse(attribute.getId(), value);
		response.setLocationTypeAttributeResponse(attribute);
		return response;
		
	}
}
