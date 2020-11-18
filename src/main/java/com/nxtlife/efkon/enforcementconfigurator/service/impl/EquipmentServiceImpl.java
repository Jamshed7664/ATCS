package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Tuple;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentAttributeValueJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.Equipment;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.EquipmentAttributeValue;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.util.DataTypeUtil;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentAttributeValueRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentAttributeValueResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeResponse;

@Service("equipmentServiceImpl")
@Transactional
public class EquipmentServiceImpl extends BaseService implements EquipmentService {

	@Autowired
	private EquipmentTypeJpaDao equipmentTypeJpaDao;

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	@Autowired
	private EquipmentTypeAttributeJpaDao equipmentTypeAttributeJpaDao;

	@Autowired
	private EquipmentAttributeValueJpaDao equipmentAttributeValueJpaDao;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(EquipmentServiceImpl.class);

	/**
	 * this method used to get attributes lookup for type
	 *
	 * @param typeId
	 * @return key value pair of equipmentTypeAttribute id and
	 *         equipmentTypeAttribute
	 */
	private Map<String, EquipmentTypeAttributeResponse> getAttributes(String typeId) {
		List<EquipmentTypeAttributeResponse> equipmentTypeAttributes = equipmentTypeAttributeJpaDao
				.findByEquipmentTypeIdAndActive(typeId, true);
		Map<String, EquipmentTypeAttributeResponse> attributes = new HashMap<>();
		equipmentTypeAttributes.forEach(attribute -> {
			attributes.putIfAbsent(attribute.getId(), attribute);
		});
		return attributes;
	}

	/**
	 * this method used to validate value for specified data type
	 *
	 * @param dataType
	 * @param value
	 * @param options
	 */
	private void validateValues(DataType dataType, String value, Set<String> options) {
		if (!DataTypeUtil.validValue(dataType, value)) {
			throw new ValidationException(String.format("Attribute data type (%s) value (%s) not valid", dataType,
					value == null ? "Value not assigned" : value));
		}
		if ((dataType.equals(DataType.Option) || dataType.equals(DataType.Range)) && !options.contains(value)) {
			throw new ValidationException(
					String.format("Option and range value will be only these (%s)", String.join(",", options)));
		}
	}

	/**
	 * this method used to validate all the attributes are assigned with valid
	 * values according to their data type.
	 *
	 * @param attributesValue
	 * @param equipmentTypeAttributes
	 */
	private void validate(List<EquipmentAttributeValueRequest> attributesValue,
			Map<String, EquipmentTypeAttributeResponse> equipmentTypeAttributes, Integer count) {
		if (attributesValue.size() != equipmentTypeAttributes.size()) {
			throw new ValidationException("Attributes value are not specified for every equipment type attributes");
		}
		attributesValue.forEach(attributeValue -> {
			if (equipmentTypeAttributes.get(attributeValue.getAttributeId()) == null) {
				throw new ValidationException(
						String.format("This equipment attribute (%s) not found", attributeValue.getAttributeId()));
			}
			EquipmentTypeAttributeResponse equipmentTypeAttribute = equipmentTypeAttributes
					.get(attributeValue.getAttributeId());
			if (equipmentTypeAttribute.getIsSpecific() && (attributeValue.getValue() != null
					|| attributeValue.getAttributeValues() == null || attributeValue.getAttributeValues().isEmpty())) {
				throw new ValidationException(
						"If equipment type attribute value are specific to equipment then attribute values should be assigned otherwise value should be assigned");
			}
			if (equipmentTypeAttribute.getIsSpecific()) {
				if (attributeValue.getAttributeValues().size() != count) {
					throw new ValidationException(
							String.format("Attribute (%s) values are not assigned for every equipment",
									equipmentTypeAttribute.getName()));
				}
				attributeValue.getAttributeValues().forEach(specificValue -> {
					validateValues(equipmentTypeAttribute.getDataType(), specificValue.getValue(),
							equipmentTypeAttribute.getOptions());
				});
			} else {
				validateValues(equipmentTypeAttribute.getDataType(), attributeValue.getValue(),
						equipmentTypeAttribute.getOptions());
			}

		});

	}

