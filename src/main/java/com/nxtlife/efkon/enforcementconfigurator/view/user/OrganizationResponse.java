package com.nxtlife.efkon.enforcementconfigurator.view.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_ABSENT)
public class OrganizationResponse implements Response {

	@Schema(description = "Unique identifier of organization", example = "1")
	public Long id;
	@Schema(description = "Organization's name", example = "TUMKUR")
	public String name;
	@Schema(description = "Organization's code", example = "TUM")
	public String code;
	@Schema(description = "Organization's description", example = "Tumkur description", nullable = true)
	public String description;
	@Schema(description = "Organization's centre longitude", example = "Tumkur centre longitude", nullable = true)
	public String centreLongitude;
	@Schema(description = "Organization's centre latitude", example = "Tumkur centre latitude", nullable = true)
	public String centreLatitude;
	@Schema(description = "Organization's map restrict point a", example = "82.9999", nullable = true)
	public String pointA;
	@Schema(description = "Organization's map restrict point b", example = "82.9999", nullable = true)
	public String pointB;
	@Schema(description = "Organization's map restrict point c", example = "82.9999", nullable = true)
	public String pointC;
	@Schema(description = "Organization's map restrict point d", example = "82.9999", nullable = true)
	public String pointD;
	@Schema(description = "Code is auto incremented or not for every equipment/location", example = "TRUE", nullable = true)
	public Boolean isCodeAutoIncremented;
	@Schema(description = "Incident duration limit for incidents", example = "10", nullable = true)
	public Integer incidentDurationLimit;

	public OrganizationResponse(Long id, String name, String code, String description, String centreLongitude,
			String centreLatitude, String pointA, String pointB, String pointC, String pointD,
			Boolean isCodeAutoIncremented, Integer incidentDurationLimit) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.description = description;
		this.centreLongitude = centreLongitude;
		this.centreLatitude = centreLatitude;
		this.pointA = pointA;
		this.pointB = pointB;
		this.pointC = pointC;
		this.pointD = pointD;
		this.isCodeAutoIncremented = isCodeAutoIncremented;
		this.incidentDurationLimit = incidentDurationLimit;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCentreLongitude() {
		return centreLongitude;
	}

	public void setCentreLongitude(String centreLongitude) {
		this.centreLongitude = centreLongitude;
	}

	public String getCentreLatitude() {
		return centreLatitude;
	}

	public void setCentreLatitude(String centreLatitude) {
		this.centreLatitude = centreLatitude;
	}

	public String getPointA() {
		return pointA;
	}

	public void setPointA(String pointA) {
		this.pointA = pointA;
	}

	public String getPointB() {
		return pointB;
	}

	public void setPointB(String pointB) {
		this.pointB = pointB;
	}

	public String getPointC() {
		return pointC;
	}

	public void setPointC(String pointC) {
		this.pointC = pointC;
	}

	public String getPointD() {
		return pointD;
	}

	public void setPointD(String pointD) {
		this.pointD = pointD;
	}

	public Boolean getIsCodeAutoIncremented() {
		return isCodeAutoIncremented;
	}

	public void setIsCodeAutoIncremented(Boolean isCodeAutoIncremented) {
		this.isCodeAutoIncremented = isCodeAutoIncremented;
	}

	public Integer getIncidentDurationLimit() {
		return incidentDurationLimit;
	}

	public void setIncidentDurationLimit(Integer incidentDurationLimit) {
		this.incidentDurationLimit = incidentDurationLimit;
	}

}
