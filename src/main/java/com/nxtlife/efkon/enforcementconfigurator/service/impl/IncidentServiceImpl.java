package com.nxtlife.efkon.enforcementconfigurator.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.IncidentAttributeIncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.IncidentAttributeJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.IncidentJpaDao;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.Incident;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttribute;
import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttributeIncident;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.enums.IncidentType;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;
import com.nxtlife.efkon.enforcementconfigurator.service.IncidentService;
import com.nxtlife.efkon.enforcementconfigurator.util.AuthorityUtils;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentAttributeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentAttributeResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentRequest;

@Service("incidentServiceImpl")
@Transactional
public class IncidentServiceImpl extends BaseService implements IncidentService {

	@Autowired
	private IncidentJpaDao incidentJpaDao;

	@Autowired
	private IncidentAttributeJpaDao incidentAttributeJpaDao;

	@Autowired
	private IncidentAttributeIncidentJpaDao incidentAttributeIncidentJpaDao;

	private void validate(List<IncidentRequest> request) {
		for (IncidentRequest incident : request) {
			if (!IncidentType.matches(incident.getType()))
				throw new ValidationException(String.format("Incident type (%s) not exist", incident.getType()));
			if (incident.getLocationRelated() != null && incident.getLocationRelated() < 0
					&& incident.getLocationRelated() > 2) {
				throw new ValidationException(
						String.format("Location related (%s) not valid", incident.getLocationRelated()));
			}
			if (incident.getLocationTypeRelated() != null && incident.getLocationTypeRelated() < 0
					&& incident.getLocationTypeRelated() > 2) {
				throw new ValidationException(
						String.format("Location type related (%s) not valid", incident.getLocationTypeRelated()));
			}
			for (IncidentAttributeRequest attributeRequest : incident.getAttributes()) {
				if (!DataType.matches(attributeRequest.getDataType()))
					throw new ValidationException(
							String.format("Data type (%s) not exist", attributeRequest.getDataType()));
			}
		}

	}

	@Override
	@Secured(AuthorityUtils.INCIDENT_CREATE)
	public SuccessResponse save(List<IncidentRequest> request) {
		validate(request);
		Set<String> incidentNames = incidentJpaDao.findAllNames();
		List<IncidentAttributeResponse> incidentAttributes = incidentAttributeJpaDao.findByActive(true);
		Map<String, Long> incidentAttributeMap = new HashMap<>();
		incidentAttributes.forEach(attribute -> {
			incidentAttributeMap.putIfAbsent(attribute.getName().toUpperCase(), unmask(attribute.getId()));
		});
		List<Incident> incidents = new ArrayList<>();
		List<IncidentAttributeIncident> incidentAttributeIncidents = new ArrayList<>();
		Incident incident;
		IncidentAttribute incidentAttribute;
		for (IncidentRequest incidentRequest : request) {
			if (!incidentNames.contains(incidentRequest.getName())) {
				incident = incidentRequest.toEntity();
				if (incidentRequest.getLocationRelated() != null) {
					incident.setLocationRelated(incidentRequest.getLocationRelated());
				} else {
					incident.setLocationRelated(0);
				}
				if (incidentRequest.getLocationTypeRelated() != null) {
					incident.setLocationTypeRelated(incidentRequest.getLocationTypeRelated());
				} else {
					incident.setLocationTypeRelated(0);
				}
				incident.setIncidentType(IncidentType.valueOf(incidentRequest.getType()));
				incidentNames.add(incidentRequest.getName().toUpperCase());
				incidents.add(incident);

			}
			for (IncidentAttributeRequest attributeRequest : incidentRequest.getAttributes()) {
				if (!incidentAttributeMap.containsKey(attributeRequest.getName().toUpperCase())) {
					incidentAttribute = attributeRequest.toEntity();
					incidentAttribute.setDataType(DataType.valueOf(attributeRequest.getDataType()));
					incidentAttributeJpaDao.save(incidentAttribute);
					incidentAttributeIncidents
							.add(new IncidentAttributeIncident(incidentRequest.getName(), incidentAttribute.getId()));
					incidentAttributeMap.putIfAbsent(attributeRequest.getName().toUpperCase(),
							incidentAttribute.getId());
				} else {
					incidentAttributeIncidents.add(new IncidentAttributeIncident(incidentRequest.getName(),
							incidentAttributeMap.get(attributeRequest.getName().toUpperCase())));
				}
			}
		}
		incidentJpaDao.saveAll(incidents);
		incidentAttributeIncidentJpaDao.saveAll(incidentAttributeIncidents);
		return new SuccessResponse(HttpStatus.OK.value(), "Incidents successfully saved");

	}

}
