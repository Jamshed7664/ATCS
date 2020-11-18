package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraIncident;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentAttributeValueResponse;

public interface CameraIncidentJpaDao extends JpaRepository<CameraIncident, Long> {

	public List<CameraIncidentAttributeValueResponse> findByCameraId(String cameraId);

	public List<CameraIncidentAttributeValueResponse> findByCameraIdAndIncidentTypeOrderByIncidentNameAscIncidentAttributeIdAsc(
			String cameraId, IncidentType incidentType);

	@Modifying
	@Query(value = "insert into camera_incident(camera_id,incident_name,incident_attribute_id,value) values (?1,?2,?3,?4)", nativeQuery = true)
	public int save(String cameraId, String incidentName, Long attributeId, String value);

	@Query(value = "select incident.name as incidentName,attribute.id as attributeId,attribute.name as attributeName,attribute.dataType as dataType,attribute.unit as unit,cameraIncident.value as value from CameraIncident cameraIncident inner join Incident incident on cameraIncident.incident.name=incident.name inner join IncidentAttribute attribute on cameraIncident.incidentAttribute.id=attribute.id where cameraIncident.camera.id=?1 and incident.type=?2 order by incident.name")
	public List<Map<String, Object>> findCameraIncidentAttributesByCameraId(String cameraId, IncidentType incidentType);

	@Query(value = "select distinct incident.name from CameraIncident cameraIncident inner join Incident incident on cameraIncident.incident.name=incident.name where cameraIncident.camera.id=?2 and incident.type=?1")
	public List<String> findIncidentNamesByIncidentTypeAndCameraId(IncidentType incidentType, String cameraId);

	@Query(value = "select distinct incident.name from CameraIncident where camera.id=?1 order by incident.name")
	public List<String> findIncidentNamesByCameraId(String cameraId);

	@Query(value = "select distinct camera.id as id,camera.code as code from CameraIncident cameraIncident inner join EquipmentMapping equipmentMapping on cameraIncident.camera.id=equipmentMapping.equipment.id inner join Camera camera on camera.id=equipmentMapping.equipment.id where equipmentMapping.arm.id=?2 and equipmentMapping.lane.id=?3 and equipmentMapping.active=?4 and cameraIncident.camera.id<>?1 ")
	public List<Map<String, Object>> findCameraCodeByArmIdAndLaneIdAndActive(String cameraId, String armId,
			String laneId, Boolean active);

	@Query(value = "select distinct camera.id as id,camera.code as code from CameraIncident cameraIncident inner join EquipmentMapping equipmentMapping on cameraIncident.camera.id=equipmentMapping.equipment.id inner join Camera camera on camera.id=equipmentMapping.equipment.id where equipmentMapping.arm.id=?2 and equipmentMapping.lane.id is null and equipmentMapping.active=?3 and cameraIncident.camera.id<>?1 ")
	public List<Map<String, Object>> findCameraCodeByArmIdAndActiveAndLaneIdIsNULL(String cameraId, String armId,
			Boolean active);

	@Modifying
	@Query(value = "update CameraIncident set value=?4 where camera.id=?2 and incident.name=?1 and incidentAttribute.id=?3")
	public int updateValue(String incidentName, String cameraId, Long incidentAttributeId, String value);

	@Modifying
	@Query(value = "update CameraIncident set active=false,modifiedBy.id=?3,modified_at=?4 where camera.id=?2 and incident.name in ?1")
	public int deleteByIncidentNamesAndCameraId(List<String> incidentNames, String cameraId, Long userId,
			Date modifiedAt);

	@Modifying
	@Query(value = "update CameraIncident set active=false,modifiedBy.id=?2,modified_at=?3  where camera.id=?1")
	public int deleteByCameraId(String cameraId, Long userId, Date modifiedAt);
}
