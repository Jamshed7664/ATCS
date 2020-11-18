package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.ArmJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.DirectionJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LaneDirectionJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LaneJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationAttributeValueJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.LaneDirectionKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationAttributeValue;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.util.DataTypeUtil;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.direction.DirectionResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationAttributeValueRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationAttributeValueResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeResponse;

@Service("locationServiceImpl")
@Transactional
public class LocationServiceImpl extends BaseService implements LocationService {

	private static Logger logger = LoggerFactory.getLogger(LocationServiceImpl.class);

	@Value("${efkon.junction.code}")
	private String junctionCode;

	@Value("${efkon.highway.code}")
	private String highwayCode;

	@Autowired
	private LocationJpaDao locationJpaDao;

	@Autowired
	private DirectionJpaDao directionJpaDao;

	@Autowired
	private LocationTypeJpaDao locationTypeJpaDao;

	@Autowired
	private ArmJpaDao armJpaDao;

	@Autowired
	private LaneJpaDao laneJpaDao;

	@Autowired
	private LocationTypeAttributeJpaDao locationTypeAttributeJpaDao;

	@Autowired
	private LocationAttributeValueJpaDao locationAttributeValueJpaDao;

	@Autowired
	private LaneDirectionJpaDao laneDirectionJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	/**
	 * this method used to validate direction ids .
	 *
	 * @param requestDirectionIds
	 * @throws ValidationException
	 *             if some of the direction id not valid
	 */

	public void validateDirectionIds(Set<Long> requestDirectionIds, List<Long> directionIds) {
		if (requestDirectionIds == null || requestDirectionIds.isEmpty()) {
			throw new ValidationException("Direction ids are required if lanes are selected");
		}
		requestDirectionIds.removeAll(directionIds);
		if (!requestDirectionIds.isEmpty()) {
			throw new ValidationException(String.format("Some of the directions (%s) are not valid",
					requestDirectionIds.stream().map(id -> mask(id)).collect(Collectors.toSet())));
		}
	}

	/**
	 * this method used to validate name
	 *
	 * @param name
	 * @param organizationId
	 */
	private void validateName(String name, Long organizationId) {
		if (name == null) {
			return;
		}
		if (locationJpaDao.existsByNameAndOrganizationIdAndActive(name, organizationId, true)) {
			throw new ValidationException(String.format("Name '%s' is already exist ", name));
		}
	}

	/**
	 * this method used to validate latitude and longitude
	 *
	 * @param name
	 * @param organizationId
	 */
	private void validateLatitudeAndLongitude(String latitude, String longitude, Long organizationId) {
		if (latitude == null || longitude == null) {
			return;
		}
		if (locationJpaDao.existsByLatitudeAndLongitudeAndOrganizationIdAndActive(latitude, longitude, organizationId,
				true)) {
			throw new ValidationException(
					String.format("Latitude and longitude '%s,%s' is already exist ", latitude, longitude));
		}
		if(!Pattern.matches("([0-9]{1,2})[:|°]([0-9]{1,2})[:|'|′]?([0-9]{1,2}(?:\\.[0-9]+){0,1})?[\"|″]([N|S])", latitude)) {
			throw new ValidationException("Latitude format is not valid. Please check latitude again.");
		}
		if(!Pattern.matches("([0-9]{1,3})[:|°]([0-9]{1,2})[:|'|′]?([0-9]{1,2}(?:\\.[0-9]+){0,1})?[\"|″]([E|W])", longitude)) {
			throw new ValidationException("Longitude format is not valid. Please check longitude again.");
		}
	}

	/**
	 * this method used to validate junction highway request by comparing arms
	 * request with arm count.
	 *
	 * @param locationTypeResponse
	 * @param request
	 */
	private void validateArmAndLaneRequest(LocationTypeResponse locationTypeResponse, LocationRequest request,
			String locationId) {
		if ((locationTypeResponse.getCode().equalsIgnoreCase(highwayCode)
				|| locationTypeResponse.getCode().equalsIgnoreCase(junctionCode))
				&& (request.getArms() == null || request.getArms().isEmpty())) {
			throw new ValidationException("Arms can't be null or empty if location type is junctio or highway");
		}
		if (locationTypeResponse.getCode().equals(highwayCode) && request.getArms().size() != 2) {
			throw new ValidationException(
					String.format("Arms size should be 2 for location type '%s' ", locationTypeResponse.getName()));
		}
		if (locationTypeResponse.getCode().equals(junctionCode)
				&& (request.getArms().size() < 3 || request.getArms().size() > 8)) {
			throw new ValidationException(String.format(
					"Arms size should be greater than or equal to 3 and less than or equal 8 for location type '%s' ",
					locationTypeResponse.getName()));
		}
		if (!locationTypeResponse.getCode().equalsIgnoreCase(highwayCode)
				&& !locationTypeResponse.getCode().equalsIgnoreCase(junctionCode) && request.getArms() != null) {
			throw new ValidationException(String.format("Arm count can't be assigned for this location type (%s)",
					locationTypeResponse.getName()));
		}

		if (request.getArms() != null)
			validateLanesRequest(request.getArms(), locationId);
	}

