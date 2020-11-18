package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentLocationMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingRequest;

public interface EquipmentMappingService {

	/**
	 * This method used to map equipment with the location location can be
	 * junction,arm,lane,highway etc..
	 *
	 * @return {@Link SuccessResponse} if equipment successfully mapped with
	 *         location
	 * @throws NotFoundException
	 *             if location id not found
	 * @throws ValidationException
	 *             if equipment type have specific attribute and equipment ids
	 *             are empty if equipment type have non specific attribute and
	 *             count is empty if arm id not related to location id if lane
	 *             id not related to arm id if equipment type ids not valid or
	 *             already maped with the location
	 * @Param locationId
	 * @param list
	 *            of {@link EquipmentMappingRequest}
	 */
	public SuccessResponse saveEquipmentMappingForLocation(String locationId, List<EquipmentMappingRequest> request);

	/**
	 * this method used to fetch location and equipment details for equipment
	 * mapping.
	 * 
	 * @param locationId
	 * @return {@link EquipmentLocationMappingResponse}
	 */
	public EquipmentLocationMappingResponse findEquipmentMappingByLocationId(String locationId);

}