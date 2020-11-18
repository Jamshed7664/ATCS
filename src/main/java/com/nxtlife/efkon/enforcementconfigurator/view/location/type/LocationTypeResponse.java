package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_ABSENT)
public class LocationTypeResponse implements Response {

	@Schema(description = "Id of location type", example = "1")
	private String id;
	@Schema(description = "Name of location type", example = "Junction")
	private String name;
	@Schema(description = "Code of location type", example = "JUN")
	private String code;
	@Schema(description = "Location type attributes detail")
	private List<LocationTypeAttributeResponse> locationTypeAttributes;

	public LocationTypeResponse(String id, String name, String code) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<LocationTypeAttributeResponse> getLocationTypeAttributes() {
		return locationTypeAttributes;
	}

	public void setLocationTypeAttributes(List<LocationTypeAttributeResponse> locationTypeAttributes) {
		this.locationTypeAttributes = locationTypeAttributes;
	}

	public LocationTypeResponse get(LocationType locationType) {
		if (locationType != null) {
			LocationTypeResponse response = new LocationTypeResponse(locationType.getId(), locationType.getName(),
					locationType.getCode());
			return response;
		}
		return null;
	}

}
