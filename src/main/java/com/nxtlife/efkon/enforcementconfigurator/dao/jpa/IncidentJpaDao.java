package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;

public interface IncidentJpaDao extends JpaRepository<Incident, String> {

	@Query(value = "select name from Incident")
	public Set<String> findAllNames();

	@Query(value = "select distinct incident.name from Incident incident inner join CameraIncident cameraIncident on incident.name=cameraIncident.incident.name where cameraIncident.camera.id=?2 and incident.type=?1")
	public List<String> findIncidentNamesByIncidentTypeAndCameraId(IncidentType incidentType, String cameraId);

}
