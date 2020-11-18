package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

public interface LocationJpaDao extends JpaRepository<Location, String> {

	public Boolean existsByIdAndOrganizationId(String id, Long organizationId);
	
	public Boolean existsByIdAndOrganizationIdAndActive(String id, Long organizationId, Boolean active);

	public Boolean existsByIdAndActive(String id, Boolean active);

	public Boolean existsByCodeAndOrganizationIdAndActive(String code, Long organizationId, Boolean active);

	public Boolean existsByNameAndOrganizationIdAndActive(String name, Long organizationId, Boolean active);

	public Boolean existsByLatitudeAndLongitudeAndOrganizationIdAndActive(String latitude, String longitude,
			Long organizationId, Boolean active);

	public Boolean existsByLocationTypeIdAndOrganizationIdAndActive(String locationTypeId, Long organizationId,
			Boolean active);

	public LocationResponse findResponseById(String id);
	
	public List<LocationResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active);

	@Query(value = "select locationType.id from Location where id=?1")
	public String findLocationTypeIdById(String id);

	@Modifying
	@Query(value = "update Location set name=:name, code=:code, address=:address, longitude=:longitude, latitude=:latitude, modifiedBy.id =:modifiedBy, modifiedAt =:modifiedAt where id = :id")
	public int update(@Param("id") String id, @Param("name") String name, @Param("code") String code,
			@Param("address") String address, @Param("longitude") String longitude, @Param("latitude") String latitude,
			@Param("modifiedBy") Long modifiedBy, @Param("modifiedAt") Date modifiedAt);

	@Modifying
	@Query(value = "update Location set active=false, modifiedBy.id =?2, modifiedAt =?3 where id =?1")
	public int delete(String id, Long userId, Date date);

}
