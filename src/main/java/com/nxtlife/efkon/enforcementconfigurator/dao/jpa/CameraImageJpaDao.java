package com.nxtlife.efkon.enforcementconfigurator.dao.jpa;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImage;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageResponse;

public interface CameraImageJpaDao extends JpaRepository<CameraImage, String> {

	public Boolean existsByIdAndActive(String id, Boolean active);

	public Boolean existsByIdAndActiveAndCameraId(String id, Boolean active, String cameraId);

	public CameraImageResponse findByIdAndActive(String id, Boolean active);

	@Modifying
	@Query(value = "update CameraImage set imageName = ?2, imageUrl = ?3, imageSize=?4, imageType=?5, modifiedBy.id =?6, modifiedAt =?7 where id =?1")
	public int update(String id, String imageName, String imageUrl, Long imageSize, String imageType, Long userId,
			Date date);

	@Modifying
	@Query(value = "update CameraImage set active = false, modifiedBy.id =?2, modifiedAt =?3 where id =?1")
	public int delete(String id, Long userId, Date date);

}
