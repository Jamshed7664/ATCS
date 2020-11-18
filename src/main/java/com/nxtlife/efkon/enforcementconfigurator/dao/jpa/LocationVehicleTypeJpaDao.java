package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.LocationVehicleTypeKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.LocationVehicleType;

public interface LocationVehicleTypeJpaDao extends JpaRepository<LocationVehicleType, LocationVehicleTypeKey> {

	@Modifying
	@Query(value = "insert into location_vehicle_type(location_id, vehicle_type_id) values (?1,?2)", nativeQuery = true)
	public int save(String locationId, Long vehicleTypeId);

	@Query(value = "select vehicle_type_id from location_vehicle_type where location_id=?1", nativeQuery = true)
	public List<Long> findAllVehicleTypeIdsByLocationId(String locationId);

	@Modifying
	@Query(value = "delete from location_vehicle_type where location_id=?1 and vehicle_type_id in ?2", nativeQuery = true)
	public int deleteByLocationIdAndVehicleTypeIds(String locationId, List<Long> vehicleTypeIds);

}
