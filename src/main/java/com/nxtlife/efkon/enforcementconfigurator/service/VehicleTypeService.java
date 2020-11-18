package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type.VehicleTypeResponse;

public interface VehicleTypeService {

	/**
	 * this method used to fetch all the vehicle types
	 *
	 * @return list of {@link VehicleTypeResponse}
	 */
	public List<VehicleTypeResponse> findAll();
}
