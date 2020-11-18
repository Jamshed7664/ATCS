package com.nxtlife.efkon.enforcementconfigurator.view.location;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import com.nxtlife.efkon.enforcementconfigurator.view.Request;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationAttributeValueRequest;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationRequest implements Request {

	@JsonIgnore
	private String name;

	@Schema(description = "Id of the location type", example = "1")
	@NotNull(message = "location_type_id can't be null")
	private String locationTypeId;

	@Valid
	@Schema(description = "Arms in that location. This attribute is applicable for only highway or junction")
	private LinkedHashSet<ArmRequest> arms;

	@NotEmpty(message = "attributeValues can't be empty")
	@Schema(description = "Location type attribute and their values")
	private Set<LocationAttributeValueRequest> attributeValues;

	public Location toEntity(Location location) {
		if (location == null) {
			location = new Location();
		}
		location.settLocationTypeId(locationTypeId);
		return location;
	}

	public String getCode() {
		if (this.name == null) {
			return null;
		}
		String[] splitName = this.name.split(" ");
		int splitNameLength = splitName.length;
		String code;
		if (splitNameLength > 2) {
			code = String.format("%c%c%c", splitName[0].charAt(0), splitName[1].charAt(0), splitName[2].charAt(0));
		} else if (splitNameLength == 2) {
			code = String.format("%c%cJ", splitName[0].charAt(0), splitName[1].charAt(0));
		} else {
			code = splitName[0].substring(0, 3);
		}
		return code.toUpperCase();
	}

	public String getCode(String name) {
		if (name == null) {
			return null;
		}
		String[] splitName = name.split(" ");
		int splitNameLength = splitName.length;
		String code;
		if (splitNameLength > 2) {
			code = String.format("%c%c%c", splitName[0].charAt(0), splitName[1].charAt(0), splitName[2].charAt(0));
		} else if (splitNameLength == 2) {
			code = String.format("%c%cJ", splitName[0].charAt(0), splitName[1].charAt(0));
		} else {
			code = splitName[0].substring(0, 3);
		}
		return code.toUpperCase();
	}

	public String getLocationTypeId() {
		return locationTypeId;
	}

	public LinkedHashSet<ArmRequest> getArms() {
		return arms;
	}

	public Set<LocationAttributeValueRequest> getAttributeValues() {
		return attributeValues;
	}

}
