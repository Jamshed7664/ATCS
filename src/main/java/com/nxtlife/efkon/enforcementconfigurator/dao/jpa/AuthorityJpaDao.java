package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.Authority;
import com.nxtlife.efkon.enforcementconfigurator.view.user.security.AuthorityResponse;

import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AuthorityJpaDao extends JpaRepository<Authority, Long> {
	
	public Boolean existsByName(String name);
	
	public AuthorityResponse findResponseById(Long id);
	
	public List<AuthorityResponse> findByAuthorityRolesRoleId(Long roleId);

	@Query(value="select id from authority",nativeQuery = true)
	public List<Long> findAllIds();

}
