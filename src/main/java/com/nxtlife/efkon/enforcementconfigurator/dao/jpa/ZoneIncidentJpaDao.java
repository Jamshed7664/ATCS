package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.ZoneIncident;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.ZoneIncidentKey;

public interface ZoneIncidentJpaDao extends JpaRepository<ZoneIncident, ZoneIncidentKey> {

	@Query(value = "select incident.name from ZoneIncident where cameraImageCoordinate.id=?1 order by incident.name")
	public List<String> findIncidentNamesByZoneId(Long zoneId);

	@Modifying
	@Query(value = "insert into zone_incident(zone_id, incident_name) values (?1,?2)", nativeQuery = true)
	public int save(Long zoneId, String incidentName);

	@Modifying
	@Query(value = "delete from ZoneIncident where cameraImageCoordinate.id=?2 and incident.name in ?1")
	public int deleteByIncidentNamesAndZoneId(List<String> incidentNames, Long zoneId);
}
