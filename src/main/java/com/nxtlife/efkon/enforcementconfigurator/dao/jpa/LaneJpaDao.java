package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

public interface LaneJpaDao extends JpaRepository<Lane, String> {

	public List<LaneResponse> findByArmIdAndActive(String armId, Boolean active);

	public List<LaneResponse> findByLocationIdAndActive(String locationId, Boolean active);

	public LaneResponse findByIdAndActive(String id, Boolean active);

	public Boolean existsByIdAndArmIdAndLocationIdAndActive(String id, String armId, String locationId, Boolean active);

	public Boolean existsByCameraImageCoordinateIdAndActive(Long cameraImageCoordinateId, Boolean active);

	public LaneResponse findByCameraImageCoordinateIdAndActive(Long cameraImageCoordinateId, Boolean active);

	@Query(value = "select id from Lane where arm.id=?1 and active= ?2 order by createdAt asc")
	public List<String> findIdsByArmIdAndActive(String armId, Boolean active);

	@Query(value = "select cameraImageCoordinate.id from Lane lane where lane.id=?1 and active=?2")
	public Long findZoneIdByLaneIdAndActive(String laneId, Boolean active);

	@Query(value = "select id as id,arm.id as armId from Lane where location.id=?1 and active= ?2 order by createdAt asc")
	public List<Map<String, String>> findIdAndArmIdsByLocationIdAndActive(String locationId, Boolean active);

	@Query(value = "select lane.id  from Lane lane inner join CameraImageCoordinate cameraImageCoordinate on lane.cameraImageCoordinate.id=cameraImageCoordinate.id where cameraImageCoordinate.cameraImage.id=?1 and lane.active=?2 and cameraImageCoordinate.active=?2 and cameraImageCoordinate.imageCoordinateType=?3")
	public List<String> findIdByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId, Boolean active,
			ImageCoordinateType imageCoordinateType);

	public long countByArmId(String armId);

	@Modifying
	@Query(value = "update Lane set active = false, modified_by =?2, modified_at =?3 where id = ?1")
	public int delete(String id, Long userId, Date date);

	@Modifying
	@Query(value = "update Lane set active = false, modified_by =?2, modified_at =?3 where arm.id=?1")
	public int deleteByArmId(String armId, Long userId, Date date);

	@Modifying
	@Query(value = "update Lane set cameraImageCoordinate.id=NULL,modified_by =?2, modified_at =?3 where cameraImageCoordinate.id=?1")
	public int updateByZoneCoordinateId(Long zoneCoordinateId, Long userId, Date date);

	@Modifying
	@Query(value = "update Lane  set cameraImageCoordinate.id=?1 ,modified_by =?3, modified_at =?4  where id=?2")
	public int updateById(Long zoneCoordinate, String id, Long userId, Date date);

}
