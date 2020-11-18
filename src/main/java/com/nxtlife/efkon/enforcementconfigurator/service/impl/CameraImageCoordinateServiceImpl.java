package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraImageCoordinateJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraImageJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraIncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LaneJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.ZoneIncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraImageCoordinateService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.util.MathUtil;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.common.RoadAlignmentPropertyResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

@Service("cameraImageCoordinateServiceImpl")
@Transactional
public class CameraImageCoordinateServiceImpl extends BaseService implements CameraImageCoordinateService {

	private static Logger logger = LoggerFactory.getLogger(CameraImageCoordinateServiceImpl.class);

	@Value("${efkon.junction.code}")
	private String junctionCode;

	@Value("${efkon.highway.code}")
	private String highwayCode;

	@Autowired
	private CameraJpaDao cameraJpaDao;

	@Autowired
	private CameraImageCoordinateJpaDao cameraImageCoordinateJpaDao;

	@Autowired
	private CameraImageJpaDao cameraImageJpaDao;

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private LaneJpaDao laneJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	@Autowired
	private LocationJpaDao locationJpaDao;

	@Autowired
	private CameraIncidentJpaDao cameraIncidentJpaDao;

	@Autowired
	private ZoneIncidentJpaDao zoneIncidentJpaDao;

