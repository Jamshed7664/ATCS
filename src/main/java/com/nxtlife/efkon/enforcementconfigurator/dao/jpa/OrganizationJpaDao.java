package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;
import com.nxtlife.efkon.enforcementconfigurator.view.user.OrganizationResponse;

public interface OrganizationJpaDao extends JpaRepository<Organization, Long> {

	public Organization findByCode(String code);

	public OrganizationResponse findResponseById(Long id);

	@Query(value = "select id from organization where code=?1", nativeQuery = true)
	public Long findIdByCode(String code);

	@Query(value = "select id from Organization where name=?1")
	public Long findIdByName(String name);

	@Query(value = "select is_code_auto_incremented from organization where id=?1", nativeQuery = true)
	public Boolean findIsCodeAutoIncrementedById(Long id);

	public Boolean existsByCode(String code);

}
