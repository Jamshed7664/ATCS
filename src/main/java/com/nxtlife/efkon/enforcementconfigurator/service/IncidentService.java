package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentRequest;

public interface IncidentService {

	/**
	 * this method used to save list of incidents
	 *
	 * @param request
	 * @return {@link SuccessResponse}
	 * 
	 * @throws ValidationException if incident type not exist or data type of
	 *                             incident attribute not exist
	 */
	public SuccessResponse save(List<IncidentRequest> request);

}
