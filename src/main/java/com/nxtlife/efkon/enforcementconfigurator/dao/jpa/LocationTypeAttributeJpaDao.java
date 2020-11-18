package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeResponse;

public interface LocationTypeAttributeJpaDao extends JpaRepository<LocationTypeAttribute, String> {

	@Query(value = "select name from LocationTypeAttribute where locationType.id = ?1 and active = ?2")
	public Set<String> findNamesByLocationTypeIdAndActive(String locationTypeId, Boolean active);

	public List<LocationTypeAttributeResponse> findByLocationTypeIdAndOrganizationIdAndActive(String locationTypeId,Long organizationId, Boolean active);

	public Boolean existsByNameAndLocationTypeId(String name, String locationTypeId);

	public List<LocationTypeAttributeResponse> findByLocationTypeIdAndActiveOrderByFixedDesc(String locationTypeId,
			Boolean active);

	@Query(value = "select count(id) from LocationTypeAttribute where locationType.id =?1")
	public Long findCountByLocationTypeId(String locationTypeId);

	public LocationTypeAttributeResponse findResponseById(String id);

	public long countByLocationTypeIdAndActive(String locationTypeId, Boolean active);

	@Modifying
	@Query(value = "update location_type_attribute_mst set name=:name, data_type=:dataType, options = :options, modified_by =:modifiedBy, modified_at =:modifiedAt where id=:id", nativeQuery = true)
	public int updateNameAndDataTypeAndOptionsById(@Param("name") String name, @Param("dataType") String dataType,
			@Param("options") String options, @Param("modifiedBy") Long modifiedBy, @Param("modifiedAt") Date date,
			@Param("id") String id);

	@Modifying
	@Query(value = "update LocationTypeAttribute set active = false, modified_by =?2, modified_at =?3 where id = ?1")
	public int delete(String id, Long userId, Date date);

}
