package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;

public interface CameraIncidentService {

	/**
	 * this method used to save or update incidents for a particular camera
	 *
	 * @param locationId
	 * @param cameraId
	 * @param request
	 * @return {@link SuccessResponse}
	 * @throws NotFoundException   if camera id not found in location id
	 * @throws ValidationException if incident type not valid or two incident names
	 *                             are same or attribute values not filled properly
	 *                             or attribute not exist or value not according to
	 *                             data type
	 */
	public SuccessResponse saveCameraIncidents(String locationId, String cameraId, CameraIncidentRequest request);

	/**
	 * this method used to get incident details for particular camera using camera
	 * id and incident type
	 *
	 * @param cameraId
	 * @return list of {@link CameraIncidentResponse}
	 * @throws NotFoundException if camera id not found in given location id or
	 *                           camera not mapped with any location or incident
	 *                           type not found
	 */
	public List<CameraIncidentResponse> findByCameraId(String locationId, String cameraId, String incidentType);

	/**
	 * this method used to get incident names for particular camera using camera id
	 *
	 * @param cameraId
	 * @return list of String
	 * @throws NotFoundException if camera id not found in given location id
	 */
	public List<String> findIncidentNamesByCameraId(String locationId, String cameraId);

	/**
	 * this method used to get camera names that defined their incidents using other
	 * camera id
	 *
	 * @param cameraId
	 * @return list of String
	 * @throws NotFoundException if camera id not found in given location id or
	 *                           camera not mapped with locationo
	 */
	public List<CameraResponse> findCameraNamesByCameraId(String locationId, String cameraId);
}
