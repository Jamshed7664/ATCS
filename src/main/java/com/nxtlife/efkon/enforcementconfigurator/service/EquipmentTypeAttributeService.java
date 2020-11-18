package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;

public interface EquipmentTypeAttributeService {

	/**
	 * this method used to fetch all equipment type attributes by equipment type id
	 *
	 * @param equipmentTypeId
	 * @return list of {@link EquipmentTypeAttributeResponse}
	 * @throws NotFoundException
	 *             if equipment type not found
	 */
	public List<EquipmentTypeAttributeResponse> findAllByEquipmentTypeId(String equipmentTypeId);

	/**
	 * this method used to save equipment type attributes and update equipment type
	 * attributes
	 *
	 * @param equipmentTypeId
	 * @param list
	 *            of <tt>EquipmentTypeAttributeRequest</tt>
	 * @return list of {@link EquipmentTypeAttributeResponse}
	 * @throws ValidationException
	 *             if DataType incorrect or duplicate attributes or options attribute is
	 *             valid or not
	 * @throws NotFoundException
	 *             if equipment type not found
	 */
	public List<EquipmentTypeAttributeResponse> save(String equipmentTypeId,
			List<EquipmentTypeAttributeRequest> request);

	/**
	 * this method used to delete a attribute in equipment type attribute. It throws
	 * exception if equipment type or equipment type attribute not found or if user
	 * trying to delete fixed attribute.
	 *
	 * @param equipmentTypeId
	 * @param equipmentTypeAttributeId
	 * @return list of {@link EquipmentTypeAttributeResponse} after deleting the
	 *         element
	 * @throws NotFoundException
	 *             if equipment type or equipment type attribute not found
	 * @throws ValidationException
	 *             if user trying to delete fixed attribute
	 */
	public List<EquipmentTypeAttributeResponse> deleteByEquipmentTypeAttributeId(String equipmentTypeId,
			String equipmentTypeAttributeId);

}
