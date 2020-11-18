package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentMappingJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.EquipmentTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OrganizationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentType;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentTypeService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeResponse;

@Service("equipmentTypeServiceImpl")
@Transactional
public class EquipmentTypeServiceImpl extends BaseService implements EquipmentTypeService {

	@Autowired
	private EquipmentTypeJpaDao equipmentTypeJpaDao;

	@Autowired
	private EquipmentJpaDao equipmentJpaDao;

	@Autowired
	private EquipmentMappingJpaDao equipmentMappingJpaDao;

	@Autowired
	private OrganizationJpaDao organizationJpaDao;

	@Autowired
	private EquipmentTypeAttributeJpaDao equipmentTypeFieldJpaDao;

	@Value("${efkon.organization.code}")
	private String organizationCode;

	@PostConstruct
	private void init() {
		Long organizationId = organizationJpaDao.findIdByCode(organizationCode);
		if (organizationId != null) {
			Integer sequence = sequenceGenerator("EQT");
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("RSU", organizationId)) {
				equipmentTypeJpaDao.save(
						new EquipmentType(String.format("EQT%4d", sequence++), "RSU", "RSU", 100, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("CAM", organizationId)) {
				equipmentTypeJpaDao.save(
						new EquipmentType(String.format("EQT%4d", sequence++), "CAM", "Camera", 200, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("RAD", organizationId)) {
				equipmentTypeJpaDao.save(
						new EquipmentType(String.format("EQT%4d", sequence++), "RAD", "Radar", 20, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("FTP", organizationId)) {
				equipmentTypeJpaDao.save(new EquipmentType(String.format("EQT%4d", sequence++), "FTP", "FTP Server", 20,
						organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("WEB", organizationId)) {
				equipmentTypeJpaDao.save(
						new EquipmentType(String.format("EQT%4d", sequence++), "WEB", "WEB API", 20, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("DMS", organizationId)) {
				equipmentTypeJpaDao.save(new EquipmentType(String.format("EQT%4d", sequence++), "DMS", "DMS Server", 20,
						organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("DBS", organizationId)) {
				equipmentTypeJpaDao.save(
						new EquipmentType(String.format("EQT%4d", sequence++), "DBS", "Database", 10, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("HHD", organizationId)) {
				equipmentTypeJpaDao.save(new EquipmentType(String.format("EQT%4d", sequence++), "HHD", "HHD Device", 20,
						organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("BOC", organizationId)) {
				equipmentTypeJpaDao.save(new EquipmentType(String.format("EQT%4d", sequence++), "BOC",
						"Fixed-Box Camera", 200, organizationId));
			}
			if (!equipmentTypeJpaDao.existsByCodeAndOrganizationId("SAG", organizationId)) {
				equipmentTypeJpaDao.save(new EquipmentType(String.format("EQT%4d", sequence++), "SAG", "Server Agent",
						20, organizationId));
			}
			updateSequenceGenerator("EQT", sequence);
		}
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_CREATE)
	public EquipmentTypeResponse save(EquipmentTypeRequest equipmentTypeRequest) {
		Long organizationId = getUser().gettOrganizationId();
		if (equipmentTypeJpaDao.existsByCodeAndOrganizationId(equipmentTypeRequest.getCode(), organizationId)) {
			throw new ValidationException(
					String.format("Equipment type for this code (%s) already exists", equipmentTypeRequest.getCode()));
		}
		EquipmentType equipmentType = equipmentTypeRequest.toEntity();
		equipmentType.settOrganizationId(organizationId);
		Integer sequence = sequenceGenerator("EQT");
		equipmentType.setId(String.format("EQT%4d", sequence));
		equipmentTypeJpaDao.save(equipmentType);
		return equipmentTypeJpaDao.findByOrganizationIdAndActiveAndId(organizationId, true, equipmentType.getId());
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_FETCH)
	public List<EquipmentTypeResponse> findAll() {
		Long organizationId = getUser().gettOrganizationId();
		List<EquipmentTypeResponse> equipmentTypes = equipmentTypeJpaDao.findByOrganizationIdAndActive(organizationId,
				true);
		for (EquipmentTypeResponse equipmentType : equipmentTypes) {
			equipmentType.setEquipmentTypeAttributes(
					equipmentTypeFieldJpaDao.findByEquipmentTypeIdAndActive(equipmentType.getId(), true));
		}
		return equipmentTypes;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_FETCH)
	public List<EquipmentTypeResponse> findAllWithItsTotalCountAndAvailableCount() {
		Long organizationId = getUser().gettOrganizationId();
		List<EquipmentTypeResponse> equipmentTypes = equipmentTypeJpaDao.findByOrganizationIdAndActive(organizationId,
				true);
		List<Map<String, Object>> availableEquipmentTypes = equipmentJpaDao
				.findEquipmentTypeIdAndCountByOrganizationIdAndActiveAndGroupByEquipmentTypeId(organizationId, true);
		List<Map<String, Object>> equipmentTypesMappedCount = equipmentMappingJpaDao
				.findEquipmentTypeIdAndCountByActive(true);
		Map<String, Long> equipmentTypesCount = new HashMap<>();
		equipmentTypesMappedCount.forEach(equipmentGroup -> {
			equipmentTypesCount.putIfAbsent(equipmentGroup.get("equipmentTypeId").toString(),
					Long.parseLong(equipmentGroup.get("count").toString()));
		});
		Long mappedCount;
		int i = 0, availableEquipmentTypesSize = availableEquipmentTypes.size();
		for (EquipmentTypeResponse equipmentType : equipmentTypes) {
			if (i < availableEquipmentTypesSize
					&& equipmentType.getId().equals(availableEquipmentTypes.get(i).get("equipmentTypeId").toString())) {
				equipmentType.setCount(Long.parseLong(availableEquipmentTypes.get(i).get("count").toString()));
				equipmentType.setAvailableCount(equipmentType.getCount());
				if ((mappedCount = equipmentTypesCount
						.get(availableEquipmentTypes.get(i).get("equipmentTypeId").toString())) != null) {
					equipmentType.setAvailableCount(equipmentType.getCount() - mappedCount);
				}
				i++;
			} else {
				equipmentType.setCount(0l);
				equipmentType.setAvailableCount(0l);
			}
		}
		return equipmentTypes;
	}

	@Override
	@Cacheable
	public Map<String, EquipmentTypeResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active) {
		List<EquipmentTypeResponse> equipmentTypes = equipmentTypeJpaDao.findByOrganizationIdAndActive(organizationId,
				active);
		Map<String, EquipmentTypeResponse> equipmentTypesMap = new HashMap<>();
		equipmentTypes.forEach(equipmentType -> {
			equipmentTypesMap.putIfAbsent(equipmentType.getId(), equipmentType);
		});
		return equipmentTypesMap;
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_UPDATE)
	public EquipmentTypeResponse updateQuantity(String id, Integer quantity) {
		Long organizationId = getUser().gettOrganizationId();
		if (equipmentTypeJpaDao.existsByIdAndOrganizationId(id, organizationId)) {
			throw new NotFoundException(String.format("Equipment type for this id (%s) not exists", id));
		}
		equipmentTypeJpaDao.updateQuantity(id, quantity);
		return equipmentTypeJpaDao.findByOrganizationIdAndActiveAndId(organizationId, true, id);
	}

	@Override
	@Secured(AuthorityUtils.EQUIPMENT_TYPE_UPDATE)
	public SuccessResponse delete(String id) {
		Long organizationId = getUser().gettOrganizationId();
		if (equipmentTypeJpaDao.existsByIdAndOrganizationId(id, organizationId)) {
			throw new NotFoundException(String.format("Equipment type for this id (%s) not exists", id));
		}
		if (equipmentJpaDao.existsByEquipmentTypeIdAndOrganizationId(id, organizationId)) {
			throw new ValidationException("This equipment type is in use");
		}
		equipmentTypeJpaDao.deleteById(id);
		return new SuccessResponse(HttpStatus.OK.value(), "Successfully deleted");
	}
}