	private void validateImageCoordinates(String cameraImageId, CameraImageCoordinateListRequest request) {
		Set<String> requestCoordinateTypes = new HashSet<>();
		CameraImageCoordinateResponse mainCoordinates = null;
		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraImageId,
				true);
		if (equipmentMapping == null) {
			throw new ValidationException(
					String.format("This camera (%s) not mapped with any location ", cameraImageId));
		}
		LocationResponse locationResponse = locationJpaDao.findResponseById(equipmentMapping.getLocationId());
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format(
					"This camera not mapped with any arm or lane in location (%s). Only arm or lane camera used for incident detection or red lamp detection",
					equipmentMapping.getLocationId()));
		}
		for (CameraImageCoordinateRequest imageCoordinate : request.getImageCoordinates()) {
			if (imageCoordinate.getImageCoordinateType().equals(ImageCoordinateType.RED_LAMP.name())) {
				if (locationResponse.getLocationTypeCode().equalsIgnoreCase(highwayCode)) {
					throw new ValidationException("Red lamp coordinates not valid for highway");
				}
				if (equipmentMapping.getLaneId() != null) {
					throw new ValidationException("Red lamp coordinates not valid for lane");
				}
			}
			if (imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.INCIDENT_DETECTION.name())) {
				mainCoordinates = new CameraImageCoordinateResponse(null, imageCoordinate.getaPointXCoordinate(),
						imageCoordinate.getaPointYCoordinate(), imageCoordinate.getbPointXCoordinate(),
						imageCoordinate.getbPointYCoordinate(), imageCoordinate.getcPointXCoordinate(),
						imageCoordinate.getcPointYCoordinate(), imageCoordinate.getdPointXCoordinate(),
						imageCoordinate.getdPointYCoordinate(), null, null, null, null, null, null, null);
			}

			if (imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.ROAD_ALIGNMENT.name())) {
				if (request.getRoadAlignmentProperties() == null) {
					throw new ValidationException(
							"Road Alignment coordinates can't be saved without road alignment properties");
				} else {
					if (mainCoordinates == null) {
						throw new ValidationException(
								"Road alignment can't be saved without main detector coordinates");
					}
				}
				boolean inside = (MathUtil.isInside(mainCoordinates, imageCoordinate.getaPointXCoordinate(),
						imageCoordinate.getaPointYCoordinate())
						&& MathUtil.isInside(mainCoordinates, imageCoordinate.getbPointXCoordinate(),
								imageCoordinate.getbPointYCoordinate())
						&& MathUtil.isInside(mainCoordinates, imageCoordinate.getbPointXCoordinate(),
								imageCoordinate.getbPointYCoordinate())
						&& MathUtil.isInside(mainCoordinates, imageCoordinate.getbPointXCoordinate(),
								imageCoordinate.getbPointYCoordinate()));
				if (!inside) {
					throw new ValidationException("Road alignment coordinates should be inside of main coordinate");
				}
			}
			if (requestCoordinateTypes.contains(imageCoordinate.getImageCoordinateType())) {
				throw new ValidationException("Two image coordinates with same coordinate type can't be saved");
			}
			if (!imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.INCIDENT_DETECTION.name())
					&& !imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.RED_LAMP.name())
					&& !imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.ROAD_ALIGNMENT.name())) {
				throw new ValidationException(String.format("Image coordinate type (%s) not valid",
						imageCoordinate.getImageCoordinateType()));
			}
			requestCoordinateTypes.add(imageCoordinate.getImageCoordinateType());
		}
	}

	private void validateLane(CameraImageCoordinateRequest requestZoneCoordinate, Set<String> armLaneIds,
			Map<String, Long> laneZoneMap, LaneResponse lane, EquipmentMappingResponse equipmentMapping) {

		if (equipmentMapping.getLaneId() != null) {
			if (!requestZoneCoordinate.getLaneId().equals(equipmentMapping.getLaneId())) {
				throw new ValidationException(String.format("This lane (%s) not mapped with camera (%s)",
						requestZoneCoordinate.getLaneId(), equipmentMapping.getEquipmentId()));
			} else {
				if (lane.getCameraImageCoordinateId() != null && (requestZoneCoordinate.getId() == null
						|| !unmask(requestZoneCoordinate.getId()).equals(lane.getCameraImageCoordinateId()))) {
					throw new ValidationException(String.format("This lane (%s) already mapped with zone (%s)",
							requestZoneCoordinate.getLaneId(), mask(lane.getCameraImageCoordinateId())));
				}
			}
		} else {
			if (!armLaneIds.contains(requestZoneCoordinate.getLaneId())) {
				throw new ValidationException(String.format("This lane (%s) not exist in arm (%s) and location (%s)",
						requestZoneCoordinate.getLaneId(), equipmentMapping.getArmId(),
						equipmentMapping.getLocationId()));
			} else {
				if (laneZoneMap.get(requestZoneCoordinate.getLaneId()) != null
						&& (requestZoneCoordinate.getId() == null || !unmask(requestZoneCoordinate.getId())
								.equals(laneZoneMap.get(requestZoneCoordinate.getLaneId())))) {
					throw new ValidationException(String.format("This lane (%s) already mapped with zone (%s)",
							requestZoneCoordinate.getLaneId(),
							mask(laneZoneMap.get(requestZoneCoordinate.getLaneId()))));
				}
			}
		}
	}

	/**
	 * this method used to validate zone coordinates
	 *
	 * @param request
	 * @param existZonesCoordinates
	 * @param existMainCoordinates
	 * @return
	 * @throws ValidationException if zone coordinate is same as main detector
	 *                             coordinates if zone coordinate is same as zone
	 *                             coordinate exist in database if zones coordinates
	 *                             not inside the main detector coordinates
	 */

	private void validateZoneCoordinates(CameraImageCoordinateRequest request,
			List<CameraImageCoordinateResponse> existZonesCoordinates,
			CameraImageCoordinateResponse existMainCoordinates) {

		if (equalsCoordinates(existMainCoordinates, request))
			throw new ValidationException("Zone coordinates can't be same as main detector coordinates");

		existZonesCoordinates.forEach(zoneCoordinates -> {
			if (request.getId() == null || !request.getId().equals(zoneCoordinates.getId())) {
				if (equalsCoordinates(zoneCoordinates, request))
					throw new ValidationException(String.format("This zone coordinates is same as zone coordinate (%s)",
							zoneCoordinates.getId()));
			}
		});

		boolean inside = (MathUtil.isInside(existMainCoordinates, request.getaPointXCoordinate(),
				request.getaPointYCoordinate())
				&& MathUtil.isInside(existMainCoordinates, request.getbPointXCoordinate(),
						request.getbPointYCoordinate())
				&& MathUtil.isInside(existMainCoordinates, request.getbPointXCoordinate(),
						request.getbPointYCoordinate())
				&& MathUtil.isInside(existMainCoordinates, request.getbPointXCoordinate(),
						request.getbPointYCoordinate()));
		if (!inside) {
			throw new ValidationException("Zone coordinates not valid");
		}

	}

	/**
	 * this method used to validate zone incident
	 *
	 * @param requestIncidentNames
	 * @param existIncidentNames
	 * @throws ValidationException when incident names not exist
	 */

	private void validateZoneIncident(Set<String> requestIncidentNames, List<String> existIncidentNames) {
		requestIncidentNames.removeAll(existIncidentNames);
		if (!requestIncidentNames.isEmpty()) {
			throw new ValidationException(String.format("Some incidents (%s) not exist", requestIncidentNames));
		}

	}

	/**
	 * this method used to validate incidentScheduling when zone maps with incidents
	 * and validate zone id
	 *
	 * @param cameraImageId
	 * @param zoneId
	 * @throws ValidationException when camera image coordinates not filled or
	 *                             scheduling is on
	 * @throws NotFoundException   when zone id is incorrect
	 */

	private void validateZoneScheduling(String cameraImageId, Long zoneId) {
		Integer incidentScheduling = cameraJpaDao.findIncidentSchedulingByCameraId(cameraImageId);
		if (incidentScheduling == null) {
			throw new ValidationException(String.format("Camera image (%s) coordinates not filled yet", cameraImageId));
		} else {
			if (incidentScheduling != 0) {
				throw new ValidationException(String.format("Scheduling should be off for save zone incidents"));
			}
		}
		CameraImageCoordinateResponse imageCoordinate = cameraImageCoordinateJpaDao
				.findByIdAndActiveAndCameraImageId(unmask(zoneId), true, cameraImageId);
		if (imageCoordinate == null) {
			throw new NotFoundException(
					String.format("This zone coordinate (%s) not found in camera image (%s)", zoneId, cameraImageId));
		} else {
			if (!imageCoordinate.getImageCoordinateType().equals(ImageCoordinateType.ZONE)) {
				throw new NotFoundException(String.format("This image coordinate (%s) not zone coordinate ", zoneId));
			}
		}
	}

	/**
	 * this method used to check coordinates whether they are equal or not
	 *
	 * @param existCoordinates   {@Link CameraImageCoordinateResponse}
	 * @param requestCoordinates {@Link CameraImageCoordinateRequest}
	 * @return boolean
	 */
	private boolean equalsCoordinates(CameraImageCoordinateResponse existCoordinates,
			CameraImageCoordinateRequest requestCoordinates) {
		boolean match = false;
		if (existCoordinates.getaPointXCoordinate().compareTo(requestCoordinates.getaPointXCoordinate()) == 0
				&& existCoordinates.getaPointYCoordinate().compareTo(requestCoordinates.getaPointYCoordinate()) == 0
				&& existCoordinates.getbPointXCoordinate().compareTo(requestCoordinates.getbPointXCoordinate()) == 0
				&& existCoordinates.getbPointYCoordinate().compareTo(requestCoordinates.getbPointYCoordinate()) == 0
				&& existCoordinates.getcPointXCoordinate().compareTo(requestCoordinates.getcPointXCoordinate()) == 0
				&& existCoordinates.getcPointYCoordinate().compareTo(requestCoordinates.getcPointYCoordinate()) == 0
				&& existCoordinates.getdPointXCoordinate().compareTo(requestCoordinates.getdPointXCoordinate()) == 0
				&& existCoordinates.getdPointYCoordinate().compareTo(requestCoordinates.getdPointYCoordinate()) == 0) {
			match = true;
		}
		return match;

	}

	@Override
	@Secured(AuthorityUtils.IMAGE_COORDINATE_CREATE)
	public SuccessResponse saveImageCoordinates(String cameraImageId, CameraImageCoordinateListRequest request) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found", cameraImageId));
		}

		request.getImageCoordinates().sort(new Comparator<CameraImageCoordinateRequest>() {
			@Override
			public int compare(CameraImageCoordinateRequest o1, CameraImageCoordinateRequest o2) {
				if (o1.getImageCoordinateType() == null || o2.getImageCoordinateType() == null) {
					throw new ValidationException("Image coordinate type can't be null");
				}
				return o1.getImageCoordinateType().compareTo(o2.getImageCoordinateType());

			}
		});
		validateImageCoordinates(cameraImageId, request);
		Set<String> existCoordinateTypes = new HashSet<>();
		CameraImageCoordinateResponse existMainCoordinates = null;
		List<CameraImageCoordinate> cameraImageCoordinates = new ArrayList<>();
		List<CameraImageCoordinateResponse> existImageCoordinates = cameraImageCoordinateJpaDao
				.findByCameraImageIdAndActiveAndImageCoordinateTypeNot(cameraImageId, true, ImageCoordinateType.ZONE);
		for (CameraImageCoordinateResponse imageCoordinate : existImageCoordinates) {
			existCoordinateTypes.add(imageCoordinate.getImageCoordinateType().toString());
			if (imageCoordinate.getImageCoordinateType().equals(ImageCoordinateType.INCIDENT_DETECTION)) {
				existMainCoordinates = imageCoordinate;
			}
		}

		for (CameraImageCoordinateRequest imageCoordinate : request.getImageCoordinates()) {
			if (existCoordinateTypes.contains(imageCoordinate.getImageCoordinateType())) {
				existCoordinateTypes.remove(imageCoordinate.getImageCoordinateType());
				if (imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.INCIDENT_DETECTION.name())) {
					if (!equalsCoordinates(existMainCoordinates, imageCoordinate)) {
						cameraImageCoordinateJpaDao.deleteByCameraImageIdAndActiveAndImageCoordinateType(cameraImageId,
								true, ImageCoordinateType.ZONE, getUserId(), new Date());
					}
				} else if (imageCoordinate.getImageCoordinateType()
						.matches(ImageCoordinateType.ROAD_ALIGNMENT.name())) {
					int row = cameraImageCoordinateJpaDao.updateRoadAlignmentCoordinates(
							imageCoordinate.getaPointXCoordinate(), imageCoordinate.getaPointYCoordinate(),
							imageCoordinate.getbPointXCoordinate(), imageCoordinate.getbPointYCoordinate(),
							imageCoordinate.getcPointXCoordinate(), imageCoordinate.getcPointYCoordinate(),
							imageCoordinate.getdPointXCoordinate(), imageCoordinate.getdPointYCoordinate(),
							request.getRoadAlignmentProperties().getMaxDistance(),
							request.getRoadAlignmentProperties().getRoadLength(),
							request.getRoadAlignmentProperties().getRoadBreadth(), cameraImageId,
							ImageCoordinateType.ROAD_ALIGNMENT, getUserId(), new Date());
					if (row > 0) {
						logger.info("Image coordinate {} successfully updated",
								imageCoordinate.getImageCoordinateType());
					}
					continue;
				}
				int row = cameraImageCoordinateJpaDao.updateCoordinatesByCameraImageIdAndImageCoordinateType(
						imageCoordinate.getaPointXCoordinate(), imageCoordinate.getaPointYCoordinate(),
						imageCoordinate.getbPointXCoordinate(), imageCoordinate.getbPointYCoordinate(),
						imageCoordinate.getcPointXCoordinate(), imageCoordinate.getcPointYCoordinate(),
						imageCoordinate.getdPointXCoordinate(), imageCoordinate.getdPointYCoordinate(), cameraImageId,
						ImageCoordinateType.valueOf(imageCoordinate.getImageCoordinateType()), getUserId(), new Date());
				if (row > 0) {
					logger.info("Image coordinate {} successfully updated", imageCoordinate.getImageCoordinateType());
				}
			} else {
				CameraImageCoordinate cameraImageCoordinate = imageCoordinate.toEntity();
				cameraImageCoordinate
						.setImageCoordinateType(ImageCoordinateType.valueOf(imageCoordinate.getImageCoordinateType()));
				cameraImageCoordinate.settCameraImageId(cameraImageId);
				if (imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.INCIDENT_DETECTION.name())) {
					cameraJpaDao.updateIsIncidentDetect(cameraImageId, true, getUserId(), new Date());
				} else if (imageCoordinate.getImageCoordinateType().matches(ImageCoordinateType.RED_LAMP.name())) {
					cameraJpaDao.updateIsRedSignalDetect(cameraImageId, true, getUserId(), new Date());
				} else {
					cameraJpaDao.updateIsRoadAlignment(cameraImageId, true, getUserId(), new Date());
					cameraImageCoordinate.setMaxDistance(request.getRoadAlignmentProperties().getMaxDistance());
					cameraImageCoordinate.setRoadLength(request.getRoadAlignmentProperties().getRoadLength());
					cameraImageCoordinate.setRoadBreadth(request.getRoadAlignmentProperties().getRoadBreadth());
				}
				cameraImageCoordinates.add(cameraImageCoordinate);
			}
		}
		if (!existCoordinateTypes.isEmpty()) {
			existCoordinateTypes.forEach(coordinateType -> {
				if (coordinateType.matches(ImageCoordinateType.INCIDENT_DETECTION.name())) {
					cameraJpaDao.updateIsIncidentDetect(cameraImageId, false, getUserId(), new Date());
				} else if (coordinateType.matches(ImageCoordinateType.RED_LAMP.name())) {
					cameraJpaDao.updateIsRedSignalDetect(cameraImageId, false, getUserId(), new Date());
				} else {
					cameraJpaDao.updateIsRoadAlignment(cameraImageId, false, getUserId(), new Date());
				}
				cameraImageCoordinateJpaDao.deleteByCameraImageIdAndActiveAndImageCoordinateType(cameraImageId, true,
						ImageCoordinateType.valueOf(coordinateType), getUserId(), new Date());
			});
		}

		if (request.getIncidentScheduling() != null && request.getIncidentScheduling()) {
			cameraJpaDao.updateIncidentScheduling(1, cameraImageId, getUserId(), new Date());
		} else {
			cameraJpaDao.updateIncidentScheduling(0, cameraImageId, getUserId(), new Date());
		}
		cameraImageCoordinateJpaDao.saveAll(cameraImageCoordinates);
		logger.info("Camera Image {} coordinates successfully saved", cameraImageId);
		return new SuccessResponse(HttpStatus.OK.value(),
				String.format("Camera image (%s) coordinates successfully saved", cameraImageId));
	}

	@Override
	@Secured(AuthorityUtils.IMAGE_COORDINATE_CREATE)
	public CameraImageCoordinateResponse saveZoneCoordinates(String cameraImageId,
			CameraImageCoordinateRequest request) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found", cameraImageId));
		}
		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraImageId,
				true);
		if (equipmentMapping == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any location "));
		}
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format(
					"This camera not mapped with any arm or lane in location (%s). Only arm or lane camera used for create zones",
					equipmentMapping.getLocationId()));
		} else {
			if (request.getLaneId() != null) {
				LaneResponse lane = null;
				List<LaneResponse> lanes = null;
				Set<String> armLaneIds = new HashSet<>();
				Map<String, Long> laneZoneMap = new HashMap<>();
				if (equipmentMapping.getLaneId() == null) {
					lanes = laneJpaDao.findByArmIdAndActive(equipmentMapping.getArmId(), true);
					lanes.forEach(l -> {
						armLaneIds.add(l.getId());
						laneZoneMap.putIfAbsent(l.getId(), l.getCameraImageCoordinateId());
					});
				} else {
					lane = laneJpaDao.findByIdAndActive(equipmentMapping.getLaneId(), true);
				}
				validateLane(request, armLaneIds, laneZoneMap, lane, equipmentMapping);
			}
		}

		if (!cameraJpaDao.findIsIncidentDetectById(cameraImageId)) {
			throw new ValidationException(
					"You can't save zone coordinate because this camera is not for incident detection");
		}
		CameraImageCoordinateResponse existZoneCoordinates = null;
		if (request.getId() != null) {
			existZoneCoordinates = cameraImageCoordinateJpaDao.findByIdAndActiveAndCameraImageIdAndImageCoordinateType(
					unmask(request.getId()), true, cameraImageId, ImageCoordinateType.ZONE);
			if (existZoneCoordinates == null) {
				throw new ValidationException(String.format("This zone coordinate (%s) not exist in camera image (%s)",
						request.getId(), cameraImageId));
			}
		}
		List<CameraImageCoordinateResponse> existZonesCoordinates = new ArrayList<>(), existImageCoordinates;
		existImageCoordinates = cameraImageCoordinateJpaDao.findByCameraImageIdAndActive(cameraImageId, true);
		CameraImageCoordinateResponse existMainCoordinates = null;
		for (CameraImageCoordinateResponse existCoordinates : existImageCoordinates) {
			if (existCoordinates.getImageCoordinateType().equals(ImageCoordinateType.ZONE)) {
				existZonesCoordinates.add(existCoordinates);
			}
			if (existCoordinates.getImageCoordinateType().equals(ImageCoordinateType.INCIDENT_DETECTION))
				existMainCoordinates = existCoordinates;
		}
		if (existMainCoordinates == null) {
			throw new ValidationException(
					String.format("Main detector coordinates does't exist in camera image (%s)", cameraImageId));
		}
		validateZoneCoordinates(request, existZonesCoordinates, existMainCoordinates);
		int sequence = existZonesCoordinates.size() + 1;
		CameraImageCoordinate zoneCoordinate = request.toEntity();
		zoneCoordinate.settCameraImageId(cameraImageId);
		zoneCoordinate.setImageCoordinateType(ImageCoordinateType.ZONE);
		if (existZoneCoordinates != null) {
			laneJpaDao.updateByZoneCoordinateId(unmask(request.getId()), getUserId(), new Date());
			zoneCoordinate.setId(unmask(existZoneCoordinates.getId()));
			zoneCoordinate.setCreatedAt(existZoneCoordinates.getCreatedAt());
			if (existZoneCoordinates.getCreatedById() != null) {
				User createdBy = new User();
				createdBy.setId(existZoneCoordinates.getCreatedById());
				zoneCoordinate.setCreatedBy(createdBy);
			}
			sequence = existZoneCoordinates.getSequence();
		}
		zoneCoordinate.setSequence(sequence);
		zoneCoordinate = cameraImageCoordinateJpaDao.save(zoneCoordinate);
		CameraImageCoordinateResponse response = CameraImageCoordinateResponse.get(zoneCoordinate);
		if (request.getLaneId() != null) {
			Lane existLane = laneJpaDao.findById(request.getLaneId()).get();
			existLane.setCameraImageCoordinate(zoneCoordinate);
			laneJpaDao.save(existLane);
			response.setLane(LaneResponse.get(existLane));
		}

		return response;
	}

	@Override
	@Secured(AuthorityUtils.IMAGE_COORDINATE_FETCH)
	public List<CameraImageCoordinateResponse> findZoneCoordinates(String cameraImageId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found"));
		}
		return cameraImageCoordinateJpaDao.findByCameraImageIdAndActiveAndImageCoordinateType(cameraImageId, true,
				ImageCoordinateType.ZONE);
	}

	@Override
	@Secured(AuthorityUtils.IMAGE_COORDINATE_FETCH)
	public CameraImageCoordinateListResponse findCameraImageCoordinates(String cameraImageId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found", cameraImageId));
		}
		CameraImageCoordinateListResponse cameraImageCoordinates = new CameraImageCoordinateListResponse();
		List<CameraImageCoordinateResponse> imageCoordinates = cameraImageCoordinateJpaDao
				.findByCameraImageIdAndActiveAndImageCoordinateTypeNot(cameraImageId, true, ImageCoordinateType.ZONE);
		cameraImageCoordinates.setImageCoordinates(imageCoordinates);
		imageCoordinates.forEach(coordinates -> {
			if (coordinates.getImageCoordinateType().equals(ImageCoordinateType.ROAD_ALIGNMENT)) {
				cameraImageCoordinates.setRoadAlignmentProperties(new RoadAlignmentPropertyResponse(
						coordinates.getRoadLength(), coordinates.getRoadBreadth(), coordinates.getMaxDistance()));
			}
		});
		cameraImageCoordinates.setIncidentScheduling(
				cameraJpaDao.findIncidentSchedulingByCameraId(cameraImageId) == 0 ? false : true);
		return cameraImageCoordinates;

	}

	@Override
	@Secured(AuthorityUtils.ZONE_INCIDENT_CREATE)
	public SuccessResponse saveZoneIncidents(String cameraImageId, Long zoneId, ZoneIncidentRequest request) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found", cameraImageId));
		}
		validateZoneScheduling(cameraImageId, zoneId);
		List<String> existIncidentNames = cameraIncidentJpaDao.findIncidentNamesByCameraId(cameraImageId);
		if (existIncidentNames.isEmpty()) {
			throw new ValidationException(String.format("This camera (%s) not define any incident", cameraImageId));
		}
		validateZoneIncident(new HashSet<>(request.getIncidentNames()), existIncidentNames);
		List<String> existZoneIncidents = zoneIncidentJpaDao.findIncidentNamesByZoneId(unmask(zoneId));
		for (String incident : request.getIncidentNames()) {
			if (existZoneIncidents.contains(incident)) {
				existZoneIncidents.remove(incident);
			} else {
				int row = zoneIncidentJpaDao.save(unmask(zoneId), incident);
				if (row > 0) {
					logger.info("Zone {} incident {} successfully saved", unmask(zoneId), incident);
				}
			}
		}
		if (!existZoneIncidents.isEmpty()) {
			int rows = zoneIncidentJpaDao.deleteByIncidentNamesAndZoneId(existZoneIncidents, unmask(zoneId));
			if (rows > 0) {
				logger.info("Zone {} Incidents {} successfully deleted", zoneId, existZoneIncidents);
			}
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Zone Incidents successfully saved");
	}

	@Override
	@Secured(AuthorityUtils.ZONE_INCIDENT_FETCH)
	public List<ZoneIncidentResponse> findAllIncidentsByZoneId(String cameraImageId, Long zoneId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		if (!cameraImageJpaDao.existsByIdAndActive(cameraImageId, true)) {
			throw new NotFoundException(String.format("Camera image (%s) not found", cameraImageId));
		}
		validateZoneScheduling(cameraImageId, zoneId);
		List<String> existIncidentNames = cameraIncidentJpaDao.findIncidentNamesByCameraId(cameraImageId);
		if (existIncidentNames.isEmpty()) {
			throw new ValidationException(String.format("This camera (%s) not define any incident", cameraImageId));
		}
		List<String> existZoneIncidents = zoneIncidentJpaDao.findIncidentNamesByZoneId(unmask(zoneId));
		int i = 0, j = 0;
		ZoneIncidentResponse zoneIncident = null;
		List<ZoneIncidentResponse> zoneIncidents = new ArrayList<>();

		while (i < existIncidentNames.size() && j < existZoneIncidents.size()) {
			if (existIncidentNames.get(i).equals(existZoneIncidents.get(j))) {
				zoneIncident = new ZoneIncidentResponse(existIncidentNames.get(i), true);
				zoneIncidents.add(zoneIncident);
				i++;
				j++;
			} else {
				zoneIncident = new ZoneIncidentResponse(existIncidentNames.get(i), false);
				zoneIncidents.add(zoneIncident);
				i++;
			}
		}

		while (i < existIncidentNames.size()) {
			zoneIncident = new ZoneIncidentResponse(existIncidentNames.get(i), false);
			zoneIncidents.add(zoneIncident);
			i++;
		}

		return zoneIncidents;

	}

	@Override
	@Secured(AuthorityUtils.IMAGE_COORDINATE_DELETE)
	public SuccessResponse delete(Long id, String cameraImageId) {
		Long unmaskId = unmask(id);
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraImageId, getUser().gettOrganizationId(),
				true)) {
			throw new NotFoundException(
					String.format("This camera not found for this organization(%s)", getUser().gettOrganizationId()));
		}
		CameraImageCoordinateResponse imageCoordinate = cameraImageCoordinateJpaDao
				.findByIdAndActiveAndCameraImageId(unmaskId, true, cameraImageId);
		int rows;
		if (imageCoordinate == null)
			throw new NotFoundException(
					String.format("Camera Image Coordinate (%s) not exist in camera image (%s)", id, cameraImageId));
		if (imageCoordinate.getImageCoordinateType().equals(ImageCoordinateType.INCIDENT_DETECTION)) {
			throw new ValidationException(String.format("You can't delete main detector coordinate (%s)", id));
		} else {
			if (imageCoordinate.getImageCoordinateType().equals(ImageCoordinateType.ZONE)) {
				if (laneJpaDao.existsByCameraImageCoordinateIdAndActive(unmaskId, true))
					throw new ValidationException(String.format("Lane is mapped with this image coordinate (%s)", id));
			}
			rows = cameraImageCoordinateJpaDao.delete(unmaskId, getUserId(), new Date());
		}
		if (rows > 0) {
			logger.info("Camera Image coordinate {} successfully deleted", unmaskId);
		}
		return new SuccessResponse(HttpStatus.OK.value(),
				String.format("Image coordinate (%s) successfully deleted", id));
	}
}
