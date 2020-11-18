package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationAttributeValue;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationAttributeValueResponse;

public interface LocationAttributeValueJpaDao extends JpaRepository<LocationAttributeValue, String> {

	// @Modifying
	// @Query(value = "insert into location_attribute_value (id,
	// location_type_attribute_id, location_id, value, created_at, created_by)
	// values (?1,?2,?3,?4,?5,?6)")
	// public int save(String id, String locationTypeAttributeId, String
	// LocationId,)
	List<LocationAttributeValueResponse> findByLocationId(String locationId);

	public Boolean existsByLocationTypeAttributeIdAndActiveAndLocationActive(String locationTypeAttributeId, Boolean active,
			Boolean locationActive);

	@Modifying
	@Query(value = "update LocationAttributeValue set value=?1, modifiedBy.id = ?4, modifiedAt=?5 where locationTypeAttribute.id=?2 and location.id=?3")
	public int update(String value, String locationAttributeId, String locationId, Long userId, Date date);

}
