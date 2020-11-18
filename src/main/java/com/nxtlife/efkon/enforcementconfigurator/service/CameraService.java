package com.nxtlife.efkon.enforcementconfigurator.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.ex.ValidationException;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

public interface CameraService {

	/**
	 * this method used to save or update camera detail using equipment id
	 * 
	 * @param equipmentId
	 * @param request
	 * @return {@link CameraResponse}
	 * @throws NotFoundException   if equipment id not found
	 * @throws ValidationException if camera type not valid or equipment is not
	 *                             camera
	 */
	public CameraResponse save(String equipmentId, CameraRequest request);

	/**
	 * this method used to fetch camera details for equipment id.
	 * 
	 * @param equipmentId
	 * @return {@link CameraResponse}
	 * @throws NotFoundException if equipment id not found
	 */
	public CameraResponse findByEquipmentId(String equipmentId);

	/**
	 * this method used to fetch lanes details which is mapped to camera.
	 *
	 * @param cameraId
	 * @return list of {@link LaneResponse}
	 * @throws NotFoundException if camera id not found
	 */
	public List<LaneResponse> findByCameraId(String cameraId);

	/**
	 * this method used to upload image for a camera
	 * 
	 * @param id
	 * @param image
	 * @return {@link SuccessResponse} - if successfully saved
	 * @throws NotFoundException if id is not valid
	 */
	public SuccessResponse uploadImage(String id, MultipartFile image);

	/**
	 * this method used to fetch image for a camera
	 * 
	 * @param id
	 * @return {@link CameraImageResponse} - if successfully saved
	 * @throws NotFoundException if id is not valid
	 */
	public CameraImageResponse fetchImage(String id);

	/**
	 * this method used to delete image for a camera by id
	 *
	 * @param cameraId
	 * @param id
	 * @return {@link SuccessResponse} - if image successfully deleted
	 * @throws NotFoundException   if cameraId or image id not valid
	 * @throws ValidationException if camera image having coordinates which is
	 *                             mapped with lanes
	 */
	public SuccessResponse delete(String id, String cameraId);

}
