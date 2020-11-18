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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraIncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.CameraJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.IncidentAttributeIncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraIncident;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraIncidentService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.util.DataTypeUtil;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentAttributeValueRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentAttributeValueResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentAttributeResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

@Service("cameraIncidentServiceImpl")
@Transactional
public class CameraIncidentServiceImpl extends BaseService implements CameraIncidentService {

	@Value("${efkon.junction.code}")
	private String junctionCode;

	@Value("${efkon.highway.code}")
	private String highwayCode;

	@Autowired
	private CameraJpaDao cameraJpaDao;

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private CameraIncidentJpaDao cameraIncidentJpaDao;

	@Autowired
	private IncidentAttributeIncidentJpaDao incidentAttributeIncidentJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	@Autowired
	private LocationJpaDao locationJpaDao;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(EquipmentServiceImpl.class);

	/**
	 * this method used to validate incidents for a particular camera
	 *
	 * @param request
	 * @param incidentAttributesLookUp
	 * @param attributeLookUp
	 * @return
	 * @throws ValidationException if incident type is empty or not exist , incident
	 *                             name not exist in incident type, two incidents
	 *                             are same, incident attribute id not exist in any
	 *                             incident, value not according to data type
	 */
	private void validate(String cameraId, CameraIncidentRequest request,
			Map<String, List<IncidentAttributeResponse>> incidentAttributesLookUp) {
		Set<String> requestIncidentNames = new HashSet<>();
		if (request.getIncidentType() == null) {
			throw new ValidationException("Incident type can't be empty or null");
		} else {
			if (!IncidentType.matches(request.getIncidentType())) {
				throw new ValidationException(
						String.format("This incident type (%s) not exist", request.getIncidentType()));
			}
		}
		List<IncidentAttributeResponse> incidentAttributes;
		for (IncidentRequest incident : request.getIncidents()) {
			incidentAttributes = incidentAttributesLookUp.get(incident.getName());
			if (incidentAttributes == null) {
				throw new ValidationException(
						String.format("This incident (%s) not exist for camera (%s)", incident.getName(), cameraId));
			}
			if (requestIncidentNames.contains(incident.getName())) {
				throw new ValidationException("Two incident with same name can't be save");
			}
			int size = incident.getAttributeValues().size();
			if (incidentAttributes.size() != size) {
				throw new ValidationException(
						String.format("Incident Attributes values details not filled properly for incident (%s)",
								incident.getName()));
			}
			incident.getAttributeValues().sort(new Comparator<CameraIncidentAttributeValueRequest>() {
				@Override
				public int compare(CameraIncidentAttributeValueRequest o1, CameraIncidentAttributeValueRequest o2) {
					return o1.getAttributeId().compareTo(o2.getAttributeId());
				}
			});
			for (int i = 0; i < size; i++) {
				if (!incident.getAttributeValues().get(i).getAttributeId()
						.equals(unmask(incidentAttributes.get(i).getId()))) {
					throw new ValidationException(String.format("This attribute (%s) not exist in incident (%s)",
							mask(incident.getAttributeValues().get(i).getAttributeId()), incident.getName()));
				}
				if (!DataTypeUtil.validValue(incidentAttributes.get(i).getDataType(),
						incident.getAttributeValues().get(i).getValue())) {
					throw new ValidationException(String.format("Attribute (%s) value (%s) not valid",
							mask(incident.getAttributeValues().get(i).getAttributeId()),
							incident.getAttributeValues().get(i).getValue()));
				}

			}

			requestIncidentNames.add(incident.getName());
		}

	}

