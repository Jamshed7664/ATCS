package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.LocationTypeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OrganizationJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.type.LocationType;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationTypeService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeResponse;

@Service("locationTypeServiceImpl")
@Transactional
public class LocationTypeServiceImpl extends BaseService implements LocationTypeService {

	@Autowired
	private LocationTypeJpaDao locationTypeDao;

	@Autowired
	private LocationTypeAttributeJpaDao locationTypeAttributeJpaDao;

	@Autowired
	private OrganizationJpaDao organizationDao;

	@Value("${efkon.organization.code}")
	private String organizationCode;

	@Value("${efkon.junction.code}")
	private String junctionCode;

	@Value("${efkon.highway.code}")
	private String highwayCode;

	@PostConstruct
	private void init() {
		Long organizationId = organizationDao.findIdByCode(organizationCode);
		if (organizationId != null) {
			if (!locationTypeDao.existsByCodeAndOrganizationId(junctionCode, organizationId)) {
				locationTypeDao.save(new LocationType("LOCTYP0001", junctionCode, "Junction", organizationId));
			}
			if (!locationTypeDao.existsByCodeAndOrganizationId(highwayCode, organizationId)) {
				locationTypeDao.save(new LocationType("LOCTYP0002", highwayCode, "Highway", organizationId));
			}
			if (!locationTypeDao.existsByCodeAndOrganizationId("DTC", organizationId)) {
				locationTypeDao.save(new LocationType("LOCTYP0003", "DTC", "Data Centre", organizationId));
			}
			if (!locationTypeDao.existsByCodeAndOrganizationId("CTR", organizationId)) {
				locationTypeDao.save(new LocationType("LOCTYP0004", "CTR", "Control Room", organizationId));
			}
		}
	}

	@Override
	@Secured(AuthorityUtils.LOCATION_TYPE_FETCH)
	public List<LocationTypeResponse> findAll() {
		Long organizationId = getUser().gettOrganizationId();
		List<LocationTypeResponse> locationTypes = locationTypeDao.findByOrganizationIdAndActive(organizationId, true);
		for (LocationTypeResponse locationType : locationTypes) {
			locationType.setLocationTypeAttributes(
					locationTypeAttributeJpaDao.findByLocationTypeIdAndActiveOrderByFixedDesc(locationType.getId(), true));
		}
		return locationTypes;
	}

}
