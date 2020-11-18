package com.nxtlife.efkon.enforcementconfigurator.view.location.type;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_ABSENT)
public class LocationTypeAttributeResponse implements Response {

	@Schema(description = "Id of location type attribute", example = "1")
	private String id;
	@Schema(description = "Name of location type attribute", example = "ZoneId")
	private String name;
	@Schema(description = "data type of location type attribute", example = "Integer or Option")
	private DataType dataType;
	@Schema(description = "Data type of location type attribute for html input type", example = "text")
	private String htmlInputType;
	@Schema(description = "Location type attribute is static or not", defaultValue = "false", example = "true")
	private Boolean fixed;
	@Schema(description = "Location type details")
	private LocationTypeResponse locationType;
	@Schema(description = "Location type id", example = "1")
	private String locationTypeId;
	@Schema(description = "location type attribute options if required", example = "1,2,3", nullable = true)
	private Set<String> options;

	public LocationTypeAttributeResponse(String id, String name, DataType dataType, Boolean fixed,
			String locationTypeId, Set<String> options) {
		super();
		this.id = id;
		this.name = name;
		this.dataType = dataType;
		if (dataType != null) {
			this.htmlInputType = dataType.getHtmlInputType();
		}
		this.fixed = fixed;
		this.locationTypeId = locationTypeId;
		this.options = options;
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

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getHtmlInputType() {
		return htmlInputType;
	}

	public void setHtmlInputType(String htmlInputType) {
		this.htmlInputType = htmlInputType;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public LocationTypeResponse getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationTypeResponse locationType) {
		this.locationType = locationType;
	}

	public String getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(String locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

}
