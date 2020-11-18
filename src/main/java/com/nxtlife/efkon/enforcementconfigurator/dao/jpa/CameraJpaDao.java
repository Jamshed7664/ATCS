package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;

public interface CameraJpaDao extends JpaRepository<Camera, String> {

	public CameraResponse findByEquipmentId(String equipmentId);

	public CameraResponse findResponseById(String id);

	@Query(value = "select isIncidentDetect from Camera where id = ?1")
	public Boolean findIsIncidentDetectById(String id);

	@Query(value = "select incidentScheduling from Camera where id=?1")
	public Integer findIncidentSchedulingByCameraId(String cameraId);

	@Modifying
	@Query(value = "update Camera set isIncidentDetect = ?2, modifiedBy.id  = ?3, modifiedAt =?4 where id =?1")
	public int updateIsIncidentDetect(String id, Boolean isIncidentDetect, Long userId, Date modifiedAt);

	@Modifying
	@Query(value = "update Camera set isRedSignalDetect = ?2, modifiedBy.id  = ?3, modifiedAt =?4 where id =?1")
	public int updateIsRedSignalDetect(String id, Boolean isRedSignalDetect, Long userId, Date modifiedAt);

	@Modifying
	@Query(value = "update Camera set isRoadAlignment = ?2, modifiedBy.id  = ?3, modifiedAt =?4 where id =?1")
	public int updateIsRoadAlignment(String id, Boolean isRoadAlignment, Long userId, Date modifiedAt);

	@Modifying
	@Query(value = "update Camera set incidentScheduling=?1,modifiedBy.id  = ?3, modified_At =?4 where id=?2")
	public int updateIncidentScheduling(Integer incidentSchedule, String id, Long userId, Date modifiedAt);

}
