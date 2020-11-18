package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.ArmJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentAttributeValueJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LaneJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.EquipmentMapping;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentMappingService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.arm.ArmResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentAttributeValueResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentGroupByResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentLocationMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

@Service("equipmentMappingServiceImpl")
@Transactional
public class EquipmentMappingServiceImpl extends BaseService implements EquipmentMappingService {

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(EquipmentMappingServiceImpl.class);

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	@Autowired
	private EquipmentTypeAttributeJpaDao equipmentTypeAttributeJpaDao;

	@Autowired
	private EquipmentAttributeValueJpaDao equipmentAttributeValueJpaDao;

	@Autowired
	private LocationJpaDao locationJpaDao;

	@Autowired
	private ArmJpaDao armJpaDao;

	@Autowired
	private LaneJpaDao laneJpaDao;

	/**
	 * this method used to validate arm and lane id for that particular location
	 * id
	 * 
	 * @param armId
	 *            - request arm id
	 * @param laneId
	 *            - request lane id
	 * @param armIds
	 *            - armIds available for that location
	 * @param armLanesMapping
	 *            - arm and lane mapping available for that location
	 */
	private void validateArmAndLane(String armId, String laneId, Set<String> armIds,
			Map<String, Set<String>> armLanesMapping) {
		if (armId != null && !armIds.contains(armId)) {
			throw new ValidationException(String.format("This arm(%s) not exist in this location", armId));
		}
		if (laneId != null) {
			if (armId == null) {
				throw new ValidationException(
						String.format("arm id can't be null when lane (%s) is being mapped with equipment", laneId));
			} else {
				if (armLanesMapping.get(armId) != null && !armLanesMapping.get(armId).contains(laneId)) {
					throw new ValidationException(
							String.format("This lane(%s) not exist in arm(%s) for this location", laneId, armId));
				}
			}
		}
	}