	/**
	 * this method used to validate request and location attributes with all the
	 * attributes are assigned with valid values according to their data type
	 *
	 * @param request
	 * @param organizationId
	 */
	private void validateRequest(LocationRequest request,
			Map<String, LocationTypeAttributeResponse> locationTypeAttributeLookup, Long organizationId) {
		if (request.getAttributeValues() == null) {
			return;
		}
		LocationTypeAttributeResponse locationTypeAttribute;
		String name = null, latitude = null, longitude = null;
		for (LocationAttributeValueRequest attribute : request.getAttributeValues()) {
			locationTypeAttribute = locationTypeAttributeLookup.get(attribute.getLocationTypeAttributeId());
			if (locationTypeAttribute == null) {
				throw new ValidationException(String.format("Location type attribute (%s) not found",
						attribute.getLocationTypeAttributeId()));
			}
			if (!DataTypeUtil.validValue(locationTypeAttribute.getDataType(), attribute.getValue())) {
				throw new ValidationException(String.format("Attribute (%s) value (%s) not valid",
						locationTypeAttribute.getDataType(), attribute.getValue()));
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("latitude")) {
				latitude = attribute.getValue();
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("longitude")) {
				latitude = attribute.getValue();
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("name")) {
				name = attribute.getValue();
			}
		}
		validateName(name, organizationId);
		validateLatitudeAndLongitude(latitude, longitude, organizationId);
	}

	/**
	 * this method used to validate lanes request by comparing size of lane
	 * request with laneCount
	 *
	 * @param armRequestSet
	 */

	private void validateLanesRequest(Set<ArmRequest> armRequestSet, String locationId) {
		List<Long> directionIds = directionJpaDao.findAllIds();
		armRequestSet.forEach(arm -> {
			if (arm.getId() != null && !armJpaDao.existsByIdAndLocationIdAndActive(arm.getId(), locationId, true))
				throw new ValidationException(String.format("Arm (%s) not found", arm.getId()));
			if (arm.getLanes() != null)
				arm.getLanes().forEach(lane -> {
					if (lane.getId() != null && !laneJpaDao.existsByIdAndArmIdAndLocationIdAndActive(lane.getId(),
							arm.getId(), locationId, true))
						throw new ValidationException(String.format("Lane (%s) not found", lane.getId()));
					validateDirectionIds(lane.getDirectionIds(), directionIds);
				});
		});
	}

