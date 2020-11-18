package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeResponse;

public interface LocationTypeAttributeService {

	/**
	 * this method used to fetch all location type attributes by location type id
	 *
	 * @param locationTypeId
	 * @return list of {@link LocationTypeAttributeResponse}
	 * @throws NotFoundException
	 *             if location type not found
	 */
	public List<LocationTypeAttributeResponse> findAllByLocationTypeId(String locationTypeId);

	/**
	 * this method used to save location type attributes and update location type
	 * attributes
	 * 
	 * @param locationTypeId
	 * @param list
	 *            of <tt>LocationTypeAttributeRequest</tt>
	 * @return list of {@link LocationTypeAttributeResponse}
	 * @throws ValidationException
	 *             if DataType incorrect or duplicate attributes or options attribute is
	 *             valid or not
	 * @throws NotFoundException
	 *             if location type not found
	 */
	public List<LocationTypeAttributeResponse> save(String locationTypeId, List<LocationTypeAttributeRequest> request);

	/**
	 * this method used to delete a attribute in location type attribute. It throws
	 * exception if location type or location type attribute not found or if user
	 * trying to delete fixed attribute.
	 * 
	 * @param locationTypeId
	 * @param locationTypeAttributeId
	 * @return list of {@link LocationTypeAttributeResponse} after deleting the
	 *         element
	 * @throws NotFoundException
	 *             if location type or location type attribute not found
	 * @throws ValidationException
	 *             if user trying to delete fixed attribute
	 */
	public List<LocationTypeAttributeResponse> deleteByLocationTypeAttributeId(String locationTypeId,
			String locationTypeAttributeId);

}