	/**
	 * this method used to fetch equipment detail using equipment group id,
	 * equipmentTypeId and organizationId.
	 *
	 * @param equipmentGroupId
	 * @param equipmentTypeId
	 * @param organizationId
	 * @return equipment response
	 */
	private EquipmentResponse get(String equipmentGroupId, String equipmentTypeId, Long organizationId) {
		Map<String, Object> equipment = equipmentJpaDao
				.findByOrganizationIdAndGroupIdAndActiveAndEquipmentTypeIdAndGroupByGroupId(organizationId,
						equipmentGroupId, true, equipmentTypeId);
		EquipmentResponse equipmentResponse = new EquipmentResponse(null, null, equipment.get("groupId").toString(),
				equipmentTypeId, equipment.get("equipmentTypeCode").toString(),
				equipment.get("equipmentTypeName").toString());
		equipmentResponse.setCount(Long.parseLong(equipment.get("count").toString()));
		return equipmentResponse;
	}

	/**
	 * this method used to fetch equipment detail using equipment group id and
	 * organizationId.
	 *
	 * @param equipmentGroupId
	 * @param organizationId
	 * @return equipment response
	 */
	private EquipmentResponse get(String equipmentGroupId, Long organizationId) {
		Map<String, Object> equipment = equipmentJpaDao
				.findByOrganizationIdAndGroupIdAndActiveAndGroupByGroupId(organizationId, equipmentGroupId, true);
		EquipmentResponse equipmentResponse = new EquipmentResponse(null, null, equipment.get("groupId").toString(),
				equipment.get("equipmentTypeId").toString(), equipment.get("equipmentTypeCode").toString(),
				equipment.get("equipmentTypeName").toString());
		equipmentResponse.setCount(Long.parseLong(equipment.get("count").toString()));
		return equipmentResponse;
	}

