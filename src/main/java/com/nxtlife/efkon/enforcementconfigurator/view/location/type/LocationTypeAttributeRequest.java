package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import java.util.Arrays;
import java.util.HashSet;

import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class LocationTypeAttributeRequest implements Request {

	@Schema(description = "Id of the location type attribute which cannot be empty", example = "1")
	private String id;

	@Schema(description = "Name of the location type attribute which cannot be empty", example = "ZoneId", required = true)
	@NotEmpty(message = "name can't be empty")
	private String name;

	@Schema(description = "Data type of the location type attribute which cannot be empty", example = "Integer or Long", required = true, allowableValues = "[Integer, Boolean, Float, Double, Long, Text, Option, Range]")
	@NotEmpty(message = "data type can't be empty")
	private String dataType;

	@Schema(description = "Options if data type is option or range", example = "A,B,C", maxLength = 1000)
	private String options;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

	public String getOptions() {
		return options;
	}

	public LocationTypeAttribute toEntity() {
		LocationTypeAttribute locationTypeAttribute = new LocationTypeAttribute();
		locationTypeAttribute.setName(name);
		if (dataType != null)
			locationTypeAttribute.setDataType(DataType.valueOf(dataType));
		if (options != null)
			locationTypeAttribute.setOptions(new HashSet<>(Arrays.asList(options.split(","))));
		return locationTypeAttribute;
	}
}
