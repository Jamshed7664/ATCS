package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.vehicle.type.VehicleType;
import com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type.VehicleTypeResponse;

public interface VehicleTypeJpaDao extends JpaRepository<VehicleType, Long> {

	public Boolean existsByName(String name);

	@Query(value = "select id from vehicle_type_fx", nativeQuery = true)
	public List<Long> findAllIds();
	
	public List<VehicleTypeResponse> findByLocationVehicleTypesLocationId(String locationId);
}
