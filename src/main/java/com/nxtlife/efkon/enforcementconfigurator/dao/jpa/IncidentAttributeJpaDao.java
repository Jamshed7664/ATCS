package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttribute;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentAttributeResponse;

public interface IncidentAttributeJpaDao extends JpaRepository<IncidentAttribute, Long> {

	public List<IncidentAttributeResponse> findByActive(Boolean active);
}
