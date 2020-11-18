package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import org.springframework.security.access.AccessDeniedException;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

public interface LocationService {

	/**
	 * this method used to fetch location by location id
	 *
	 * @param
	 * @param id
	 * @return {@link LocationResponse}
	 * @throws NotFoundException
	 *             if location id not found
	 */
	public LocationResponse findById(String id);

	/**
	 * this method used to fetch locations for an organization
	 * 
	 * @return list of {@link LocationResponse}
	 */
	public List<LocationResponse> findAll();

	/**
	 * this method used to save location such as junction,point etc
	 *
	 * @param
	 * @param request
	 *            of LocationRequest
	 * @return {@link LocationResponse}
	 * @throws ValidationException
	 *             if location type id not found or location details are not
	 *             filled properly if name or(latitude and longitude) is already
	 *             exist if number of arms are not according to requirement if
	 *             location type field not exist or field values not according
	 *             to requirement if vehicle type not found if directions are
	 *             not found
	 * @throws AccessDeniedException
	 *             if user is try to access different organization
	 */
	public LocationResponse save(LocationRequest request);

	/**
	 * this method used to update location by given location id lane can be
	 * delete from last only.
	 *
	 * @param id
	 * @param request
	 *            of LocationRequest
	 * @return {@link LocationResponse}
	 * @throws NotFoundException
	 *             if location id not found
	 * @throws ValidationException
	 *             if name or latitude and longitude already exist if location
	 *             type id not found and location details are not filled
	 *             properly if location type field not found or field values are
	 *             not according to requirement if vehicle type not found if
	 *             directions are not found if arm not found or lane not found
	 *             which is to be updated
	 *
	 */
	public LocationResponse update(String id, LocationRequest request);

	/**
	 * this method used to delete location. It throws exception if location id
	 * not found
	 * 
	 * @throws NotFoundException
	 *             if location id not found
	 *
	 * @throws ValidationException
	 *             if equipment is mapped with this location
	 *
	 * @param id
	 * @return {@link SuccessResponse} if location deleted successfully
	 */
	public SuccessResponse delete(String id);
}