	/**
	 * this method used to set attribute values. if values not available for a
	 * attribute then we are setting value null
	 *
	 * @param attributes
	 * @param attributeValuesList
	 * @return list of {@link EquipmentAttributeValueResponse}
	 */
	private List<EquipmentAttributeValueResponse> setAttributeValues(List<EquipmentTypeAttributeResponse> attributes,
			List<Map<String, Object>> attributeValuesList, Boolean groupByGroupId) {
		Map<String, String> attributeValues = new HashMap<>();
		Map<String, List<EquipmentAttributeValueResponse>> specificAttributeValues = new HashMap<>();
		attributeValuesList.forEach(attributeValue -> {
			String attributeId = attributeValue.get("attributeId") == null ? null
					: attributeValue.get("attributeId").toString();
			attributeValues.putIfAbsent(attributeId,
					attributeValue.get("value") == null ? null : attributeValue.get("value").toString());
			specificAttributeValues.computeIfAbsent(attributeId, (values) -> {
				return new ArrayList<EquipmentAttributeValueResponse>();
			}).add(new EquipmentAttributeValueResponse(
					attributeValue.get("equipmentId") == null ? null : attributeValue.get("equipmentId").toString(),
					attributeValue.get("equipmentName") == null ? null : attributeValue.get("equipmentName").toString(),
					attributeValue.get("value") == null ? null : attributeValue.get("value").toString()));
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

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_CREATE)
	public EquipmentResponse save(EquipmentRequest equipment) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentTypeJpaDao.existsById(equipment.getTypeId())) {
			throw new ValidationException("Equipment type is not valid");
		}
		Map<String, EquipmentTypeAttributeResponse> equipmentTypeAttributes = getAttributes(equipment.getTypeId());
		Integer equipmentCount = null;
		for (EquipmentAttributeValueRequest attribute : equipment.getAttributesValue()) {
			if (equipmentTypeAttributes.get(attribute.getAttributeId()) == null) {
				throw new ValidationException(
						String.format("This equipment attribute (%s) not found", attribute.getAttributeId()));
			}
			if (equipmentTypeAttributes.get(attribute.getAttributeId()).getName().equalsIgnoreCase("quantity")) {
				if (Integer.parseInt(attribute.getValue()) > 0) {
					equipmentCount = Integer.parseInt(attribute.getValue());
				} else {
					throw new ValidationException(
							String.format("Equipment count(%d) can't be less than 1", attribute.getValue()));
				}
			}
		}
		if (equipmentCount == null) {
			throw new ValidationException("Equipment quantity not found");
		}
		validate(equipment.getAttributesValue(), equipmentTypeAttributes, equipmentCount);
		Long count = equipmentJpaDao.countByEquipmentTypeIdAndOrganizationIdAndActive(equipment.getTypeId(),
				organizationId, true);
		Integer limit = equipmentTypeJpaDao.findQuantityById(equipment.getTypeId());
		if ((count.intValue() + equipmentCount) > limit) {
			throw new ValidationException("Equipment limit exceed");
		}
		Equipment sEquipment = null;
		Integer equipmentSequence = sequenceGenerator("EQMST"), equipmentGroupSequence = sequenceGenerator("EQMSTGRP"),
				equipmentAttributeSequence = sequenceGenerator("EQATDT"),
				equipmentNameSequence = sequenceGenerator(String.format("EQMST%s", equipment.getTypeId()));
		String equipmentTypeName = equipmentTypeJpaDao.findNameById(equipment.getTypeId());
		for (int i = 0; i < equipmentCount; i++) {
			sEquipment = equipmentJpaDao.save(new Equipment(String.format("EQMST%06d", equipmentSequence),
					String.format("%s %d", equipmentTypeName, equipmentNameSequence),
					String.format("EQMSTGRP%04d", equipmentGroupSequence), equipment.getTypeId(), organizationId));
			for (EquipmentAttributeValueRequest attributeValue : equipment.getAttributesValue()) {
				if (equipmentTypeAttributes.get(attributeValue.getAttributeId()).getIsSpecific()) {
					equipmentAttributeValueJpaDao.save(new EquipmentAttributeValue(
							String.format("ATDT%07d", equipmentAttributeSequence), attributeValue.getAttributeId(),
							sEquipment.getId(), attributeValue.getAttributeValues().get(i).getValue()));
				} else {
					equipmentAttributeValueJpaDao
							.save(new EquipmentAttributeValue(String.format("ATDT%07d", equipmentAttributeSequence),
									attributeValue.getAttributeId(), sEquipment.getId(), attributeValue.getValue()));
				}
				equipmentAttributeSequence += 1;
			}
			equipmentSequence += 1;
			equipmentNameSequence += 1;
		}
		updateSequenceGenerator("EQMST", equipmentSequence);
		updateSequenceGenerator(String.format("EQMST%s", equipment.getTypeId()), equipmentNameSequence);
		updateSequenceGenerator("EQATDT", equipmentAttributeSequence);
		EquipmentResponse equipmentResponse = get(String.format("EQMSTGRP%04d", equipmentGroupSequence),
				equipment.getTypeId(), organizationId);

		if (sEquipment != null) {
			List<EquipmentTypeAttributeResponse> attributes = new ArrayList<>(equipmentTypeAttributes.values());
			equipmentResponse.setAttributeValues(setAttributeValues(attributes,
					equipmentAttributeValueJpaDao
							.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
									String.format("EQMSTGRP%04d", equipmentGroupSequence), true),
					true));
		}
		return equipmentResponse;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public List<EquipmentResponse> findAll() {
		Long organizationId = getUser().gettOrganizationId();
		List<EquipmentResponse> equipments = equipmentJpaDao.findByOrganizationIdAndActive(organizationId, true);
		for (EquipmentResponse equipment : equipments) {
			List<EquipmentTypeAttributeResponse> attributes = equipmentTypeAttributeJpaDao
					.findByEquipmentTypeIdAndActive(equipment.getEquipmentTypeId(), true);
			equipment.setAttributeValues(setAttributeValues(attributes,
					equipmentAttributeValueJpaDao
							.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
									equipment.getGroupId(), true),
					false));
		}
		return equipments;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public List<EquipmentResponse> findAllGroupByGroupId() {
		Long organizationId = getUser().gettOrganizationId();
		EquipmentResponse equipmentResponse;
		List<Map<String, Object>> equipments = equipmentJpaDao
				.findByOrganizationIdAndActiveAndGroupByGroupId(organizationId, true);
		List<Map<String, Object>> equipmentGroups = equipmentMappingJpaDao.findEquipmentGroupIdAndCountByActive(true);
		Map<String, Long> equipmentGroupCount = new HashMap<>();
		equipmentGroups.forEach(equipmentGroup -> {
			equipmentGroupCount.putIfAbsent(equipmentGroup.get("groupId").toString(),
					Long.parseLong(equipmentGroup.get("count").toString()));
		});
		List<EquipmentResponse> equipmentResponseList = new ArrayList<>();
		List<EquipmentTypeAttributeResponse> attributes = null;
		String equipmentTypeId = null, currEquipmentTypeId;
		List<Map<String, Object>> attributeValues;
		Long mappedCount;
		for (Map<String, Object> equipment : equipments) {
			equipmentResponse = new EquipmentResponse(null, null, equipment.get("groupId").toString(),
					equipment.get("equipmentTypeId").toString(), equipment.get("equipmentTypeCode").toString(),
					equipment.get("equipmentTypeName").toString());
			equipmentResponse.setCount(Long.parseLong(equipment.get("count").toString()));
			equipmentResponse.setAvailableCount(equipmentResponse.getCount());
			currEquipmentTypeId = equipmentResponse.getEquipmentTypeId();
			if (equipmentTypeId == null || !equipmentTypeId.equals(currEquipmentTypeId)) {
				attributes = equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(currEquipmentTypeId, true);
			}
			equipmentTypeId = currEquipmentTypeId;
			attributeValues = equipmentAttributeValueJpaDao
					.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
							equipmentResponse.getGroupId(), true);
			if (attributes != null)
				equipmentResponse.setAttributeValues(setAttributeValues(attributes, attributeValues, true));
			if ((mappedCount = equipmentGroupCount.get(equipment.get("groupId").toString())) != null) {
				equipmentResponse.setAvailableCount(equipmentResponse.getCount() - mappedCount);
			}
			equipmentResponseList.add(equipmentResponse);
		}
		return equipmentResponseList;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public List<EquipmentResponse> findByEquipmentTypeIdAndGroupByGroupId(String equipmentTypeId) {
		Long organizationId = getUser().gettOrganizationId();
		EquipmentTypeResponse equipmentType = equipmentTypeJpaDao.findByOrganizationIdAndActiveAndId(organizationId,
				true, equipmentTypeId);
		if (equipmentType == null) {
			throw new NotFoundException(String.format("This equipment type (%s) not exists", equipmentTypeId));
		}
		EquipmentResponse equipmentResponse;
		List<Map<String, Object>> equipments = equipmentJpaDao
				.findByOrganizationIdAndActiveAndEquipmentTypeIdAndGroupByGroupId(organizationId, true,
						equipmentTypeId);
		List<Map<String, Object>> equipmentGroups = equipmentMappingJpaDao
				.findEquipmentGroupIdAndCountByActiveAndEquipmentTypeId(true, equipmentTypeId);
		Map<String, Long> equipmentGroupCount = new HashMap<>();
		equipmentGroups.forEach(equipmentGroup -> {
			equipmentGroupCount.putIfAbsent(equipmentGroup.get("groupId").toString(),
					Long.parseLong(equipmentGroup.get("count").toString()));
		});
		List<EquipmentResponse> equipmentResponseList = new ArrayList<>();
		List<EquipmentTypeAttributeResponse> attributes = equipmentTypeAttributeJpaDao
				.findByEquipmentTypeIdAndActive(equipmentTypeId, true);
		List<Map<String, Object>> attributeValues;
		Long mappedCount;
		for (Map<String, Object> equipment : equipments) {
			equipmentResponse = new EquipmentResponse(null, null, equipment.get("groupId").toString(), equipmentTypeId,
					equipmentType.getName(), equipmentType.getCode());
			equipmentResponse.setCount(Long.parseLong(equipment.get("count").toString()));
			equipmentResponse.setAvailableCount(equipmentResponse.getCount());
			attributeValues = equipmentAttributeValueJpaDao
					.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
							equipmentResponse.getGroupId(), true);
			if (attributes != null)
				equipmentResponse.setAttributeValues(setAttributeValues(attributes, attributeValues, true));
			if ((mappedCount = equipmentGroupCount.get(equipment.get("groupId").toString())) != null) {
				equipmentResponse.setAvailableCount(equipmentResponse.getCount() - mappedCount);
			}
			equipmentResponseList.add(equipmentResponse);
		}
		return equipmentResponseList;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public EquipmentResponse findByGroupIdAndGroupByGroupId(String groupId) {
		Long organizationId = getUser().gettOrganizationId();
		EquipmentResponse equipmentResponse;
		Map<String, Object> equipment = equipmentJpaDao
				.findByOrganizationIdAndActiveAndGroupIdAndGroupByGroupId(organizationId, true, groupId);
		if (equipment == null) {
			throw new NotFoundException("Group id not found");
		}
		List<EquipmentTypeAttributeResponse> attributes = null;
		List<Map<String, Object>> attributeValues;
		equipmentResponse = new EquipmentResponse(null, null, equipment.get("groupId").toString(),
				equipment.get("equipmentTypeId").toString(), equipment.get("equipmentTypeCode").toString(),
				equipment.get("equipmentTypeName").toString());
		equipmentResponse.setCount(Long.parseLong(equipment.get("count").toString()));
		Long availableCount = equipmentJpaDao
				.findCountByGroupIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNullAndGroupByGroupId(groupId, true);
		equipmentResponse.setAvailableCount(availableCount == null ? 0l : availableCount);
		attributes = equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(equipmentResponse.getEquipmentTypeId(),
				true);
		attributeValues = equipmentAttributeValueJpaDao
				.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(
						equipmentResponse.getGroupId(), true);
		if (attributes != null)
			equipmentResponse.setAttributeValues(setAttributeValues(attributes, attributeValues, true));
		return equipmentResponse;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public List<EquipmentResponse> findByGroupId(String groupId) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentJpaDao.existsByGroupIdAndOrganizationIdAndActive(groupId, organizationId, true)) {
			throw new NotFoundException(String.format("This group id (%s) not found", groupId));
		}
		List<EquipmentResponse> equipments = equipmentJpaDao.findByGroupIdAndOrganizationIdAndActive(groupId,
				organizationId, true);
		List<EquipmentTypeAttributeResponse> attributes = null;
		if (!equipments.isEmpty())
			attributes = equipmentTypeAttributeJpaDao
					.findByEquipmentTypeIdAndActive(equipments.get(0).getEquipmentTypeId(), true);
		for (EquipmentResponse equipment : equipments) {
			equipment.setAttributeValues(setAttributeValues(attributes,
					equipmentAttributeValueJpaDao.findAttributeIdAndValueByEquipmentId(equipment.getId()), false));
		}
		return equipments;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public EquipmentResponse findById(String id) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentJpaDao.existsByIdAndOrganizationIdAndActive(id, organizationId, true)) {
			throw new NotFoundException(String.format("This equipment (%s) not found", id));
		}
		EquipmentResponse equipment = equipmentJpaDao.findByIdAndOrganizationIdAndActive(id, organizationId, true);
		List<EquipmentTypeAttributeResponse> attributes = null;
		if (equipment != null)
			attributes = equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(equipment.getEquipmentTypeId(),
					true);
		equipment.setAttributeValues(setAttributeValues(attributes,
				equipmentAttributeValueJpaDao.findAttributeIdAndValueByEquipmentId(equipment.getId()), false));
		Tuple tuple = equipmentMappingJpaDao.findByEquipmentId(id);
		if (tuple != null) {
			EquipmentMappingResponse equipmentMappingResponse = new EquipmentMappingResponse(
					tuple.get("locationId").toString(),
					tuple.get("armId") == null ? null : tuple.get("armId").toString(),
					tuple.get("laneId") == null ? null : tuple.get("laneId").toString(), null, null, null);
			equipmentMappingResponse.setArmName(tuple.get("armName") == null ? null : tuple.get("armName").toString());
			equipmentMappingResponse
					.setLaneName(tuple.get("laneName") == null ? null : tuple.get("laneName").toString());
			equipmentMappingResponse
					.setLocationName(tuple.get("locationName") == null ? null : tuple.get("locationName").toString());
			equipmentMappingResponse.setLocationTypeName(
					tuple.get("locationTypeName") == null ? null : tuple.get("locationTypeName").toString());
			equipment.setEquipmentMapping(equipmentMappingResponse);
		}
		return equipment;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_FETCH)
	public List<EquipmentResponse> findByLocationIdAndGroupIdAndArmIdAndLaneId(String locationId, String groupId,
			String armId, String laneId) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentJpaDao.existsByGroupIdAndOrganizationIdAndActive(groupId, organizationId, true)) {
			throw new NotFoundException(String.format("This group id (%s) not found", groupId));
		}
		List<EquipmentResponse> equipments;
		if (armId == null) {
			equipments = equipmentJpaDao
					.findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdNullAndEquipmentMappingLaneIdNull(
							groupId, organizationId, true, locationId);
		} else if (armId != null && laneId == null) {
			equipments = equipmentJpaDao
					.findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdAndEquipmentMappingLaneIdNull(
							groupId, organizationId, true, locationId, armId);
		} else {
			equipments = equipmentJpaDao
					.findByGroupIdAndOrganizationIdAndActiveAndEquipmentMappingLocationIdAndEquipmentMappingArmIdAndEquipmentMappingLaneId(
							groupId, organizationId, true, locationId, armId, laneId);
		}
		List<EquipmentTypeAttributeResponse> attributes = null;
		if (!equipments.isEmpty())
			attributes = equipmentTypeAttributeJpaDao
					.findByEquipmentTypeIdAndActive(equipments.get(0).getEquipmentTypeId(), true);
		for (EquipmentResponse equipment : equipments) {
			equipment.setAttributeValues(setAttributeValues(attributes,
					equipmentAttributeValueJpaDao.findAttributeIdAndValueByEquipmentId(equipment.getId()), false));
		}
		return equipments;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_UPDATE)
	public EquipmentResponse updateByGroupId(String groupId, EquipmentRequest request) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentJpaDao.existsByGroupIdAndOrganizationIdAndActive(groupId, organizationId, true)) {
			throw new NotFoundException(String.format("This group id (%s) not found", groupId));
		}
		EquipmentResponse equipment = get(groupId, organizationId);
		String equipmentTypeId = equipment.getEquipmentTypeId();
		Integer limit = equipmentTypeJpaDao.findQuantityById(equipment.getEquipmentTypeId());

		Map<String, EquipmentTypeAttributeResponse> equipmentTypeAttributes = getAttributes(equipmentTypeId);
		Integer equipmentCount = null;
		for (EquipmentAttributeValueRequest attribute : request.getAttributesValue()) {
			if (equipmentTypeAttributes.get(attribute.getAttributeId()) == null) {
				throw new ValidationException(
						String.format("This equipment attribute (%s) not found", attribute.getAttributeId()));
			}
			if (equipmentTypeAttributes.get(attribute.getAttributeId()).getName().equalsIgnoreCase("quantity")) {
				if (Integer.parseInt(attribute.getValue()) > 0) {
					equipmentCount = Integer.parseInt(attribute.getValue());
				} else {
					throw new ValidationException(
							String.format("Equipment count(%d) can't be less than 1", attribute.getValue()));
				}
			}
		}
		if (equipmentCount == null) {
			throw new ValidationException("Equipment quantity not found");
		}
		Long count = equipmentJpaDao.countByEquipmentTypeIdAndOrganizationIdAndActive(equipmentTypeId, organizationId,
				true);
		if ((count - equipment.getCount() + equipmentCount) > limit) {
			throw new ValidationException("Equipment limit exceed");
		}
		validate(request.getAttributesValue(), equipmentTypeAttributes, equipmentCount);
		Integer equipmentSequence = sequenceGenerator("EQMST"),
				equipmentAttributeSequence = sequenceGenerator("EQATDT"),
				equipmentNameSequence = sequenceGenerator(String.format("EQMST%s", equipmentTypeId));
		String equipmentTypeName = equipment.getEquipmentTypeName();
		if (equipment.getCount().intValue() < equipmentCount.intValue()) {
			Equipment sEquipment = null;
			for (int i = 0; i < (equipmentCount - equipment.getCount().intValue()); i++) {
				sEquipment = equipmentJpaDao.save(new Equipment(String.format("EQMST%06d", equipmentSequence),
						String.format("%s %d", equipmentTypeName, equipmentNameSequence), groupId, equipmentTypeId,
						organizationId));
				for (EquipmentAttributeValueRequest attributeValue : request.getAttributesValue()) {
					if (equipmentTypeAttributes.get(attributeValue.getAttributeId()).getIsSpecific()) {
						equipmentAttributeValueJpaDao.save(new EquipmentAttributeValue(
								String.format("ATDT%07d", equipmentAttributeSequence), attributeValue.getAttributeId(),
								sEquipment.getId(), attributeValue.getAttributeValues()
										.get(i + equipment.getCount().intValue()).getValue()));
					} else {
						equipmentAttributeValueJpaDao.save(new EquipmentAttributeValue(
								String.format("ATDT%07d", equipmentAttributeSequence), attributeValue.getAttributeId(),
								sEquipment.getId(), attributeValue.getValue()));
					}
					equipmentAttributeSequence += 1;
				}
				equipmentSequence += 1;
				equipmentNameSequence += 1;
			}

		} else if (equipment.getCount().intValue() > equipmentCount.intValue()) {
			List<String> ids = equipmentJpaDao
					.findIdsByGroupIdAndOrganizationIdAndActiveAndLaneIdNullAndLocationIdNullAndArmIdNull(groupId,
							organizationId, true);
			if (ids.size() < equipment.getCount().intValue() - equipmentCount.intValue()) {
				throw new ValidationException("Some of the equipment are mapped with any arm or lane or location");
			}
			List<String> equipmentIds = new ArrayList<>(ids.subList(equipmentCount.intValue(), ids.size()));
			int rows = equipmentJpaDao.delete(equipmentIds, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Equipment {} deleted successfully", equipmentIds);
			}
			rows = 0;
			rows = equipmentAttributeValueJpaDao.delete(equipmentIds, getUserId(), new Date());
			if (rows > 0) {
				logger.info("Equipment attribute values deleted successfully");
			}
		}

		int rows = 0;
		Set<String> equipmentIds = new HashSet<>(
				equipmentJpaDao.findIdByOrganizationIdAndActiveAndGroupId(organizationId, true, groupId));
		if (!equipmentIds.isEmpty()) {
			for (EquipmentAttributeValueRequest attributeValue : request.getAttributesValue()) {
				if (equipmentTypeAttributes.get(attributeValue.getAttributeId()).getIsSpecific()) {
					List<String> specificEquipmentIds = new ArrayList<>(equipmentIds);
					int i = 0;
					for (EquipmentAttributeValueRequest specificValue : attributeValue.getAttributeValues()) {
						if (specificValue.getEquipmentId() != null) {
							specificEquipmentIds.remove(specificValue.getEquipmentId());
							rows = equipmentAttributeValueJpaDao.update(attributeValue.getAttributeId(),
									specificValue.getValue(), specificValue.getEquipmentId());
							if (rows == 0) {
								equipmentAttributeValueJpaDao.save(new EquipmentAttributeValue(
										String.format("ATDT%07d", equipmentAttributeSequence++),
										attributeValue.getAttributeId(), specificValue.getEquipmentId(),
										specificValue.getValue()));
								logger.info("New equipment attribute {} value updated for equipment {}",
										attributeValue.getAttributeId(), specificValue.getEquipmentId());
							}
						} else {
							rows = equipmentAttributeValueJpaDao.update(attributeValue.getAttributeId(),
									specificValue.getValue(), specificEquipmentIds.get(i++));
							if (rows == 0) {
								equipmentAttributeValueJpaDao.save(new EquipmentAttributeValue(
										String.format("ATDT%07d", equipmentAttributeSequence++),
										attributeValue.getAttributeId(), specificEquipmentIds.get((i - 1)),
										specificValue.getValue()));
								logger.info("New equipment attribute {} value updated for equipment {}",
										attributeValue.getAttributeId(), specificValue.getEquipmentId() != null
												? specificValue.getEquipmentId() : specificEquipmentIds.get((i - 1)));
							}

						}

					}

				} else {
					rows = equipmentAttributeValueJpaDao.update(attributeValue.getAttributeId(),
							attributeValue.getValue(), equipmentIds);
					if (rows == 0) {
						for (String equipmentId : equipmentIds) {
							equipmentAttributeValueJpaDao.save(
									new EquipmentAttributeValue(String.format("ATDT%07d", equipmentAttributeSequence++),
											attributeValue.getAttributeId(), equipmentId, attributeValue.getValue()));
						}
						logger.info("New equipment attribute {} value updated for equipments {}",
								attributeValue.getAttributeId(), String.join(",", equipmentIds));
					}
				}
				if (rows > 0) {
					logger.info("Equipment group {} attribute {} value {} updated successfully", groupId,
							attributeValue.getAttributeId(), attributeValue.getValue());
				}
			}
		}
		updateSequenceGenerator("EQMST", equipmentSequence);
		updateSequenceGenerator(String.format("EQMST%s", equipmentTypeId), equipmentNameSequence);
		updateSequenceGenerator("EQATDT", equipmentAttributeSequence);
		equipment.setCount(equipmentCount.longValue());
		if (equipmentIds != null && !equipmentIds.isEmpty()) {
			List<EquipmentTypeAttributeResponse> attributes = new ArrayList<>(equipmentTypeAttributes.values());
			equipment.setAttributeValues(setAttributeValues(attributes, equipmentAttributeValueJpaDao
					.findAttributeIdAndValueAndEquipmentIdAndEquipmentNameByEquipmentGroupIdAndActive(groupId, true),
					true));
		}
		return equipment;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_DELETE)
	public SuccessResponse delete(String groupId) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentJpaDao.existsByGroupIdAndOrganizationIdAndActive(groupId, organizationId, true)) {
			throw new NotFoundException(String.format("This group id (%s) not found", groupId));
		}
		if (equipmentMappingJpaDao.existsByEquipmentGroupIdAndEquipmentActive(groupId, true)) {
			throw new ValidationException("This equipment is mapped with location or lane or arm");
		}
		List<String> equipmentIds = equipmentJpaDao.findIdByOrganizationIdAndActiveAndGroupId(organizationId, true,
				groupId);
		int rows = equipmentJpaDao.delete(groupId, organizationId, getUserId(), new Date());
		if (rows > 0) {
			logger.info("Equipment group {} deleted successfully", groupId);
		}
		rows = 0;
		rows = equipmentAttributeValueJpaDao.delete(equipmentIds, getUserId(), new Date());
		return new SuccessResponse(HttpStatus.OK.value(), "Equipment successfully deleted");
	}
}