	/**
	 * this method used to get the location response by location id and set all
	 * the necessary response such as arm,lane,direction etc
	 *
	 * @Param locationId
	 */
	private LocationResponse getLocationResponse(String locationId, Long organizationId) {
		LocationResponse response = locationJpaDao.findResponseById(locationId);
		if (response == null) {
			throw new NotFoundException(String.format("Location(%s) not found", locationId));
		}
		if (!response.getOrganizationId().equals(organizationId)) {
			throw new AccessDeniedException("You aren't login with correct user to fetch this details");
		}
		List<ArmResponse> arms = armJpaDao.findByLocationIdAndActive(locationId, true);
		List<LaneResponse> lanes = laneJpaDao.findByLocationIdAndActive(locationId, true);
		List<Map<String, Object>> laneDirections = laneDirectionJpaDao.findDirectionByLocationId(locationId);
		Map<String, List<DirectionResponse>> laneDirectionsLookup = new HashMap<>();
		laneDirections.forEach(laneDirection -> {
			laneDirectionsLookup
					.computeIfAbsent(laneDirection.get("laneId").toString(), (directions) -> new ArrayList<>())
					.add(new DirectionResponse(Long.parseLong(laneDirection.get("id").toString()),
							laneDirection.get("name").toString()));
		});
		Map<String, List<LaneResponse>> armLaneLookup = new HashMap<>();
		lanes.forEach(lane -> {
			lane.setDirections(laneDirectionsLookup.get(lane.getId()));
			armLaneLookup.computeIfAbsent(lane.getArmId(), (laneList) -> new ArrayList<>()).add(lane);
		});
		arms.forEach(arm -> {
			arm.setLanes(armLaneLookup.get(arm.getId()));
		});
		if (arms.isEmpty()) {
			response.setArms(null);
		}
		response.setArms(arms);
		List<LocationTypeAttributeResponse> attributes = locationTypeAttributeJpaDao
				.findByLocationTypeIdAndActiveOrderByFixedDesc(response.getLocationTypeId(), true);
		if (!attributes.isEmpty()) {
			List<LocationAttributeValueResponse> attributeValuesResponse = locationAttributeValueJpaDao
					.findByLocationId(locationId);
			Map<String, String> attributeValueLookup = new HashMap<>();
			attributeValuesResponse.forEach(attributeValue -> {
				attributeValueLookup.putIfAbsent(attributeValue.getLocationTypeAttributeId(),
						attributeValue.getValue());
			});
			response.setAttributeValues(new ArrayList<>());
			attributes.forEach(attribute -> {
				response.getAttributeValues().add(
						LocationAttributeValueResponse.get(attribute, attributeValueLookup.get(attribute.getId())));
			});
		}
		return response;
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_CREATE)
	public LocationResponse save(LocationRequest request) {
		Long organizationId = getUser().gettOrganizationId();
		LocationTypeResponse locationTypeResponse = locationTypeJpaDao.findResponseById(request.getLocationTypeId());
		if (locationTypeResponse == null) {
			throw new ValidationException(String.format("Location type (%s)  not found", request.getLocationTypeId()));
		}
		Long attributeSize = locationTypeAttributeJpaDao.countByLocationTypeIdAndActive(request.getLocationTypeId(),
				true);
		if ((attributeSize != 0 && request.getAttributeValues() == null)
				|| (request.getAttributeValues() != null && request.getAttributeValues().size() != attributeSize)) {
			throw new ValidationException(
					String.format("Location type attribute value details are not filled properly"));
		}
		List<LocationTypeAttributeResponse> locationTypeAttributes = locationTypeAttributeJpaDao
				.findByLocationTypeIdAndOrganizationIdAndActive(request.getLocationTypeId(),organizationId, true);
		Map<String, LocationTypeAttributeResponse> locationTypeAttributeLookup = new HashMap<>();
		locationTypeAttributes.forEach(locationTypeAttribute -> {
			locationTypeAttributeLookup.putIfAbsent(locationTypeAttribute.getId(), locationTypeAttribute);
		});
		validateRequest(request, locationTypeAttributeLookup, organizationId);
		validateArmAndLaneRequest(locationTypeResponse, request, null);
		Location location = request.toEntity(null);
		LocationTypeAttributeResponse locationTypeAttribute;
		for (LocationAttributeValueRequest attribute : request.getAttributeValues()) {
			locationTypeAttribute = locationTypeAttributeLookup.get(attribute.getLocationTypeAttributeId());
			if (locationTypeAttribute.getName().equalsIgnoreCase("latitude")) {
				location.setLatitude(attribute.getValue());
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("longitude")) {
				location.setLongitude(attribute.getValue());
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("name")) {
				location.setName(attribute.getValue());
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("address")) {
				location.setAddress(attribute.getValue());
			}
		}
		String code = request.getCode(location.getName());
		Integer sequence = sequenceGenerator("LOCA");
		location.setId(String.format("LOCA%s%04d", code, sequence));
		location.setCode(code);
		List<LaneDirectionKey> laneDirections = new ArrayList<>();
		if (request.getArms() != null) {
			location.setArms(new ArrayList<>());
			Arm arm;
			Integer armSequence = 0, laneSequence = 0;
			for (ArmRequest armRequest : request.getArms()) {
				armSequence++;
				arm = new Arm(String.format("ARM%d%s%04d", armSequence, code, sequence),
						locationTypeResponse.getCode().equalsIgnoreCase(highwayCode)
								? (armSequence.equals(1) ? "LHS" : "RHS") : String.format("ARM%d", armSequence),
						armRequest.getDirection(), location, new ArrayList<>());
				if (armRequest.getLanes() != null) {
					laneSequence = 0;
					for (LaneRequest lane : armRequest.getLanes()) {
						laneSequence++;
						arm.getLanes()
								.add(new Lane(String.format("L%dA%d%s%04d", laneSequence, armSequence, code, sequence),
										String.format("LANE%d", laneSequence), arm, location));
						if (lane.getDirectionIds() != null)
							for (Long directionId : lane.getDirectionIds()) {
								laneDirections.add(new LaneDirectionKey(
										String.format("L%dA%d%s%04d", laneSequence, armSequence, code, sequence),
										directionId));
							}
					}
				}
				location.getArms().add(arm);
			}
		}
		location.settOrganizationId(organizationId);
		if (request.getAttributeValues() != null) {
			location.setLocationAttributeValues(new ArrayList<>());
			Integer locationAttributeSequence = sequenceGenerator("LOATDT");
			for (LocationAttributeValueRequest attributeValue : request.getAttributeValues()) {
				location.getLocationAttributeValues()
						.add(new LocationAttributeValue(String.format("ATDT%07d", locationAttributeSequence++),
								location, attributeValue.getValue(), attributeValue.getLocationTypeAttributeId()));
			}
			updateSequenceGenerator("LOATDT", locationAttributeSequence);
		}
		location = locationJpaDao.save(location);

		for (LaneDirectionKey laneDirection : laneDirections) {
			laneDirectionJpaDao.save(laneDirection.getLaneId(), laneDirection.getDirectionId());
		}
		LocationResponse response = getLocationResponse(location.getId(), organizationId);
		return response;
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_FETCH)
	public LocationResponse findById(String id) {
		Long organizationId = getUser().gettOrganizationId();
		return getLocationResponse(id, organizationId);
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_FETCH)
	public List<LocationResponse> findAll() {
		Long organizationId = getUser().gettOrganizationId();
		List<LocationResponse> locations = locationJpaDao.findByOrganizationIdAndActive(organizationId, true);
		return locations;
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_UPDATE)
	public LocationResponse update(String id, LocationRequest request) {
		Long organizationId = getUser().gettOrganizationId();
		LocationResponse location = locationJpaDao.findResponseById(id);
		if (location == null) {
			throw new NotFoundException(String.format("Location (%s) not found", id));
		}
		List<LocationTypeAttributeResponse> locationTypeAttributes = locationTypeAttributeJpaDao
				.findByLocationTypeIdAndOrganizationIdAndActive(location.getLocationTypeId(),organizationId, true);
		Map<String, LocationTypeAttributeResponse> locationTypeAttributeLookup = new HashMap<>();
		locationTypeAttributes.forEach(locationTypeAttribute -> {
			locationTypeAttributeLookup.putIfAbsent(locationTypeAttribute.getId(), locationTypeAttribute);
		});
		LocationTypeAttributeResponse locationTypeAttribute;
		String name = null, latitude = null, longitude = null, address = null;
		List<LocationAttributeValueRequest> removeAttributeList = new ArrayList<>();
		for (LocationAttributeValueRequest attribute : request.getAttributeValues()) {
			locationTypeAttribute = locationTypeAttributeLookup.get(attribute.getLocationTypeAttributeId());
			if (locationTypeAttribute.getName().equalsIgnoreCase("latitude")) {
				if (attribute.getValue().equals(location.getLatitude())) {
					removeAttributeList.add(attribute);
				} else {
					latitude = attribute.getValue();
				}
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("longitude")) {
				if (attribute.getValue().equals(location.getLongitude())) {
					removeAttributeList.add(attribute);
				} else {
					longitude = attribute.getValue();
				}
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("name")) {
				if (attribute.getValue().equals(location.getName())) {
					removeAttributeList.add(attribute);
				} else {
					name = attribute.getValue();
				}
			}
			if (locationTypeAttribute.getName().equalsIgnoreCase("address")) {
				address = attribute.getValue();
			}
		}
		request.getAttributeValues().removeAll(removeAttributeList);
		validateRequest(request, locationTypeAttributeLookup, organizationId);
		if (request.getArms() != null) {
			validateLanesRequest(request.getArms(), id);
			request.getArms().forEach(arm -> {
				if (arm.getId() == null) {
					throw new ValidationException("Arm id can't be null or empty");
				}
			});
		}
		int rows = locationJpaDao.update(id, name == null ? location.getName() : name,
				request.getCode() == null ? location.getCode() : request.getCode(), address,
				longitude == null ? location.getLongitude() : longitude,
				latitude == null ? location.getLatitude() : latitude, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Location {} updated successfully");
		}
		List<String> laneIds, laneEquipmentIds;
		Integer activeLaneCount, totalLaneCount;
		if (request.getArms() != null) {
			for (ArmRequest armRequest : request.getArms()) {
				if (armRequest.getId() != null) {
					armJpaDao.update(armRequest.getId(), armRequest.getDirection(), getUserId(), new Date());
				}
				laneIds = laneJpaDao.findIdsByArmIdAndActive(armRequest.getId(), true);
				totalLaneCount = (int) laneJpaDao.countByArmId(armRequest.getId());

				activeLaneCount = laneIds == null ? 0 : laneIds.size();
				List<Long> laneDirectionIds = null;
				Set<Long> requestDirectionIds;
				for (LaneRequest laneRequest : armRequest.getLanes()) {
					laneDirectionIds = null;
					requestDirectionIds = new HashSet<>(laneRequest.getDirectionIds());
					String laneId = null;
					if (laneRequest.getId() == null) {
						laneId = String.format("L%d%s", ++totalLaneCount, armRequest.getId().replace("ARM", "A"));
						laneJpaDao.save(
								new Lane(laneId, String.format("LANE%d", ++activeLaneCount), armRequest.getId(), id));
						logger.info("Lane {} successfully added", laneId);
					} else {
						laneId = laneRequest.getId();
						laneDirectionIds = laneDirectionJpaDao.findAllDirectionIdsByLaneId(laneId);
						requestDirectionIds.removeAll(laneDirectionIds);
						laneDirectionIds.removeAll(laneRequest.getDirectionIds());
						laneIds.remove(laneId);
					}
					for (Long directionId : requestDirectionIds) {
						laneDirectionJpaDao.save(laneId, directionId);
					}
					if (laneDirectionIds != null && !laneDirectionIds.isEmpty()) {
						laneDirectionJpaDao.deleteByLaneIdAndDirectionIds(laneId, laneDirectionIds);
					}
				}
				if (laneIds != null && !laneIds.isEmpty()) {
					laneEquipmentIds = equipmentMappingJpaDao.findIdByLaneIdsAndActive(laneIds, true);
					if (!laneEquipmentIds.isEmpty()) {
						throw new ValidationException(
								"lane can't be delete as some of the equipment are mapped with lane");
					}
					laneIds.forEach(laneId -> {
						laneJpaDao.delete(laneId, getUserId(), new Date());
					});
				}
			}
		}
		if (request.getAttributeValues() != null && !request.getAttributeValues().isEmpty()) {
			Integer locationAttributeSequence = null;
			for (LocationAttributeValueRequest attributeValueRequest : request.getAttributeValues()) {
				rows = locationAttributeValueJpaDao.update(attributeValueRequest.getValue(),
						attributeValueRequest.getLocationTypeAttributeId(), id, getUserId(), new Date());
				if (rows == 0) {
					if (locationAttributeSequence == null) {
						locationAttributeSequence = sequenceGenerator("LOATDT");
					}
					locationAttributeValueJpaDao.save(new LocationAttributeValue(
							String.format("ATDT%07d", locationAttributeSequence++),
							attributeValueRequest.getLocationTypeAttributeId(), id, attributeValueRequest.getValue()));
				}
			}
			if (locationAttributeSequence != null) {
				updateSequenceGenerator("LOATDT", locationAttributeSequence);
			}
		}
		LocationResponse response = getLocationResponse(location.getId(), organizationId);
		return response;
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_DELETE)
	public SuccessResponse delete(String id) {
		Boolean exist = locationJpaDao.existsByIdAndActive(id, true);
		if (!exist) {
			throw new NotFoundException(String.format("Location (%s) not found", id));
		}
		if (equipmentMappingJpaDao.existsByLocationIdAndActive(id, true)) {
			throw new ValidationException(String.format(
					"This location(%s) can't be deleted as some of the equipment mapped with this location(%s)", id,
					id));
		}
		List<String> armIds = armJpaDao.findAllIdsByLocationId(id);
		int rows = 0;
		rows = armJpaDao.deleteByLocationId(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Arms deleted successfully for location {}", id);
		}
		rows = 0;
		for (String armId : armIds)
			rows += laneJpaDao.deleteByArmId(armId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Lanes deleted successfully for arms {}", armIds);
		}
		rows = locationJpaDao.delete(id, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Location {} deleted successfully", id);
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Location deleted successfully");

	}
}
