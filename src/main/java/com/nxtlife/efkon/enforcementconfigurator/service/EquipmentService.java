package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;

public interface EquipmentService {

	/**
	 * This method used to save equipment details. Before saving equipment we
	 * are checking that equipment data type are not valid or all the fields
	 * values are not assigned or if value isn't according to data type.
	 * 
	 * @param equipment
	 * @return equipment details
	 * @throws ValidationException
	 *             if data type isn't valid or all fields are not assigned or if
	 *             value isn't according to data type
	 */
	public EquipmentResponse save(EquipmentRequest equipment);

	/**
	 * This method used to fetch equipment details for an organization
	 * 
	 * @return list of {@link EquipmentResponse}
	 */
	public List<EquipmentResponse> findAll();

	/**
	 * This method used to fetch equipment details with count and absolute count
	 * for an organization.
	 * 
	 * @return list of {@link EquipmentResponse}
	 */
	public List<EquipmentResponse> findAllGroupByGroupId();

	/**
	 * this method used to fetch equipment groups with count and available count
	 * for particular equipment type
	 * 
	 * @param equipmentTypeId
	 * @return list of {@link EquipmentResponse}
	 * @throws NotFoundException
	 *             if equipmentTypeId not found
	 */
	public List<EquipmentResponse> findByEquipmentTypeIdAndGroupByGroupId(String equipmentTypeId);

	/**
	 * This method used to fetch equipment details with count using groupId
	 * 
	 * @param groupId
	 * @return {@link EquipmentResponse}
	 * @throws NotFoundException
	 *             if group id is not valid
	 */
	public EquipmentResponse findByGroupIdAndGroupByGroupId(String groupId);

	/**
	 * this method used to get equipment details using equipment group id
	 * 
	 * @param groupId
	 * @return list of {@link EquipmentResponse}
	 */
	public List<EquipmentResponse> findByGroupId(String groupId);

	/**
	 * this method used to get equipment detail by id
	 * 
	 * @param id
	 * @return {@link EquipmentResponse}
	 */
	public EquipmentResponse findById(String id);

	/**
	 * 
	 * @param locationId
	 * @param groupId
	 * @return list of {@link EquipmentResponse}
	 */
	public List<EquipmentResponse> findByLocationIdAndGroupIdAndArmIdAndLaneId(String locationId, String groupId,
			String armId, String laneId);

	/**
	 * this method used to update group of equipment. In this method we can
	 * change equipment count and its fields values.
	 * 
	 * @param groupId
	 * @param request
	 * @return equipment details
	 * @throws ValidationException
	 *             if field values are not corresponding to field's data type or
	 *             if we are reducing the count and equipments are mapped with
	 *             any arm, location and lane
	 * @throws NotFoundException
	 *             if group id not found
	 */
	public EquipmentResponse updateByGroupId(String groupId, EquipmentRequest request);

	/**
	 * this method used to delete equipment by group. it throws exception if
	 * group id not found or equipment mapped with arm or lane or location.
	 * 
	 * @param groupId
	 * @return {@link SuccessResponse} - if successfully deleted
	 * @throws NotFoundException
	 *             if group id not found
	 * @throws ValidationException
	 *             if equipment mapped with any arm or lane or location
	 */
	public SuccessResponse delete(String groupId);

}
