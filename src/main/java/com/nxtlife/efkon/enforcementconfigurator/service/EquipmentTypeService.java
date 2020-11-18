package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;
import java.util.Map;

import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeResponse;

public interface EquipmentTypeService {

	/**
	 * this method used to save equipment type
	 * 
	 * @param equipmentTypeRequest
	 * @return {@link EquipmentTypeResponse}
	 */
	public EquipmentTypeResponse save(EquipmentTypeRequest equipmentTypeRequest);

	/**
	 * this method used to fetch all equipment type for an organization
	 *
	 * @return list of {@link EquipmentTypeResponse}
	 */
	public List<EquipmentTypeResponse> findAll();

	/**
	 * this method used to fetch equipment types with available count and its total
	 * count.
	 * 
	 * @return {@link EquipmentTypeResponse}
	 */
	public List<EquipmentTypeResponse> findAllWithItsTotalCountAndAvailableCount();

	/**
	 * this method used to get lookup for equipment type.
	 * 
	 * @param organizationId
	 * @param active
	 * @return key value pair of equipment type id and its details
	 */
	public Map<String, EquipmentTypeResponse> findByOrganizationIdAndActive(Long organizationId, Boolean active);

	/**
	 * this method used to update quantity of equipment type
	 * 
	 * @param id
	 * @param quantity
	 * @return
	 */
	public EquipmentTypeResponse updateQuantity(String id, Integer quantity);

	/**
	 * this method used to delete equipment type
	 * 
	 * @param id
	 * @return {@link SuccessResponse}
	 */
	public SuccessResponse delete(String id);

}
