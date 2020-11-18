package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraImageCoordinateJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraImageJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LaneJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImage;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.enums.CameraType;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraService;
import com.nxtlife.efkon.enforcementconfigurator.service.FileStorageService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

@Service("cameraServiceImpl")
@Transactional
public class CameraServiceImpl extends BaseService implements CameraService {

	private static Logger logger = LoggerFactory.getLogger(CameraServiceImpl.class);

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private CameraJpaDao cameraJpaDao;

	@Autowired
	private CameraImageJpaDao cameraImageJpaDao;

	@Autowired
	private LaneJpaDao laneJpaDao;

	@Autowired
	private CameraImageCoordinateJpaDao cameraImageCoordinateJpaDao;

	@Autowired
	private FileStorageService<MultipartFile> fileStorageService;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	/**
	 * this method used to validate camera request. this method validate equipment
	 * id exists for organization or camera type is valid or not
	 *
	 * @param equipmentId
	 * @param request
	 */
	private void validate(String equipmentId, Long organizationId, CameraRequest request) {
		EquipmentResponse equipment = equipmentJpaDao.findByIdAndOrganizationIdAndActive(equipmentId, organizationId,
				true);
		if (equipment == null) {
			throw new NotFoundException(String.format("This active equipment(%s) not found", equipmentId));
		}
		request.setCode(equipment.getName());
		if (!equipment.getEquipmentTypeName().equalsIgnoreCase("Camera")) {
			throw new ValidationException(String.format("Camera detail can be saved for only EquipmentType Camera"));
		}
		if (request.getCameraType() != null && !CameraType.matches(request.getCameraType())) {
			throw new ValidationException(String.format("This is not valid camera type(%s)", request.getCameraType()));
		}
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_UPDATE)
	public CameraResponse save(String equipmentId, CameraRequest request) {
		validate(equipmentId, getUser().gettOrganizationId(), request);
		CameraResponse cameraResponse = cameraJpaDao.findByEquipmentId(equipmentId);
		Camera camera = request.toEntity();
		camera.setId(equipmentId);
		if (cameraResponse != null) {
			camera.setCreatedAt(cameraResponse.getCreatedAt());
			if (cameraResponse.getCreatedById() != null) {
				User createdBy = new User();
				createdBy.setId(cameraResponse.getCreatedById());
				camera.setCreatedBy(createdBy);
			}
		}
		cameraJpaDao.save(camera);
		return cameraJpaDao.findByEquipmentId(equipmentId);
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_FETCH)
	public CameraResponse findByEquipmentId(String equipmentId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(equipmentId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("There is no active equipment(%s) found", equipmentId));
		}
		CameraResponse camera = cameraJpaDao.findByEquipmentId(equipmentId);
		if (camera == null) {
			return new CameraResponse(equipmentId, null, null, null, null, null, null,null, null, null, null, null, null,
					null);
		}
		return camera;
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_LANE_FETCH)
	public List<LaneResponse> findByCameraId(String cameraId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("There is no active camera(%s) found", cameraId));
		}
		if (!cameraJpaDao.existsById(cameraId)) {
			throw new NotFoundException(String.format("Camera (%s) not found", cameraId));
		}
		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraId, true);
		if (equipmentMapping == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any location "));
		}
		List<LaneResponse> lanes = null;
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any arm in location (%s)",
					equipmentMapping.getLocationId()));
		} else {
			if (equipmentMapping.getLaneId() == null) {
				lanes = laneJpaDao.findByArmIdAndActive(equipmentMapping.getArmId(), true);
			} else {
				lanes = new ArrayList<>();
				lanes.add(laneJpaDao.findByIdAndActive(equipmentMapping.getLaneId(), true));
			}
		}
		return lanes;
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_UPDATE)
	public SuccessResponse uploadImage(String id, MultipartFile image) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(id, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("There is no active camera(%s) found", id));
		}
		if (!cameraJpaDao.existsById(id)) {
			throw new NotFoundException(String.format("Camera (%s) not found", id));
		}
		int rows;
		List<String> mappedLaneIds = null;
		if (cameraImageCoordinateJpaDao.existsByCameraImageIdAndActiveAndImageCoordinateType(id, true,
				ImageCoordinateType.INCIDENT_DETECTION)) {
			mappedLaneIds = laneJpaDao.findIdByCameraImageIdAndActiveAndImageCoordinateType(id, true,
					ImageCoordinateType.ZONE);
			if (!mappedLaneIds.isEmpty()) {
				throw new ValidationException(String.format(
						"Some of the lanes are mapped with zone coordinates exist in this camera image (%s)", id));
			}
			rows = cameraImageCoordinateJpaDao.deleteByCameraImageIdAndActive(id, true, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Image coordinates successfully deleted");
			}
		}
		String imageUrl = fileStorageService.storeFile(image, image.getOriginalFilename(), "/camera-image/", true,
				true);
		if (cameraImageJpaDao.existsById(id)) {

			cameraImageJpaDao.update(id, image.getOriginalFilename(), imageUrl, image.getSize(), image.getContentType(),
					getUserId(), new Date());
		} else {
			cameraImageJpaDao.save(new CameraImage(id, imageUrl, image.getOriginalFilename(), image.getSize(),
					image.getContentType()));
		}

		return new SuccessResponse(HttpStatus.OK.value(), "Image uploaded successfully");

	}

	@Override
	@Secured(AuthorityUtils.CAMERA_FETCH)
	public CameraImageResponse fetchImage(String id) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(id, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("There is no active camera(%s) found", id));
		}
		CameraImageResponse cameraImage = cameraImageJpaDao.findByIdAndActive(id, true);
		if (cameraImage == null) {
			throw new NotFoundException(String.format("Camera imaage(%s) not found", id));
		}
		byte[] imageData;
		try {
			imageData = fileStorageService.loadFileAsByte(cameraImage.getImageUrl());
			cameraImage.setImageData(imageData);
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Couldn't able to convert image {} into resource", cameraImage.getImageUrl());
		}
		return cameraImage;
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_IMAGE_DELETE)
	public SuccessResponse delete(String id, String cameraId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("There is no active camera(%s) found", id));
		}
		if (!cameraImageJpaDao.existsByIdAndActiveAndCameraId(id, true, cameraId)) {
			throw new NotFoundException(String.format("This image (%s) not exist in camera (%s)", id, cameraId));
		}
		int rows;
		List<String> mappedLaneIds = null;
		if (cameraImageCoordinateJpaDao.existsByCameraImageIdAndActiveAndImageCoordinateType(id, true,
				ImageCoordinateType.INCIDENT_DETECTION)) {
			mappedLaneIds = laneJpaDao.findIdByCameraImageIdAndActiveAndImageCoordinateType(id, true,
					ImageCoordinateType.ZONE);
			if (!mappedLaneIds.isEmpty()) {
				throw new ValidationException(String.format(
						"Some of the lanes are mapped with zone coordinates exist in this camera image (%s)", id));
			}
			rows = cameraImageCoordinateJpaDao.deleteByCameraImageIdAndActive(id, true, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Image coordinates successfully deleted");
			}
			rows = 0;
		}
		rows = cameraImageJpaDao.delete(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Camera Image {} successfully deleted", id);
		}
		return new SuccessResponse(HttpStatus.OK.value(), String.format("Camera image (%s) successfully deleted", id));

	}

}