	@Override
	@Secured((AuthorityUtils.CAMERA_INCIDENT_CREATE))
	public SuccessResponse saveCameraIncidents(String locationId, String cameraId, CameraIncidentRequest request) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("This camera (%s) not found in organization (%s)", cameraId,
					getUser().gettOrganizationId()));
		}
		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraId, true);
		if (equipmentMapping == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any location", cameraId));
		}
		if (!equipmentMapping.getLocationId().equals(locationId)) {
			throw new ValidationException(
					String.format("This camera (%s) not mapped with location (%s)", cameraId, locationId));
		}
		Integer locationRelated = 0, locationTypeRelated = 0;
		LocationResponse locationResponse = locationJpaDao.findResponseById(equipmentMapping.getLocationId());
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any arm or lane"));
		} else {
			locationTypeRelated = locationResponse.getLocationTypeCode().equalsIgnoreCase(junctionCode) ? 1 : 2;
			locationRelated = equipmentMapping.getLaneId() == null ? 1 : 2;
		}
		Boolean isIncidentDetect = cameraJpaDao.findIsIncidentDetectById(cameraId);
		if (isIncidentDetect == null) {
			throw new ValidationException(String.format("This camera (%s) is not for incident detection", cameraId));
		} else if (!isIncidentDetect) {
			throw new ValidationException(String.format("Incident detect not found for camera (%s)", cameraId));
		}
		Map<String, List<IncidentAttributeResponse>> incidentAttributesLookUp = new HashMap<>();
		List<CameraIncident> cameraIncidents = new ArrayList<>();
		if (request.getCameraId() != null) {
			if (!equipmentMappingJpaDao.existsByLocationIdAndEquipmentIdAndActive(locationId, request.getCameraId(),
					true)) {
				throw new NotFoundException(String.format("This camera (%s)  not found for location (%s)",
						request.getCameraId(), locationId));
			}
			List<CameraIncidentAttributeValueResponse> existCameraIncidents = cameraIncidentJpaDao
					.findByCameraId(request.getCameraId());
			if (existCameraIncidents.isEmpty()) {
				throw new ValidationException(
						String.format("Incidents doesn't define for camera (%s)", request.getCameraId()));
			}
			int rows = cameraIncidentJpaDao.deleteByCameraId(cameraId, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Camera {} incidents deleted successfully", cameraId);
			}
			existCameraIncidents.forEach(cameraIncident -> {
				cameraIncidents.add(new CameraIncident(cameraId, cameraIncident.getIncidentName(),
						unmask(cameraIncident.getIncidentAttributeId()), cameraIncident.getValue()));
			});
		} else {
			List<Map<String, Object>> existIncidentAttributes = incidentAttributeIncidentJpaDao
					.findByIncidentTypeAndLocationRelatedAndLocationTypeRelated(
							IncidentType.valueOf(request.getIncidentType()), locationRelated, locationTypeRelated);
			for (Map<String, Object> incidentAttribute : existIncidentAttributes) {
				Long attributeId = incidentAttribute.get("attributeId") == null ? null
						: Long.parseLong(incidentAttribute.get("attributeId").toString());
				if (attributeId == null) {
					incidentAttributesLookUp.putIfAbsent(incidentAttribute.get("incidentName").toString(),
							new ArrayList<>());
				} else {
					incidentAttributesLookUp
							.computeIfAbsent(incidentAttribute.get("incidentName").toString(),
									(attribute) -> new ArrayList<>())
							.add(new IncidentAttributeResponse(attributeId,
									incidentAttribute.get("attributeName").toString(),
									DataType.valueOf(incidentAttribute.get("dataType").toString()),
									incidentAttribute.get("unit") == null ? null
											: incidentAttribute.get("unit").toString()));
				}
			}
			validate(cameraId, request, incidentAttributesLookUp);
			List<String> cameraIncidentNames = cameraIncidentJpaDao.findIncidentNamesByIncidentTypeAndCameraId(
					IncidentType.valueOf(request.getIncidentType()), cameraId);
			for (IncidentRequest incident : request.getIncidents()) {
				if (cameraIncidentNames.contains(incident.getName())) {
					incident.getAttributeValues().forEach(attributeValue -> {
						int row = cameraIncidentJpaDao.updateValue(incident.getName(), cameraId,
								attributeValue.getAttributeId(), attributeValue.getValue());
						if (row > 0) {
							logger.info("Incident {} attribute {} value {} successfully updated", incident.getName(),
									attributeValue.getAttributeId(), attributeValue.getValue());
						}
					});
					cameraIncidentNames.remove(incident.getName());
				} else {
					if (incident.getAttributeValues().isEmpty()) {
						cameraIncidents.add(new CameraIncident(cameraId, incident.getName(), null, null));
					} else {
						incident.getAttributeValues().forEach(attributeValue -> {
							cameraIncidents.add(new CameraIncident(cameraId, incident.getName(),
									attributeValue.getAttributeId(), attributeValue.getValue()));

						});
					}
				}
			}
			if (!cameraIncidentNames.isEmpty()) {
				int rows = cameraIncidentJpaDao.deleteByIncidentNamesAndCameraId(cameraIncidentNames, cameraId,
						getUserId(), new Date());
				if (rows > 0) {
					logger.info("Incidents {} successfully deleted", cameraIncidentNames);
				}
			}
		}
		cameraIncidentJpaDao.saveAll(cameraIncidents);
		logger.info("Camera {} incidents successfully saved", cameraId);
		return new SuccessResponse(HttpStatus.OK.value(),
				String.format("Camera (%s) incidents successfully saved", cameraId));
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_INCIDENT_FETCH)
	public List<CameraIncidentResponse> findByCameraId(String locationId, String cameraId, String incidentType) {
		if (!IncidentType.matches(incidentType)) {
			throw new NotFoundException(String.format("This incident type (%s) not found", incidentType));
		}
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("This camera (%s) not found in organization (%s)", cameraId,
					getUser().gettOrganizationId()));
		}

		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraId, true);
		Integer locationRelated = 0, locationTypeRelated = 0;
		if (equipmentMapping == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any location", cameraId));
		}
		if (!equipmentMapping.getLocationId().equals(locationId)) {
			throw new ValidationException(
					String.format("This camera (%s) not mapped with location (%s)", cameraId, locationId));
		}
		LocationResponse locationResponse = locationJpaDao.findResponseById(equipmentMapping.getLocationId());
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format("This camera (%s) not mapped with any arm or lane"));
		} else {
			locationTypeRelated = locationResponse.getLocationTypeCode().equalsIgnoreCase(junctionCode) ? 1 : 2;
			locationRelated = equipmentMapping.getLaneId() == null ? 1 : 2;
		}
		List<Map<String, Object>> incidents = incidentAttributeIncidentJpaDao
				.findByIncidentTypeAndLocationRelatedAndLocationTypeRelated(IncidentType.valueOf(incidentType),
						locationRelated, locationTypeRelated);
		List<CameraIncidentAttributeValueResponse> existCameraIncidents = cameraIncidentJpaDao
				.findByCameraIdAndIncidentTypeOrderByIncidentNameAscIncidentAttributeIdAsc(cameraId,
						IncidentType.valueOf(incidentType));
		int i = 0, j = 0;
		CameraIncidentResponse cameraIncident = null;
		CameraIncidentAttributeValueResponse attributeValue = null;
		List<CameraIncidentResponse> cameraIncidents = new ArrayList<>();
		String incidentName = null, currIncidentName;
		Map<String, Object> incident = null;
		while (i < incidents.size() && j < existCameraIncidents.size()) {
			incident = incidents.get(i);
			CameraIncidentAttributeValueResponse cameraIncidentAttributeValue = existCameraIncidents.get(j);
			currIncidentName = incident.get("incidentName").toString();
			Long attributeId = incident.get("attributeId") == null ? null
					: Long.parseLong(incident.get("attributeId").toString());

			if (incidentName == null || !incidentName.equals(currIncidentName)) {

				cameraIncident = attributeId == null ? new CameraIncidentResponse(currIncidentName, null, false)
						: new CameraIncidentResponse(currIncidentName, new ArrayList<>(), false);
				cameraIncidents.add(cameraIncident);
			}
			if (cameraIncidentAttributeValue.getIncidentName().equals(incident.get("incidentName").toString())) {
				if (attributeId == null) {
					cameraIncident.setChecked(true);

				} else {
					if (unmask(cameraIncidentAttributeValue.getIncidentAttributeId()).equals(attributeId)) {
						cameraIncident.setChecked(true);
						cameraIncidentAttributeValue.setAttribute(
								new IncidentAttributeResponse(Long.parseLong(incident.get("attributeId").toString()),
										incident.get("attributeName").toString(),
										DataType.valueOf(incident.get("dataType").toString()),
										incident.get("unit") == null ? null : incident.get("unit").toString()));
						cameraIncident.getAttributeValues().add(cameraIncidentAttributeValue);

					}
				}
				i++;
				j++;
			} else {
				if (attributeId != null) {
					attributeValue = new CameraIncidentAttributeValueResponse(currIncidentName,
							Long.parseLong(incident.get("attributeId").toString()), null);
					attributeValue.setAttribute(
							new IncidentAttributeResponse(Long.parseLong(incident.get("attributeId").toString()),
									incident.get("attributeName").toString(),
									DataType.valueOf(incident.get("dataType").toString()),
									incident.get("unit") == null ? null : incident.get("unit").toString()));
					cameraIncident.getAttributeValues().add(attributeValue);

				}
				i++;
			}
			incidentName = currIncidentName;

		}
		while (i < incidents.size()) {
			incident = incidents.get(i);
			currIncidentName = incident.get("incidentName").toString();

			if (incidentName == null || !incidentName.equals(currIncidentName)) {
				cameraIncident = new CameraIncidentResponse(currIncidentName, new ArrayList<>(), false);
				cameraIncidents.add(cameraIncident);
			}
			if (incident.get("attributeId") != null) {
				attributeValue = new CameraIncidentAttributeValueResponse(currIncidentName,
						Long.parseLong(incident.get("attributeId").toString()), null);
				attributeValue.setAttribute(new IncidentAttributeResponse(
						Long.parseLong(incident.get("attributeId").toString()),
						incident.get("attributeName").toString(), DataType.valueOf(incident.get("dataType").toString()),
						incident.get("unit") == null ? null : incident.get("unit").toString()));
				cameraIncident.getAttributeValues().add(attributeValue);
			}
			incidentName = currIncidentName;
			i++;
		}
		return cameraIncidents;

	}

	@Override
	@Secured(AuthorityUtils.CAMERA_FETCH)
	public List<CameraResponse> findCameraNamesByCameraId(String locationId, String cameraId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("This camera (%s) not found in organization (%s)", cameraId,
					getUser().gettOrganizationId()));
		}
		EquipmentMappingResponse equipmentMapping = equipmentMappingJpaDao.findByEquipmentIdAndActive(cameraId, true);
		if (equipmentMapping == null) {
			throw new ValidationException(String.format("Camera (%s) not mapped with any location", cameraId));
		}
		if (!equipmentMapping.getLocationId().equals(locationId)) {
			throw new ValidationException(
					String.format("Camera (%s) not mapped with location (%s)", cameraId, locationId));
		}
		List<Map<String, Object>> cameras = null;
		if (equipmentMapping.getArmId() == null) {
			throw new ValidationException(String.format("Camera (%s) not mapped with any arm or lane in location (%s)",
					cameraId, equipmentMapping.getLocationId()));
		} else {
			if (equipmentMapping.getLaneId() != null) {

				cameras = cameraIncidentJpaDao.findCameraCodeByArmIdAndLaneIdAndActive(cameraId,
						equipmentMapping.getArmId(), equipmentMapping.getLaneId(), true);
			} else {
				cameras = cameraIncidentJpaDao.findCameraCodeByArmIdAndActiveAndLaneIdIsNULL(cameraId,
						equipmentMapping.getArmId(), true);
			}
		}
		List<CameraResponse> response = new ArrayList<>();
		cameras.forEach(camera -> {
			response.add(new CameraResponse(camera.get("id").toString(), null, null, null, null, null,
					camera.get("code") == null ? null : camera.get("code").toString(), null, null, null, null, null,
					null, null));
		});

		return response;
	}

	@Override
	@Secured(AuthorityUtils.CAMERA_INCIDENT_FETCH)
	public List<String> findIncidentNamesByCameraId(String locationId, String cameraId) {
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(cameraId, getUser().gettOrganizationId(), true)) {
			throw new NotFoundException(String.format("This camera (%s) not found in organization (%s)", cameraId,
					getUser().gettOrganizationId()));
		}
		if (!equipmentMappingJpaDao.existsByLocationIdAndEquipmentIdAndActive(locationId, cameraId, true)) {
			throw new NotFoundException(
					String.format("This camera (%s) not mapped with location (%s)", cameraId, locationId));
		}
		if (!cameraJpaDao.existsById(cameraId)) {
			throw new NotFoundException(String.format(" Camera (%s) properties not filled", cameraId));
		}
		return cameraIncidentJpaDao.findIncidentNamesByCameraId(cameraId);
	}
}
