package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentAttributeValueJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OrganizationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentTypeAttributeService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;

@Service("equipmentTypeAttributeServiceImpl")
@Transactional
@DependsOn(value = { "equipmentTypeServiceImpl" })
public class EquipmentTypeAttributeServiceImpl extends BaseService implements EquipmentTypeAttributeService {

	@Autowired
	private EquipmentTypeAttributeJpaDao equipmentTypeAttributeJpaDao;

	@Autowired
	private EquipmentTypeJpaDao equipmentTypeJpaDao;

	@Autowired
	private EquipmentAttributeValueJpaDao equipmentAttributeValueJpaDao;

	@Autowired
	private OrganizationJpaDao organizationJpaDao;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(EquipmentTypeAttributeServiceImpl.class);

	@Value("${efkon.organization.code}")
	private String organizationCode;

	@PostConstruct
	private void init() {
		Long organizationId = organizationJpaDao.findIdByCode(organizationCode);
		if (organizationId != null) {
			List<String> equipmentIds = equipmentTypeJpaDao.findAllIdsByOrganizationId(organizationId);
			for (String equipmentId : equipmentIds) {
				if (!equipmentTypeAttributeJpaDao.existsByNameAndEquipmentTypeId("Manufacturer", equipmentId))
					equipmentTypeAttributeJpaDao.save(new EquipmentTypeAttribute(String.format("AT01%s", equipmentId),
							"Manufacturer", DataType.Text, true, false, equipmentId, organizationId));
				if (!equipmentTypeAttributeJpaDao.existsByNameAndEquipmentTypeId("Model", equipmentId))
					equipmentTypeAttributeJpaDao.save(new EquipmentTypeAttribute(String.format("AT02%s", equipmentId),
							"Model", DataType.Text, true, false, equipmentId, organizationId));
				if (!equipmentTypeAttributeJpaDao.existsByNameAndEquipmentTypeId("Version", equipmentId))
					equipmentTypeAttributeJpaDao.save(new EquipmentTypeAttribute(String.format("AT03%s", equipmentId),
							"Version", DataType.Text, true, false, equipmentId, organizationId));
				if (!equipmentTypeAttributeJpaDao.existsByNameAndEquipmentTypeId("Quantity", equipmentId))
					equipmentTypeAttributeJpaDao.save(new EquipmentTypeAttribute(String.format("AT04%s", equipmentId),
							"Quantity", DataType.Number, true, false, equipmentId, organizationId));
			}
		}
	}

