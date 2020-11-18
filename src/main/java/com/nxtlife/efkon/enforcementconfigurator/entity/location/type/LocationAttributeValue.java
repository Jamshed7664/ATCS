package com.nxtlife.efkon.enforcementconfigurator.entity.location.type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "location_attribute_value", uniqueConstraints = @UniqueConstraint(columnNames = {
		"location_type_attribute_id", "location_id" }))
public class LocationAttributeValue extends BaseEntity implements Serializable {

	@Id
	private String id;

	@ManyToOne
	@NotNull(message = "location type attribute can't be null")
	private LocationTypeAttribute locationTypeAttribute;

	@ManyToOne
	@NotNull(message = "location can't be null")
	private Location location;

	@NotEmpty(message = "value can't be empty")
	private String value;

	public LocationAttributeValue() {
		super();
	}

	public LocationAttributeValue(String id, @NotNull(message = "location can't be null") Location location,
			@NotEmpty(message = "value can't be empty") String value, String locationTypeAttributeId) {
		super();
		this.id = id;
		this.location = location;
		this.value = value;
		if (locationTypeAttributeId != null) {
			this.locationTypeAttribute = new LocationTypeAttribute();
			this.locationTypeAttribute.setId(locationTypeAttributeId);
		}
	}

	public LocationAttributeValue(LocationTypeAttribute locationTypeAttribute, Location location, String value) {
		super();
		this.locationTypeAttribute = locationTypeAttribute;
		this.location = location;
		this.value = value;
	}

	public LocationAttributeValue(String locationTypeAttributeId, String locationId, String value) {
		super();
		if (locationTypeAttributeId != null) {
			this.locationTypeAttribute = new LocationTypeAttribute();
			this.locationTypeAttribute.setId(locationTypeAttributeId);
		}
		if (locationId != null) {
			this.location = new Location();
			this.location.setId(locationId);
		}
		this.value = value;
	}

	public LocationAttributeValue(String id, String locationTypeAttributeId, String locationId, String value) {
		super();
		this.id = id;
		if (locationTypeAttributeId != null) {
			this.locationTypeAttribute = new LocationTypeAttribute();
			this.locationTypeAttribute.setId(locationTypeAttributeId);
		}
		if (locationId != null) {
			this.location = new Location();
			this.location.setId(locationId);
		}
		this.value = value;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocationTypeAttribute getLocationTypeAttribute() {
		return locationTypeAttribute;
	}

	public void setLocationTypeAttribute(LocationTypeAttribute locationTypeAttribute) {
		this.locationTypeAttribute = locationTypeAttribute;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
