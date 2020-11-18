package com.nxtlife.efkon.enforcementconfigurator.entity.location;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.LocationVehicleTypeKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.vehicle.type.VehicleType;

@SuppressWarnings("serial")
@Entity
@Table(name = "location_vehicle_type")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class LocationVehicleType implements Serializable {

	@EmbeddedId
	private LocationVehicleTypeKey locationVehicleTypeKey;

	@MapsId("locationId")
	@ManyToOne
	private Location location;

	@MapsId("vehicleTypeId")
	@ManyToOne
	private VehicleType vehicleType;

	public LocationVehicleType() {
		super();
	}

	public LocationVehicleType(LocationVehicleTypeKey locationVehicleTypeKey, Location location,
			VehicleType vehicleType) {
		super();
		this.locationVehicleTypeKey = locationVehicleTypeKey;
		this.location = location;
		this.vehicleType = vehicleType;
	}

	public LocationVehicleType(String locationId, Long vehicleTypeId) {
		super();
		this.locationVehicleTypeKey = new LocationVehicleTypeKey(locationId, vehicleTypeId);
		if (locationId != null) {
			this.location = new Location();
			this.location.setId(locationId);
		}
		if (vehicleTypeId != null) {
			this.vehicleType = new VehicleType();
			this.vehicleType.setId(vehicleTypeId);
		}
	}

	public LocationVehicleTypeKey getLocationVehicleTypeKey() {
		return locationVehicleTypeKey;
	}

	public void setLocationVehicleTypeKey(LocationVehicleTypeKey locationVehicleTypeKey) {
		this.locationVehicleTypeKey = locationVehicleTypeKey;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public VehicleType getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleType vehicleType) {
		this.vehicleType = vehicleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((location == null) ? 0 : location.hashCode());
		result = prime * result + ((vehicleType == null) ? 0 : vehicleType.hashCode());
		result = prime * result + ((locationVehicleTypeKey == null) ? 0 : locationVehicleTypeKey.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LocationVehicleType other = (LocationVehicleType) obj;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (vehicleType == null) {
			if (other.vehicleType != null)
				return false;
		} else if (!vehicleType.equals(other.vehicleType))
			return false;
		if (locationVehicleTypeKey == null) {
			if (other.locationVehicleTypeKey != null)
				return false;
		} else if (!locationVehicleTypeKey.equals(other.locationVehicleTypeKey))
			return false;
		return true;
	}
}
