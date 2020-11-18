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

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationAttributeValueJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OrganizationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationTypeAttributeService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeResponse;

@Service("locationTypeAttributeServiceImpl")
@Transactional
@DependsOn(value = { "locationTypeServiceImpl" })
public class LocationTypeAttributeServiceImpl extends BaseService implements LocationTypeAttributeService {

	@Autowired
	private LocationTypeAttributeJpaDao locationTypeAttributeJpaDao;

	@Autowired
	private LocationTypeJpaDao locationTypeJpaDao;

	@Autowired
	private LocationAttributeValueJpaDao locationAttributeValueJpaDao;

	@Autowired
	private OrganizationJpaDao organizationJpaDao;

	private static Logger logger = org.slf4j.LoggerFactory.getLogger(LocationTypeAttributeServiceImpl.class);

	@Value("${efkon.organization.code}")
	private String organizationCode;

	@PostConstruct
	private void init() {
		Long organizationId = organizationJpaDao.findIdByCode(organizationCode);
		if (organizationId != null) {
			List<String> locationTypeIds = locationTypeJpaDao.findIdsByOrganizationId(organizationId);
			for (String locationTypeId : locationTypeIds) {
				if (!locationTypeAttributeJpaDao.existsByNameAndLocationTypeId("Name", locationTypeId))
					locationTypeAttributeJpaDao.save(new LocationTypeAttribute(String.format("AT01%s", locationTypeId),
							"Name", DataType.Text, true, locationTypeId, organizationId));
				if (!locationTypeAttributeJpaDao.existsByNameAndLocationTypeId("Latitude", locationTypeId))
					locationTypeAttributeJpaDao.save(new LocationTypeAttribute(String.format("AT02%s", locationTypeId),
							"Latitude", DataType.Text, true, locationTypeId, organizationId));
				if (!locationTypeAttributeJpaDao.existsByNameAndLocationTypeId("Longitude", locationTypeId))
					locationTypeAttributeJpaDao.save(new LocationTypeAttribute(String.format("AT03%s", locationTypeId),
							"Longitude", DataType.Text, true, locationTypeId, organizationId));
				if (!locationTypeAttributeJpaDao.existsByNameAndLocationTypeId("Address", locationTypeId))
					locationTypeAttributeJpaDao.save(new LocationTypeAttribute(String.format("AT04%s", locationTypeId),
							"Address", DataType.Text, true, locationTypeId, organizationId));
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

	private void validateRequest(List<LocationTypeAttributeRequest> requestList,
			List<LocationTypeAttributeResponse> existsAttributes) {
		Set<String> requestAttributeNames = new HashSet<>(), existsAttributeNames = new HashSet<>();
		Set<String> existsAttributeIds = new HashSet<>();
		existsAttributes.forEach(existsAttribute -> {
			existsAttributeNames.add(existsAttribute.getName());
			existsAttributeIds.add(existsAttribute.getId());
		});
		for (LocationTypeAttributeRequest request : requestList) {
			if (request.getDataType() != null && !DataType.matches(request.getDataType())) {
				throw new ValidationException(String.format("Data type (%s) incorrect", request.getDataType()));
			}
			if (request.getId() == null && existsAttributeNames.contains(request.getName())) {
				throw new ValidationException(
						String.format("Duplicate attribute (%s) for location type", request.getName()));
			}
			if (request.getId() != null && !existsAttributeIds.contains(request.getId())) {
				throw new ValidationException(
						String.format("This location type attribute (%s) not exists", request.getId()));
			}
			if (requestAttributeNames.contains(request.getName())) {
				throw new ValidationException(
						String.format("Duplicate attribute (%s) for location type", request.getName()));
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
	@Secured(AuthorityUtils.LOCATION_TYPE_FETCH)
	public List<LocationTypeAttributeResponse> findAllByLocationTypeId(String locationTypeId) {
		Boolean exist = locationTypeJpaDao.existsById(locationTypeId);
		if (!exist) {
			logger.error("Location Type {} not found", locationTypeId);
			throw new NotFoundException(String.format("Equipment Type (%s) not found", locationTypeId));
		}
		return locationTypeAttributeJpaDao.findByLocationTypeIdAndActiveOrderByFixedDesc(locationTypeId, true);
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_TYPE_CREATE)
	public List<LocationTypeAttributeResponse> save(String locationTypeId, List<LocationTypeAttributeRequest> request) {
		Long organizationId = getUser().gettOrganizationId();
		if (!locationTypeJpaDao.existsById(locationTypeId)) {
			throw new NotFoundException(String.format("Location type (%s) not found", locationTypeId));
		}
		List<LocationTypeAttributeResponse> existsAttributes = locationTypeAttributeJpaDao
				.findByLocationTypeIdAndActiveOrderByFixedDesc(locationTypeId, true);
		validateRequest(request, existsAttributes);
		Map<String, LocationTypeAttributeResponse> attributeLookup = new HashMap<>();
		existsAttributes.forEach(attribute -> {
			attributeLookup.putIfAbsent(attribute.getId(), attribute);
		});
		LocationTypeAttribute locationTypeAttribute;
		List<LocationTypeAttribute> locationTypeAttributes = new ArrayList<>();
		Long count = locationTypeAttributeJpaDao.findCountByLocationTypeId(locationTypeId);
		for (LocationTypeAttributeRequest locationTypeAttributeRequest : request) {
			if (locationTypeAttributeRequest.getId() == null) {
				locationTypeAttribute = locationTypeAttributeRequest.toEntity();
				locationTypeAttribute.setId(String.format("AT%02d%s", count == null ? 1 : ++count, locationTypeId));
				locationTypeAttribute.setFixed(false);
				locationTypeAttribute.settLocationTypeId(locationTypeId);
				locationTypeAttribute.settOrganizationId(organizationId);
				locationTypeAttributes.add(locationTypeAttribute);
			} else {
				if (locationAttributeValueJpaDao.existsByLocationTypeAttributeIdAndActiveAndLocationActive(
						locationTypeAttributeRequest.getId(), true, true)
						&& (!locationTypeAttributeRequest.getDataType()
								.equals(attributeLookup.get(locationTypeAttributeRequest.getId()).getDataType().name())
								|| !locationTypeAttributeRequest.getName()
										.equals(attributeLookup.get(locationTypeAttributeRequest.getId()).getName())
								|| (locationTypeAttributeRequest.getOptions() != null && !locationTypeAttributeRequest
										.getOptions().equals(String.join(",", attributeLookup
												.get(locationTypeAttributeRequest.getId()).getOptions()))))) {
					throw new ValidationException("You can't update attribute details. Because its already used");
				}
				int row = locationTypeAttributeJpaDao.updateNameAndDataTypeAndOptionsById(
						locationTypeAttributeRequest.getName(), locationTypeAttributeRequest.getDataType(),
						locationTypeAttributeRequest.getOptions(), getUserId(), new Date(),
						locationTypeAttributeRequest.getId());
				if (row > 0)
					logger.info("Location type attribute {} successfully updated",
							locationTypeAttributeRequest.getId());
			}
		}
		locationTypeAttributeJpaDao.saveAll(locationTypeAttributes);
		return locationTypeAttributeJpaDao.findByLocationTypeIdAndActiveOrderByFixedDesc(locationTypeId, true);
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_TYPE_DELETE)
	public List<LocationTypeAttributeResponse> deleteByLocationTypeAttributeId(String locationTypeId,
			String locationTypeAttributeId) {
		if (!locationTypeJpaDao.existsById(locationTypeId)) {
			throw new NotFoundException(String.format("Location type (%s) not found", locationTypeId));
		}
		LocationTypeAttributeResponse locationTypeAttributeResponse = locationTypeAttributeJpaDao
				.findResponseById(locationTypeAttributeId);
		if (locationTypeAttributeResponse == null) {
			throw new NotFoundException(
					String.format("This location type attribute (%s) not found", locationTypeAttributeId));
		}
		if (locationTypeAttributeResponse.getFixed()) {
			throw new ValidationException("You can't delete this attribute because it is mandatory attribute");
		}
		int row = locationTypeAttributeJpaDao.delete(locationTypeAttributeId, getUserId(), new Date());
		if (row > 0) {
			logger.info("Location type attribute {} successfully deleted", locationTypeAttributeId);
		}
		return locationTypeAttributeJpaDao.findByLocationTypeIdAndActiveOrderByFixedDesc(locationTypeId, true);
	}

}