	/**
	 * this method used to validate mapping list and fetch deleted equipment
	 * mapping's equipment id
	 * 
	 * @param locationId
	 * @param request
	 * @param locationMappedEquipmentGroupEquipmentIds
	 * @param availableGroupEquipmentIds
	 * @return Mapping equipment ids which will be deleted
	 */
	private Map<String, Set<String>> fetchDeletedEquipmentIds(String locationId, List<EquipmentMappingRequest> request,
			Map<String, Set<String>> locationMappedEquipmentGroupEquipmentIds,
			Map<String, Set<String>> availableGroupEquipmentIds) {
		Set<String> groupLocationArmLaneIdSet = new HashSet<>();
		Map<String, Set<String>> deletedGroupEquipmentIds = new HashMap<>();
		Set<String> equipmentIds;
		String groupLocationArmLaneId = null;
		List<String> equipmentIdList;
		List<String> locationMappedGroups = new LinkedList<>(locationMappedEquipmentGroupEquipmentIds.keySet());
		for (EquipmentMappingRequest equipmentMapping : request) {
			groupLocationArmLaneId = String.format("%s-%s-%s-%s", equipmentMapping.getEquipmentGroupId(), locationId,
					equipmentMapping.getArmId(), equipmentMapping.getLaneId());
			if (groupLocationArmLaneIdSet.contains(groupLocationArmLaneId)) {
				throw new ValidationException(String.format("Duplicate mapping (%s)", groupLocationArmLaneId));
			}
			locationMappedGroups.remove(groupLocationArmLaneId);
			groupLocationArmLaneIdSet.add(groupLocationArmLaneId);
			if (locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId) == null) {
				locationMappedEquipmentGroupEquipmentIds.put(groupLocationArmLaneId, new TreeSet<>());
			}
			if (equipmentMapping.getEquipmentIds() != null) {
				equipmentIds = new HashSet<>(locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId));
				equipmentIds.removeAll(equipmentMapping.getEquipmentIds());
				if (deletedGroupEquipmentIds.get(groupLocationArmLaneId) != null) {
					equipmentIds.addAll(deletedGroupEquipmentIds.get(groupLocationArmLaneId));
				}
				deletedGroupEquipmentIds.put(groupLocationArmLaneId, equipmentIds);
				if (availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()) == null) {
					availableGroupEquipmentIds.put(equipmentMapping.getEquipmentGroupId(), equipmentIds);
				} else {
					availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()).addAll(equipmentIds);
				}
			} else {
				int mappedSize = locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId).size();
				if (mappedSize > equipmentMapping.getCount().intValue()) {
					equipmentIdList = new ArrayList<>(
							locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId))
									.subList(equipmentMapping.getCount().intValue(), mappedSize);
					deletedGroupEquipmentIds.computeIfAbsent(groupLocationArmLaneId, value -> new TreeSet<>())
							.addAll(equipmentIdList);
					if (availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()) == null) {
						availableGroupEquipmentIds.put(equipmentMapping.getEquipmentGroupId(),
								new TreeSet<>(equipmentIdList));
					} else {
						availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()).addAll(equipmentIdList);
					}
				} else if (mappedSize < equipmentMapping.getCount()) {
				}
			}
		}
		locationMappedGroups.forEach(locationMapped -> {
			deletedGroupEquipmentIds.computeIfAbsent(locationMapped, value -> new TreeSet<>())
					.addAll(locationMappedEquipmentGroupEquipmentIds.get(locationMapped));
		});
		return deletedGroupEquipmentIds;
	}

	/**
	 * this method used to validate mapping request and fetch added new
	 * equipment for equipment mapping
	 * 
	 * @param locationId
	 * @param request
	 * @param locationMappedEquipmentGroupEquipmentIds
	 * @param availableGroupEquipmentIds
	 * @return Mapped new equipment for equipment mapping
	 */
	private Map<String, Set<String>> fetchNewEquipmentIds(String locationId, List<EquipmentMappingRequest> request,
			Map<String, Set<String>> locationMappedEquipmentGroupEquipmentIds,
			Map<String, Set<String>> availableGroupEquipmentIds) {
		String groupLocationArmLaneId = null;
		List<String> equipmentIdList;
		Map<String, Set<String>> newGroupEquipmentIds = new HashMap<>();
		for (EquipmentMappingRequest equipmentMapping : request) {
			groupLocationArmLaneId = String.format("%s-%s-%s-%s", equipmentMapping.getEquipmentGroupId(), locationId,
					equipmentMapping.getArmId(), equipmentMapping.getLaneId());
			if (locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId) == null) {
				locationMappedEquipmentGroupEquipmentIds.put(groupLocationArmLaneId, new TreeSet<>());
			}
			if (equipmentMapping.getEquipmentIds() != null) {
				equipmentMapping.getEquipmentIds()
						.removeAll(locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId));
				if (!availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId())
						.containsAll(equipmentMapping.getEquipmentIds())) {
					equipmentMapping.getEquipmentIds()
							.removeAll(availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()));
					throw new ValidationException(String.format("These equipments (%s) are not availabe to map",
							String.join(",", equipmentMapping.getEquipmentIds())));
				} else {
					if (newGroupEquipmentIds.get(groupLocationArmLaneId) != null) {
						equipmentMapping.getEquipmentIds().addAll(newGroupEquipmentIds.get(groupLocationArmLaneId));
					}
					newGroupEquipmentIds.put(groupLocationArmLaneId, new TreeSet<>(equipmentMapping.getEquipmentIds()));
				}
			} else {
				int mappedSize = locationMappedEquipmentGroupEquipmentIds.get(groupLocationArmLaneId).size();
				if (mappedSize > equipmentMapping.getCount().intValue()) {
					// continue;
				} else if (mappedSize < equipmentMapping.getCount()) {
					if (availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId()) == null
							|| availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId())
									.size() < (equipmentMapping.getCount() - mappedSize)) {
						throw new ValidationException(String
								.format("Equipments are not availabe to map for mapping (%s)", groupLocationArmLaneId));
					} else {
						equipmentIdList = new ArrayList<>(
								availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId())).subList(0,
										(equipmentMapping.getCount().intValue() - mappedSize));
						availableGroupEquipmentIds.get(equipmentMapping.getEquipmentGroupId())
								.removeAll(equipmentIdList);
						newGroupEquipmentIds.computeIfAbsent(groupLocationArmLaneId, value -> new TreeSet<>())
								.addAll(equipmentIdList);
					}
				}
			}
		}
		return newGroupEquipmentIds;
	}

	/**
	 * this method used to set attribute values. if values not available for a
	 * attribute then we are setting value null
	 *
	 * @param attributes
	 *            - attribute available for equipment type
	 * @param attributeValuesList
	 *            - attribute values for group id
	 * @param groupByGroupId
	 *            - get values in group by group id
	 * @param mappedEquipmentIds
	 * @param isMappedEquipment
	 * @return list of {@link EquipmentAttributeValueResponse}
	 * 
	 */
	private List<EquipmentAttributeValueResponse> setAttributeValues(List<EquipmentTypeAttributeResponse> attributes,
			List<Map<String, Object>> attributeValuesList, Boolean groupByGroupId,
			Map<String, String> mappedEquipments) {
		Map<String, String> attributeValues = new HashMap<>();
		Map<String, List<EquipmentAttributeValueResponse>> specificAttributeValues = new HashMap<>();
		attributeValuesList.forEach(attributeValue -> {
			String attributeId = attributeValue.get("attributeId") == null ? null
					: attributeValue.get("attributeId").toString();
			attributeValues.putIfAbsent(attributeId,
					attributeValue.get("value") == null ? null : attributeValue.get("value").toString());
			if (attributeValue.get("equipmentId") != null) {
				specificAttributeValues.computeIfAbsent(attributeId, (values) -> {
					return new ArrayList<EquipmentAttributeValueResponse>();
				}).add(new EquipmentAttributeValueResponse(
						attributeValue.get("equipmentId") == null ? null : attributeValue.get("equipmentId").toString(),
						attributeValue.get("equipmentName") == null ? null
								: attributeValue.get("equipmentName").toString(),
						attributeValue.get("value") == null ? null : attributeValue.get("value").toString(),
						mappedEquipments.get(attributeValue.get("equipmentId"))));
			}

		});
		List<EquipmentAttributeValueResponse> attributeValuesResponse = new ArrayList<>();
		attributes.forEach(attribute -> {
			if (!attribute.getIsSpecific() || !groupByGroupId) {
				attributeValuesResponse
						.add(new EquipmentAttributeValueResponse(attribute, attributeValues.get(attribute.getId())));
			} else {
				attributeValuesResponse.add(
						new EquipmentAttributeValueResponse(attribute, specificAttributeValues.get(attribute.getId())));
			}
		});
		return attributeValuesResponse;
	}

	/**
	 * This method used to fetch equipment details in tree format.
	 * 
	 * @param equipments
	 * @param equipmentGroupCount
	 * @param equipmentTypeAttributes
	 * @param equipmentGroupAttributeValues
	 * @param mappedEquipmentIds
	 * @param isMappedEquipment
	 * @return list of {@link EquipmentGroupByResponse}
	 */
	private List<EquipmentGroupByResponse> fetchEquipmentDetails(List<Map<String, Object>> equipments,
			Map<String, Long> equipmentGroupCount,
			Map<String, List<EquipmentTypeAttributeResponse>> equipmentTypeAttributes,
			Map<String, List<Map<String, Object>>> equipmentGroupAttributeValues,
			Map<String, String> mappedEquipments) {
		EquipmentGroupByResponse equipmentGroup, equipmentResponse = null;
		String equipmentTypeId = null, currEquipmentTypeId;
		List<Map<String, Object>> attributeValues;
		List<EquipmentGroupByResponse> equipmentResponseList = new ArrayList<>();
		Long mappedCount;
		List<EquipmentTypeAttributeResponse> attributes = null;
		equipments.sort(new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				if (o1.get("equipmentTypeId") != null && o2.get("equipmentTypeId") != null) {
					return o1.get("equipmentTypeId").toString().compareTo(o2.get("equipmentTypeId").toString());
				} else {
					return 0;
				}
			}
		});
		for (Map<String, Object> equipment : equipments) {
			equipmentGroup = new EquipmentGroupByResponse(equipment.get("groupId").toString(),
					equipment.get("equipmentTypeId").toString(), equipment.get("equipmentTypeCode").toString(),
					equipment.get("equipmentTypeName").toString(), Long.parseLong(equipment.get("count").toString()));
			equipmentGroup.setAvailableCount(Long.parseLong(equipment.get("count").toString()));
			if ((mappedCount = equipmentGroupCount.get(equipment.get("groupId").toString())) != null) {
				equipmentGroup.setCount(equipmentGroup.getAvailableCount() + mappedCount);
			}
			currEquipmentTypeId = equipmentGroup.getEquipmentTypeId();
			if (equipmentTypeId == null || !equipmentTypeId.equals(equipmentGroup.getEquipmentTypeId())) {
				equipmentResponse = new EquipmentGroupByResponse(null, equipmentGroup.getEquipmentTypeId(),
						equipmentGroup.getEquipmentTypeCode(), equipmentGroup.getEquipmentTypeName(),
						equipmentGroup.getCount());
				equipmentResponse.setAvailableCount(equipmentGroup.getAvailableCount());
				equipmentResponse.setList(new ArrayList<>());
				equipmentResponse.getList().add(equipmentGroup);
				equipmentResponseList.add(equipmentResponse);
				attributes = equipmentTypeAttributes.get(equipmentResponse.getEquipmentTypeId());
			} else {
				equipmentResponse.getList().add(equipmentGroup);
				equipmentResponse.setCount(equipmentResponse.getCount() + equipmentGroup.getCount());
				equipmentResponse
						.setAvailableCount(equipmentResponse.getAvailableCount() + equipmentGroup.getAvailableCount());
			}
			attributeValues = equipmentGroupAttributeValues.get(equipmentGroup.getGroupId());
			if (attributes != null)
				equipmentGroup
						.setAttributeValues(setAttributeValues(attributes, attributeValues, true, mappedEquipments));
			equipmentTypeId = currEquipmentTypeId;
		}
		return equipmentResponseList;
	}

	/**
	 * this method used to fetch equipment details for equipment mapping
	 * 
	 * @param organizationId
	 * @param equipmentGroupCount
	 * @param equipmentTypeAttributes
	 * @param equipmentGroupAttributeValues
	 * @param locationMappedEquipments
	 * @param mappedEquipmentIds
	 * @return list of {@link EquipmentGroupByResponse}
	 */
	private List<EquipmentGroupByResponse> fetchEquipmentDetailsAndSetAvailableEquipmentCountForEquipmentMapping(
			Long organizationId, String locationId, Map<String, Long> equipmentGroupCount,
			Map<String, List<EquipmentTypeAttributeResponse>> equipmentTypeAttributes,
			Map<String, List<Map<String, Object>>> equipmentGroupAttributeValues,
			List<Map<String, Object>> locationMappedEquipments, Map<String, String> mappedEquipments,
			Map<String, Long> equipmentAvailableCount) {
		List<Map<String, Object>> equipments = equipmentJpaDao
				.findByOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullAndGroupByGroupId(
						organizationId, true);
		Set<String> equipmentGroupIds = new HashSet<>();
		equipments.forEach(equipment -> {
			equipmentGroupIds.add(equipment.get("groupId").toString());
		});
		Map<String, Object> locationMappedEquipmentCopy;
		for (Map<String, Object> locationMappedEquipment : locationMappedEquipments) {
			if (!equipmentGroupIds.contains(locationMappedEquipment.get("groupId").toString())) {
				locationMappedEquipmentCopy = new HashMap<>(locationMappedEquipment);
				locationMappedEquipmentCopy.put("count", 0);
				equipments.add(locationMappedEquipmentCopy);
			}
		}
		equipments.sort(new Comparator<Map<String, Object>>() {
			@Override
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				return o1.get("equipmentTypeId").toString().compareTo(o2.get("equipmentTypeId").toString());
			}
		});
		equipments.forEach(equipment -> {
			equipmentAvailableCount.putIfAbsent(equipment.get("groupId").toString(),
					Long.parseLong(equipment.get("count").toString()));
			equipmentGroupAttributeValues.putIfAbsent(equipment.get("groupId").toString(),
					equipmentAttributeValueJpaDao
							.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActiveAndLocationId(
									equipment.get("groupId").toString(), true, locationId));
			equipmentTypeAttributes.putIfAbsent(equipment.get("equipmentTypeId").toString(),
					equipmentTypeAttributeJpaDao
							.findByEquipmentTypeIdAndActive(equipment.get("equipmentTypeId").toString(), true));
		});
		List<EquipmentGroupByResponse> equipmentResponseList = this.fetchEquipmentDetails(equipments,
				equipmentGroupCount, equipmentTypeAttributes, equipmentGroupAttributeValues, mappedEquipments);
		return equipmentResponseList;
	}

	private void setEquipmentGroupsAndMappedEquipmentIdsAndEquipmentMappedCountForLocation(
			List<Map<String, Object>> equipments, List<Map<String, Object>> equipmentGroups,
			Map<String, Long> availabeEquipmentCount, Map<String, Long> equipmentMappedCount) {
		Map<String, Object> equipmentGroup = null;
		String equipmentGroupId = null;
		for (Map<String, Object> equipment : equipments) {
			if (equipmentGroupId == null || !equipmentGroupId.equalsIgnoreCase(equipment.get("groupId").toString())) {
				equipmentGroup = new HashMap<>(equipment);
				equipmentGroup.put("count", availabeEquipmentCount.get(equipment.get("groupId").toString()));
				equipmentMappedCount.put(equipment.get("groupId").toString(),
						(1l - availabeEquipmentCount.get(equipment.get("groupId").toString())));
				equipmentGroups.add(equipmentGroup);
			} else {
				equipmentMappedCount.put(equipment.get("groupId").toString(),
						equipmentMappedCount.get(equipment.get("groupId").toString()) + 1);
			}
			equipmentGroupId = equipment.get("groupId").toString();
		}
	}

	/**
	 * this method used to fetch location details for equipment mapping
	 * 
	 * @param locationId
	 * @param mappingIdAndEquipments
	 * @param equipmentTypeAttributes
	 * @param equipmentGroupAttributeValues
	 * @return {@link LocationResponse}
	 */
	private LocationResponse fetchLocationDetail(String locationId,
			Map<String, List<Map<String, Object>>> mappingIdAndEquipments,
			Map<String, List<EquipmentTypeAttributeResponse>> equipmentTypeAttributes,
			Map<String, List<Map<String, Object>>> equipmentGroupAttributeValues,
			Map<String, Long> equipmentAvailableCount, Map<String, String> mappedEquipments) {
		LocationResponse response = locationJpaDao.findResponseById(locationId);
		List<ArmResponse> arms = armJpaDao.findByLocationIdAndActive(locationId, true);
		List<LaneResponse> lanes = laneJpaDao.findByLocationIdAndActive(locationId, true);
		List<Map<String, Object>> equipments;
		Map<String, List<LaneResponse>> armLaneLookup = new HashMap<>();
		List<Map<String, Object>> equipmentGroups;
		Map<String, Long> equipmentMappedCount;
		for (LaneResponse lane : lanes) {
			if ((equipments = mappingIdAndEquipments.get(lane.getId())) != null) {
				equipmentGroups = new ArrayList<>();
				equipmentMappedCount = new HashMap<>();
				this.setEquipmentGroupsAndMappedEquipmentIdsAndEquipmentMappedCountForLocation(equipments,
						equipmentGroups, equipmentAvailableCount, equipmentMappedCount);
				lane.setEquipments(this.fetchEquipmentDetails(equipmentGroups, equipmentMappedCount,
						equipmentTypeAttributes, equipmentGroupAttributeValues, mappedEquipments));
			}
			armLaneLookup.computeIfAbsent(lane.getArmId(), (laneList) -> new ArrayList<>()).add(lane);
		}

		for (ArmResponse arm : arms) {
			if ((equipments = mappingIdAndEquipments.get(arm.getId())) != null) {
				equipmentGroups = new ArrayList<>();
				equipmentMappedCount = new HashMap<>();
				this.setEquipmentGroupsAndMappedEquipmentIdsAndEquipmentMappedCountForLocation(equipments,
						equipmentGroups, equipmentAvailableCount, equipmentMappedCount);
				arm.setEquipments(this.fetchEquipmentDetails(equipmentGroups, equipmentMappedCount,
						equipmentTypeAttributes, equipmentGroupAttributeValues, mappedEquipments));
			}
			arm.setLanes(armLaneLookup.get(arm.getId()));
		}
		if (arms.isEmpty()) {
			response.setArms(null);
		}

		if ((equipments = mappingIdAndEquipments.get(locationId)) != null) {
			equipmentGroups = new ArrayList<>();
			equipmentMappedCount = new HashMap<>();
			this.setEquipmentGroupsAndMappedEquipmentIdsAndEquipmentMappedCountForLocation(equipments, equipmentGroups,
					equipmentAvailableCount, equipmentMappedCount);
			response.setEquipments(this.fetchEquipmentDetails(equipmentGroups, equipmentMappedCount,
					equipmentTypeAttributes, equipmentGroupAttributeValues, mappedEquipments));
		}

		response.setArms(arms);
		return response;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_MAPPING_CREATE)
	public SuccessResponse saveEquipmentMappingForLocation(String locationId, List<EquipmentMappingRequest> request) {
		Long organizationId = getUser().gettOrganizationId();
		if (!locationJpaDao.existsByIdAndOrganizationIdAndActive(locationId, organizationId, true)) {
			throw new NotFoundException(String.format("This location (%s) not found", locationId));
		}
		Set<String> armIds = armJpaDao.findIdsByLocationIdAndActive(locationId, true);
		List<Map<String, String>> armLanes = laneJpaDao.findIdAndArmIdsByLocationIdAndActive(locationId, true);
		Map<String, Set<String>> armLanesMapping = new HashMap<>();
		armLanes.forEach(armLane -> {
			armLanesMapping.computeIfAbsent(armLane.get("armId"), (value) -> new HashSet<>()).add(armLane.get("id"));
		});
		request.forEach(equipmentMapping -> {
			validateArmAndLane(equipmentMapping.getArmId(), equipmentMapping.getLaneId(), armIds, armLanesMapping);
		});
		List<EquipmentMappingResponse> locationMappedEquipments = equipmentMappingJpaDao.findByActiveAndLocationId(true,
				locationId);
		Map<String, Set<String>> locationMappedEquipmentGroupEquipmentIds = new HashMap<>();
		Map<String, Set<String>> availableGroupEquipmentIds = new HashMap<>();
		locationMappedEquipments.forEach(locationMappedEquipment -> {
			locationMappedEquipmentGroupEquipmentIds
					.computeIfAbsent(String.format("%s-%s-%s-%s", locationMappedEquipment.getGroupId(),
							locationMappedEquipment.getLocationId(), locationMappedEquipment.getArmId(),
							locationMappedEquipment.getLaneId()), value -> new TreeSet<>())
					.add(locationMappedEquipment.getEquipmentId());
		});
		request.forEach(equipmentMapping -> {
			if (equipmentMapping.getEquipmentGroupId() != null) {
				availableGroupEquipmentIds.putIfAbsent(equipmentMapping.getEquipmentGroupId(),
						new TreeSet<>(equipmentJpaDao
								.findIdsByGroupIdAndOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNull(
										equipmentMapping.getEquipmentGroupId(), organizationId, true)));
			}
			if (equipmentMapping.getEquipmentIds() != null
					&& equipmentMapping.getEquipmentIds().size() != equipmentMapping.getCount().intValue()) {
				throw new ValidationException(
						"If equipment ids are available then it should be equal to equipment count");
			}
		});
		request.sort(new Comparator<EquipmentMappingRequest>() {
			@Override
			public int compare(EquipmentMappingRequest o1, EquipmentMappingRequest o2) {
				if (o1.getEquipmentGroupId().equalsIgnoreCase(o2.getEquipmentGroupId())) {
					if (o1.getEquipmentIds() != null) {
						return -1;
					} else if (o2.getEquipmentIds() != null) {
						return 1;
					} else {
						return 0;
					}
				}
				return o1.getEquipmentGroupId().compareTo(o2.getEquipmentGroupId());
			}
		});
		Map<String, Set<String>> deletedGroupEquipmentIds = fetchDeletedEquipmentIds(locationId, request,
				locationMappedEquipmentGroupEquipmentIds, availableGroupEquipmentIds);
		deletedGroupEquipmentIds.forEach((key, value) -> {
			equipmentMappingJpaDao.deleteByEquipmentIds(value);
		});

		Map<String, Set<String>> newGroupEquipmentIds = fetchNewEquipmentIds(locationId, request,
				locationMappedEquipmentGroupEquipmentIds, availableGroupEquipmentIds);
		List<EquipmentMapping> equipmentMappings = new ArrayList<>();
		if (!newGroupEquipmentIds.isEmpty()) {
			Integer sequence = sequenceGenerator("JEM");
			String[] groupLocationArmLane;
			for (String groupLocationArmLaneKey : newGroupEquipmentIds.keySet()) {
				groupLocationArmLane = groupLocationArmLaneKey.split("-");
				for (String equipmentId : newGroupEquipmentIds.get(groupLocationArmLaneKey)) {
					equipmentMappings.add(new EquipmentMapping(String.format("JEM%07d", sequence++), locationId,
							groupLocationArmLane[2].equalsIgnoreCase("null") ? null : groupLocationArmLane[2],
							groupLocationArmLane[3].equalsIgnoreCase("null") ? null : groupLocationArmLane[3],
							equipmentId));
				}
			}
			updateSequenceGenerator("JEM", sequence);
			if (!equipmentMappings.isEmpty()) {
				logger.info("Equipment mapping save for location {}", locationId);
				equipmentMappingJpaDao.saveAll(equipmentMappings);
			}
		}
		return new SuccessResponse(HttpStatus.OK.value(), "Equipment successfully mapped with location");
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public EquipmentLocationMappingResponse findEquipmentMappingByLocationId(String locationId) {
		Long organizationId = getUser().gettOrganizationId();
		if (!locationJpaDao.existsByIdAndActive(locationId, true)) {
			throw new NotFoundException(String.format("Location (%s) not found", locationId));
		}
		List<Map<String, Object>> locationMappedEquipments = equipmentJpaDao
				.findByActiveAndLocationIdAndGroupByGroupId(true, locationId);
		List<Map<String, Object>> equipmentMappings = equipmentMappingJpaDao
				.findEquipmentAndEquipmentTypeAndMappingByActiveAndLocationId(true, locationId);
		Map<String, List<Map<String, Object>>> mappingIdAndEquipments = new HashMap<>();
		Map<String, Long> equipmentGroupCount = new HashMap<>();
		Map<String, String> mappedEquipments = new HashMap<>();
		Set<String> mappedEquipmentIds = new HashSet<>();
		equipmentMappings.forEach(equipmentMapping -> {
			if (equipmentMapping.get("laneId") == null && equipmentMapping.get("armId") == null) {
				mappingIdAndEquipments.computeIfAbsent(locationId, value -> new ArrayList<>()).add(equipmentMapping);
				mappedEquipments.put(equipmentMapping.get("equipmentId").toString(), locationId);
			} else if (equipmentMapping.get("laneId") == null && equipmentMapping.get("armId") != null) {
				mappingIdAndEquipments
						.computeIfAbsent(equipmentMapping.get("armId").toString(), value -> new ArrayList<>())
						.add(equipmentMapping);
				mappedEquipments.put(equipmentMapping.get("equipmentId").toString(),
						equipmentMapping.get("armId").toString());
			} else {
				mappingIdAndEquipments
						.computeIfAbsent(equipmentMapping.get("laneId").toString(), value -> new ArrayList<>())
						.add(equipmentMapping);
				mappedEquipments.put(equipmentMapping.get("equipmentId").toString(),
						equipmentMapping.get("laneId").toString());
			}
			if (equipmentGroupCount.get(equipmentMapping.get("groupId")) == null) {
				equipmentGroupCount.put(equipmentMapping.get("groupId").toString(), 1l);
			} else {
				equipmentGroupCount.put(equipmentMapping.get("groupId").toString(),
						(equipmentGroupCount.get(equipmentMapping.get("groupId").toString()) + 1l));
			}
			mappedEquipmentIds.add(equipmentMapping.get("equipmentId").toString());
		});
		Map<String, Long> equipmentAvailableCount = new HashMap<>();
		Map<String, List<EquipmentTypeAttributeResponse>> equipmentTypeAttributes = new HashMap<>();
		Map<String, List<Map<String, Object>>> equipmentGroupAttributeValues = new HashMap<>();
		EquipmentLocationMappingResponse response = new EquipmentLocationMappingResponse();
		response.setEquipments(this.fetchEquipmentDetailsAndSetAvailableEquipmentCountForEquipmentMapping(
				organizationId, locationId, equipmentGroupCount, equipmentTypeAttributes, equipmentGroupAttributeValues,
				locationMappedEquipments, mappedEquipments, equipmentAvailableCount));
		response.setLocation(this.fetchLocationDetail(locationId, mappingIdAndEquipments, equipmentTypeAttributes,
				equipmentGroupAttributeValues, equipmentAvailableCount, mappedEquipments));
		return response;
	}

}
