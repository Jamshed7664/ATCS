package com.nxtlife.efkon.enforcementconfigurator.entity.vehicle.type;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.LocationVehicleType;

@SuppressWarnings("serial")
@Entity
@Table(name = "vehicle_type_fx")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class VehicleType extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "name can't be null")
	@Column(unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicleType")
	private List<LocationVehicleType> locationVehicleTypes;

	public VehicleType() {
		super();
	}

	public VehicleType(@NotNull(message = "name can't be null") String name) {
		super();
		this.name = name;
	}

	public Long getId() {
		return id;
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

	public List<LocationVehicleType> getLocationVehicleTypes() {
		return locationVehicleTypes;
	}

	public void setLocationVehicleTypes(List<LocationVehicleType> locationVehicleTypes) {
		this.locationVehicleTypes = locationVehicleTypes;
	}
}
