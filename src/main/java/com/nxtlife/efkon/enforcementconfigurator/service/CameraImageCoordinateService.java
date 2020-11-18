package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentResponse;

public interface CameraImageCoordinateService {

	/**
	 * this method used to save or update image coordinates such as red_lamp,main or
	 * road alignment using camera image id
	 *
	 * @param cameraImageId
	 * @param request
	 * @return {@Link SuccessResponse}
	 * @throws NotFoundException   if camera image id not found
	 * @throws ValidationException if camera not mapped with any location or if
	 *                             camera not mapped with any arm or lane or red
	 *                             lamp coordinates for highway or lanes or Road
	 *                             alignment coordinates without road alignment
	 *                             properties or road alignment outside the main
	 *                             detector
	 *
	 *
	 */
	public SuccessResponse saveImageCoordinates(String cameraImageId, CameraImageCoordinateListRequest request);

	/**
	 * this method used to save or update zone coordinate and map with lane using
	 * camera image id
	 *
	 * @param cameraImageId
	 * @param request
	 * @return {@Link ImageCoordinateResponse}
	 * @throws NotFoundException   if camera image id or lane id not found
	 * @throws ValidationException if camera image coordinate id not exist ->in
	 *                             update if main detector coordinate not exist if
	 *                             zone coordinates not under the main detector
	 *                             coordinates if zone coordinate is same as main
	 *                             coordinate or lane already mapped or same lane
	 *                             mapped to different zones
	 */
	public CameraImageCoordinateResponse saveZoneCoordinates(String cameraImageId,
			CameraImageCoordinateRequest request);

	/**
	 * this method used to fetch zone coordinates details by camera image id
	 *
	 * @param cameraImageId
	 * @return <tt>ImageCoordinateResponse</tt>
	 * @throws NotFoundException if camera image not found
	 */
	public List<CameraImageCoordinateResponse> findZoneCoordinates(String cameraImageId);

	/**
	 * this method used to save or update zone incidents details by zone id
	 *
	 * @param cameraImageId
	 * @param zoneId
	 * @param request
	 * @return {@Link SuccessResponse}
	 * @throws NotFoundException   if camera image not found or zone not found not
	 *                             found
	 * @throws ValidationException if scheduling is on for that camera or some
	 *                             incidents names not exist
	 *
	 */
	public SuccessResponse saveZoneIncidents(String cameraImageId, Long zoneId, ZoneIncidentRequest request);

	/**
	 * this method used to fetch zone incidents by using zone id
	 *
	 * @param cameraImageId
	 * @param zoneId
	 * @return list of {@Link ZoneIncidentResponse}
	 * @throws NotFoundException   if camera image not found or zone not found
	 * @throws ValidationException if scheduling is on for that camera
	 */
	public List<ZoneIncidentResponse> findAllIncidentsByZoneId(String cameraImageId, Long zoneId);

	/**
	 * this method used to fetch camera image coordinates details by camera image id
	 *
	 * @param cameraImageId
	 * @return {@Link CameraImageCoordinateListResponse}
	 * @throws NotFoundException if camera image not found
	 */

	public CameraImageCoordinateListResponse findCameraImageCoordinates(String cameraImageId);

	/**
	 * this method used to delete image coordinate. It throws exception if camera
	 * image id not found or lane is mapped with it
	 *
	 * @param id
	 * @return {@link SuccessResponse} if image coordinate deleted successfully
	 * @throws NotFoundException   if camera image not found
	 * @throws ValidationException if lane is mapped with image coordinate->in case
	 *                             of zone
	 */
	public SuccessResponse delete(Long id, String cameraImageId);
}
