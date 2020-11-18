package com.nxtlife.efkon.enforcementconfigurator.view.location;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentGroupByResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationAttributeValueResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type.VehicleTypeResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class LocationResponse implements Response {

	@Schema(description = "Id of the location", example = "1")
	private String id;

	@Schema(description = "Name of the location", example = "Noida")
	private String name;

	@Schema(description = "Code of the location", example = "JUN0001")
	private String code;

	@Schema(description = "Latitude of the location", example = "345.09")
	private String latitude;

	@Schema(description = "Longitude of the location", example = "245")
	private String longitude;

	@Schema(description = "Address detail of the location")
	private String address;

	@Schema(description = "Arm count of that location", example = "5")
	private Integer armCount;

	@JsonIgnore
	private Long organizationId;

	@Schema(description = "TypeId of that location", example = "LOCA00001")
	private String locationTypeId;

	@Schema(description = "TypeCode of that location", example = "JUN")
	private String locationTypeCode;

	@Schema(description = "Type of that location", example = "JUNCTION")
	private String locationTypeName;

	private List<ArmResponse> arms;

	private List<LocationAttributeValueResponse> attributeValues;

	private List<VehicleTypeResponse> vehicleTypes;

	@Schema(description = "Detail of equipments mapped with this location. This detail will be fetch when you will fetch equipmennt mappings")
	private List<EquipmentGroupByResponse> equipments;

	public LocationResponse(String id, String name, String code, String latitude, String longitude, String address,
			Long organizationId, String locationTypeId, String locationTypeName, String locationTypeCode) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.latitude = latitude;
		this.longitude = longitude;
		this.address = address;
		this.organizationId = organizationId;
		this.locationTypeId = locationTypeId;
		this.locationTypeCode = locationTypeCode;
		this.locationTypeName = locationTypeName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}

	public String getLocationTypeId() {
		return locationTypeId;
	}

	public void setLocationTypeId(String locationTypeId) {
		this.locationTypeId = locationTypeId;
	}

	public String getLocationTypeCode() {
		return locationTypeCode;
	}

	public void setLocationTypeCode(String locationTypeCode) {
		this.locationTypeCode = locationTypeCode;
	}

	public String getLocationTypeName() {
		return locationTypeName;
	}

	public void setLocationTypeName(String locationTypeName) {
		this.locationTypeName = locationTypeName;
	}

	public Integer getArmCount() {
		return armCount;
	}

	public void setArmCount(Integer armCount) {
		this.armCount = armCount;
	}

	public List<ArmResponse> getArms() {
		return arms;
	}

	public void setArms(List<ArmResponse> arms) {
		this.arms = arms;
	}

	public List<LocationAttributeValueResponse> getAttributeValues() {
		return attributeValues;
	}

	public void setAttributeValues(List<LocationAttributeValueResponse> attributeValues) {
		this.attributeValues = attributeValues;
	}

	public List<VehicleTypeResponse> getVehicleTypes() {
		return vehicleTypes;
	}

	public void setVehicleTypes(List<VehicleTypeResponse> vehicleTypes) {
		this.vehicleTypes = vehicleTypes;
	}

	public List<EquipmentGroupByResponse> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentGroupByResponse> equipments) {
		this.equipments = equipments;
	}

	public static LocationResponse get(Location location) {
		if (location != null) {
			LocationResponse locationResponse = new LocationResponse(location.getId(), location.getName(),
					location.getCode(), location.getLatitude(), location.getLongitude(), location.getAddress(),
					location.gettOrganizationId(), location.getLocationType().getId(),
					location.getLocationType().getName(), location.getLocationType().getCode());
			return locationResponse;
		}
		return null;
	}
}
