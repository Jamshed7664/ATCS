package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateResponse;

public interface CameraImageCoordinateJpaDao extends JpaRepository<CameraImageCoordinate, Long> {

	public Boolean existsByIdAndActive(Long id, Boolean active);

	public CameraImageCoordinateResponse findByIdAndActiveAndCameraImageId(Long id, Boolean active, String cameraImageId);

	public CameraImageCoordinateResponse findByIdAndActiveAndCameraImageIdAndImageCoordinateType(Long id, Boolean active,
			String cameraImageId, ImageCoordinateType imageCoordinateType);

	public List<CameraImageCoordinateResponse> findByCameraImageIdAndActiveAndImageCoordinateTypeNot(String cameraImageId,
			Boolean active, ImageCoordinateType imageCoordinateType);

	public List<CameraImageCoordinateResponse> findByCameraImageIdAndActive(String cameraImageId, Boolean active);

	@Query(value = "select id from CameraImageCoordinate where cameraImage.id=?1 and active=?2 and imageCoordinateType=?3")
	public List<Long> findIdByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId, Boolean active,
			ImageCoordinateType imageCoordinateType);

	public Boolean existsByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId, Boolean active,
			ImageCoordinateType imageCoordinateType);

	public List<CameraImageCoordinateResponse> findByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId,
			Boolean active, ImageCoordinateType imageCoordinateType);

	public Long countByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId, Boolean active,
			ImageCoordinateType imageCoordinateType);

	@Modifying
	@Query(value = "update CameraImageCoordinate set aPointXCoordinate=?1,aPointYCoordinate=?2, bPointXCoordinate=?3,bPointYCoordinate=?4,cPointXCoordinate=?5,cPointYCoordinate=?6,dPointXCoordinate=?7,dPointYCoordinate=?8,modified_by =?10, modified_at =?11 where id =?9")
	public int updateZoneCoordinatesById(BigDecimal aPointXCoordinate, BigDecimal aPointYCoordinate,
			BigDecimal bPointXCoordinate, BigDecimal bPointYCoordinate, BigDecimal cPointXCoordinate,
			BigDecimal cPointYCoordinate, BigDecimal dPointXCoordinate, BigDecimal dPointYCoordinate, Long id,
			Long userId, Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set aPointXCoordinate=?1,aPointYCoordinate=?2, bPointXCoordinate=?3,bPointYCoordinate=?4,cPointXCoordinate=?5,cPointYCoordinate=?6,dPointXCoordinate=?7,dPointYCoordinate=?8,modified_by =?11, modified_at =?12 where cameraImage.id =?9 and imageCoordinateType=?10")
	public int updateCoordinatesByCameraImageIdAndImageCoordinateType(BigDecimal aPointXCoordinate,
			BigDecimal aPointYCoordinate, BigDecimal bPointXCoordinate, BigDecimal bPointYCoordinate,
			BigDecimal cPointXCoordinate, BigDecimal cPointYCoordinate, BigDecimal dPointXCoordinate,
			BigDecimal dPointYCoordinate, String cameraImageId, ImageCoordinateType imageCoordinateType, Long userId,
			Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set aPointXCoordinate=?1,aPointYCoordinate=?2, bPointXCoordinate=?3,bPointYCoordinate=?4,cPointXCoordinate=?5,cPointYCoordinate=?6,dPointXCoordinate=?7,dPointYCoordinate=?8,maxDistance=?9,roadLength=?10,roadBreadth=?11,modified_by =?14, modified_at =?15 where cameraImage.id =?12 and imageCoordinateType=?13")
	public int updateRoadAlignmentCoordinates(BigDecimal aPointXCoordinate, BigDecimal aPointYCoordinate,
			BigDecimal bPointXCoordinate, BigDecimal bPointYCoordinate, BigDecimal cPointXCoordinate,
			BigDecimal cPointYCoordinate, BigDecimal dPointXCoordinate, BigDecimal dPointYCoordinate,
			BigDecimal maxDistance, Double roadLength, Double roadBreadth, String cameraImageId,
			ImageCoordinateType imageCoordinateType, Long userId, Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set active = false, modified_by =?2, modified_at =?3 where id =?1")
	public int delete(Long id, Long userId, Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set active = false, modified_by =?3, modified_at =?4 where cameraImage.id =?1 and active=?2")
	public int deleteByCameraImageIdAndActive(String cameraImageId, Boolean active, Long userId, Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set active = false, modified_by =?4, modified_at =?5 where cameraImage.id =?1 and active=?2 and imageCoordinateType=?3")
	public int deleteByCameraImageIdAndActiveAndImageCoordinateType(String cameraImageId, Boolean active,
			ImageCoordinateType imageCoordinateType, Long userId, Date date);

	@Modifying
	@Query(value = "update CameraImageCoordinate set active = false, modified_by =?3, modified_at =?4 where cameraImage.id= ?1 and imageCoordinateType <> ?2")
	public int deleteByCameraImageIdAndImageCoordinateTypeNotEq(String cameraImageId,
			ImageCoordinateType imageCoordinateType, Long userId, Date date);

}
