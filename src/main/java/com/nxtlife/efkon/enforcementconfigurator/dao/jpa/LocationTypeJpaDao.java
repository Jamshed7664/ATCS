package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationType;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeResponse;

public interface LocationTypeJpaDao extends JpaRepository<LocationType, String> {

	@Query(value = "select id from LocationType where organization.id=?1")
	public List<String> findIdsByOrganizationId(Long organizationId);

	public Boolean existsByCodeAndOrganizationId(String code, Long organizationId);

	public List<LocationTypeResponse> findByOrganizationId(Long organizationId);

	public List<LocationTypeResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active);

	public LocationTypeResponse findResponseById(String id);

}
