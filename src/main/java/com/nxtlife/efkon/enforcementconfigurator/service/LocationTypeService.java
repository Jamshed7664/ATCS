package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeResponse;

public interface LocationTypeService {
	
	/**
	 * this method used to fetch all location type for an organization
	 * 
	 * @return list of {@link LocationTypeResponse}
	 */
	public List<LocationTypeResponse> findAll();

}