	/**
	 * this method used to validate request from user. If user enter duplicate
	 * attribute or data type is incorrect then it throws validation exception.
	 *
	 * @param requestList
	 * @param existsAttributeNames
	 */
	private void validateRequest(List<EquipmentTypeAttributeRequest> requestList,
			List<EquipmentTypeAttributeResponse> existsAttributes) {
		Set<String> requestAttributeNames = new HashSet<>(), existsAttributeNames = new HashSet<>();
		Set<String> existsAttributeIds = new HashSet<>();
		existsAttributes.forEach(existsAttribute -> {
			existsAttributeNames.add(existsAttribute.getName());
			existsAttributeIds.add(existsAttribute.getId());
		});
		for (EquipmentTypeAttributeRequest request : requestList) {
			if (request.getDataType() != null && !DataType.matches(request.getDataType())) {
				throw new ValidationException(String.format("Data type (%s) incorrect", request.getDataType()));
			}
			if (request.getId() == null && existsAttributeNames.contains(request.getName())) {
				throw new ValidationException(
						String.format("Duplicate attribute (%s) for equipment type", request.getName()));
			}
			if (request.getId() != null && !existsAttributeIds.contains(request.getId())) {
				throw new ValidationException(
						String.format("This equipment type attribute (%s) not exists", request.getId()));
			}
			if (requestAttributeNames.contains(request.getName())) {
				throw new ValidationException(
						String.format("Duplicate attribute (%s) for equipment type", request.getName()));
			}
			if ((!request.getDataType().equalsIgnoreCase(DataType.Option.name())
					&& !request.getDataType().equalsIgnoreCase(DataType.Range.name()))
					&& request.getOptions() != null) {
				throw new ValidationException("Options attribute is valid for only option and range");
			}
			if ((request.getDataType().equalsIgnoreCase(DataType.Option.name())
					|| request.getDataType().equalsIgnoreCase(DataType.Range.name()))
					&& (request.getOptions() == null || request.getOptions().isEmpty())) {
				throw new ValidationException("Options attribute should be their for only option and range");
			}
			requestAttributeNames.add(request.getName());
		}

	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_FETCH)
	public List<EquipmentTypeAttributeResponse> findAllByEquipmentTypeId(String equipmentTypeId) {
		Boolean exist = equipmentTypeJpaDao.existsById(equipmentTypeId);
		if (!exist) {
			logger.error("Equipment Type {} not found", equipmentTypeId);
			throw new NotFoundException(String.format("Equipment Type (%s) not found", equipmentTypeId));
		}
		return equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(equipmentTypeId, true);
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_CREATE)
	public List<EquipmentTypeAttributeResponse> save(String equipmentTypeId,
			List<EquipmentTypeAttributeRequest> request) {
		Long organizationId = getUser().gettOrganizationId();
		if (!equipmentTypeJpaDao.existsById(equipmentTypeId)) {
			throw new NotFoundException(String.format("Equipment type (%s) not found", equipmentTypeId));
		}
		List<EquipmentTypeAttributeResponse> existsAttributes = equipmentTypeAttributeJpaDao
				.findByEquipmentTypeIdAndActive(equipmentTypeId, true);
		validateRequest(request, existsAttributes);
		Map<String, EquipmentTypeAttributeResponse> attributeLookup = new HashMap<>();
		existsAttributes.forEach(attribute -> {
			attributeLookup.putIfAbsent(attribute.getId(), attribute);
		});
		EquipmentTypeAttribute equipmentTypeAttribute;
		Long count = equipmentTypeAttributeJpaDao.findCountByEquipmentTypeId(equipmentTypeId);
		List<EquipmentTypeAttribute> equipmentTypeAttributes = new ArrayList<>();
		for (EquipmentTypeAttributeRequest equipmentTypeAttributeRequest : request) {
			if (equipmentTypeAttributeRequest.getId() == null) {
				equipmentTypeAttribute = equipmentTypeAttributeRequest.toEntity();
				equipmentTypeAttribute.setId(String.format("AT%02d%s", count == null ? 1 : ++count, equipmentTypeId));
				equipmentTypeAttribute.setFixed(false);
				equipmentTypeAttribute.settEquipmentTypeId(equipmentTypeId);
				equipmentTypeAttribute.settOrganizationId(organizationId);
				equipmentTypeAttributes.add(equipmentTypeAttribute);
			} else {
				if (equipmentAttributeValueJpaDao.existsByEquipmentTypeAttributeIdAndActiveAndEquipmentActive(
						equipmentTypeAttributeRequest.getId(), true, true)
						&& (!equipmentTypeAttributeRequest.getDataType()
								.equals(attributeLookup.get(equipmentTypeAttributeRequest.getId()).getDataType().name())
								|| !equipmentTypeAttributeRequest.getName()
										.equals(attributeLookup.get(equipmentTypeAttributeRequest.getId()).getName())
								|| !equipmentTypeAttributeRequest.getIsSpecific().equals(
										attributeLookup.get(equipmentTypeAttributeRequest.getId()).getIsSpecific())
								|| (equipmentTypeAttributeRequest.getOptions() != null && !equipmentTypeAttributeRequest
										.getOptions().equals(String.join(",", attributeLookup
												.get(equipmentTypeAttributeRequest.getId()).getOptions()))))) {
					throw new ValidationException("You can't update attribute details. Because its already used");
				}
				if (attributeLookup.get(equipmentTypeAttributeRequest.getId()) != null
						&& attributeLookup.get(equipmentTypeAttributeRequest.getId()).getFixed()) {
					logger.info("Fixed equipment type attribute can't be update");
				} else {
					int row = equipmentTypeAttributeJpaDao.updateNameAndDataTypeAndIsSpecificAndOptionsById(
							equipmentTypeAttributeRequest.getName(), equipmentTypeAttributeRequest.getDataType(),
							equipmentTypeAttributeRequest.getIsSpecific(), equipmentTypeAttributeRequest.getOptions(),
							getUserId(), new Date(), equipmentTypeAttributeRequest.getId());
					if (row > 0)
						logger.info("Equipment type attribute {} successfully updated",
								equipmentTypeAttributeRequest.getId());
				}
			}
		}
		equipmentTypeAttributeJpaDao.saveAll(equipmentTypeAttributes);
		return equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(equipmentTypeId, true);
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_DELETE)
	public List<EquipmentTypeAttributeResponse> deleteByEquipmentTypeAttributeId(String equipmentTypeId,
			String equipmentTypeAttributeId) {
		if (!equipmentTypeJpaDao.existsById(equipmentTypeId)) {
			throw new NotFoundException(String.format("Equipment type (%s) not found", equipmentTypeId));
		}
		EquipmentTypeAttributeResponse equipmentTypeAttributeResponse = equipmentTypeAttributeJpaDao
				.findResponseById(equipmentTypeAttributeId);
		if (equipmentTypeAttributeResponse == null) {
			throw new NotFoundException(
					String.format("This equipment type attribute (%s) not found", equipmentTypeAttributeId));
		}
		if (equipmentTypeAttributeResponse.getFixed()) {
			throw new ValidationException("You can't delete this attribute because it is mandatory attribute");
		}
		int row = equipmentTypeAttributeJpaDao.delete(equipmentTypeAttributeId, getUserId(), new Date());
		if (row > 0) {
			logger.info("Equipment type attribute {} successfully deleted", equipmentTypeAttributeId);
		}
		return equipmentTypeAttributeJpaDao.findByEquipmentTypeIdAndActive(equipmentTypeId, true);
	}

}
