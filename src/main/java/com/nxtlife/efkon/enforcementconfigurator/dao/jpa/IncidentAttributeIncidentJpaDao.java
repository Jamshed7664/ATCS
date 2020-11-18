package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.IncidentAttributeIncidentKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttributeIncident;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;

public interface IncidentAttributeIncidentJpaDao
		extends JpaRepository<IncidentAttributeIncident, IncidentAttributeIncidentKey> {

	@Query(value = "select incident.name as incidentName,attribute.id as attributeId,attribute.name as attributeName,attribute.dataType as dataType,attribute.unit as unit from IncidentAttributeIncident iai  right outer join Incident incident on iai.incident.name=incident.name left outer join IncidentAttribute attribute on iai.incidentAttribute.id=attribute.id where incident.type=?1 and (incident.locationRelated =0 or incident.locationRelated=?2) and (incident.locationTypeRelated=0 or incident.locationTypeRelated=?3) order by incident.name,attribute.id")
	public List<Map<String, Object>> findByIncidentTypeAndLocationRelatedAndLocationTypeRelated(
			IncidentType incidentType, Integer locationRelated, Integer locationTypeRelated);

}
