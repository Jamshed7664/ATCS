package com.nxtlife.efkon.enforcementconfigurator.entity.location;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationAttributeValue;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationType;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;

@SuppressWarnings("serial")
@Entity
@Table(name = "location_mst")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Location extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotEmpty(message = "name can't be null or empty")
	private String name;

	@NotNull(message = "code can't be null")
	@Column(name = "code")
	private String code;

	@NotEmpty(message = "longitude can't be empty")
	@Column(name = "longitude")
	private String longitude;

	@NotEmpty(message = "latitude can't be empty")
	@Column(name = "latitude")
	private String latitude;

	private String usages;

	private String address;

	@ManyToOne
	private LocationType locationType;

	@ManyToOne
	private Organization organization;

	@Transient
	private String tLocationTypeId;

	@Transient
	private Long tOrganizationId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
	private List<Arm> arms;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
	private List<LocationAttributeValue> locationAttributeValues;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "location")
	private List<LocationVehicleType> locationVehicleTypes;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String gettLocationTypeId() {
		return tLocationTypeId;
	}

	public void settLocationTypeId(String tLocationTypeId) {
		if (tLocationTypeId != null) {
			this.locationType = new LocationType();
			this.locationType.setId(tLocationTypeId);
		}
		this.tLocationTypeId = tLocationTypeId;
	}

	public Long gettOrganizationId() {
		return tOrganizationId;
	}

	public void settOrganizationId(Long tOrganizationId) {
		if (tOrganizationId != null) {
			this.organization = new Organization();
			this.organization.setId(tOrganizationId);
		}
		this.tOrganizationId = tOrganizationId;
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

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getUsages() {
		return usages;
	}

	public void setUsages(String usages) {
		this.usages = usages;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public LocationType getLocationType() {
		this.settLocationTypeId(locationType.getId());
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public List<Arm> getArms() {
		return arms;
	}

	public void setArms(List<Arm> arms) {
		this.arms = arms;
	}

	public List<LocationAttributeValue> getLocationAttributeValues() {
		return locationAttributeValues;
	}

	public void setLocationAttributeValues(List<LocationAttributeValue> locationAttributeValues) {
		this.locationAttributeValues = locationAttributeValues;
	}

	public List<LocationVehicleType> getLocationVehicleTypes() {
		return locationVehicleTypes;
	}

	public void setLocationVehicleTypes(List<LocationVehicleType> locationVehicleTypes) {
		this.locationVehicleTypes = locationVehicleTypes;
	}
}
