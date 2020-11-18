package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Camera", description = "Camera api's for fetch and update")
@RequestMapping("/api/")
public class CameraController {

	@Autowired
	private CameraService cameraService;

	@GetMapping(value = "camera/{equipmentId}", produces = { "application/json" })
	@Operation(summary = "Find camera details related to a equipment id", description = "return camera details for equipment id in an organization", tags = {
			"Camera" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Camera succesfully fetched", content = @Content(schema = @Schema(implementation = CameraResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch camera details", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public CameraResponse findByEquipmentId(
			@Parameter(description = "Equipment id", required = true) @PathVariable String equipmentId) {
		return cameraService.findByEquipmentId(equipmentId);
	}

	@PostMapping(value = "camera/{equipmentId}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save camera details", description = "return saved camera details for an organization and equipment", tags = {
			"Camera" })
	@ApiResponses(value = {
			@ApiResponse(description = "Camera successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = CameraResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save camera", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera type not valid or equipment is not camera", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public CameraResponse save(
			@Parameter(description = "Equipment id", required = true) @PathVariable String equipmentId,
			@Parameter(description = "Camera detail", required = true) @Valid @RequestBody CameraRequest request) {
		return cameraService.save(equipmentId, request);
	}

	@GetMapping(value = "camera/{cameraId}/lanes", produces = { "application/json" })
	@Operation(summary = "Find lanes details mapped with camera", description = "return lane details for camera id in an organization", tags = {
			"Camera" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "lanes succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LaneResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch lanes details", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LaneResponse> findByCameraId(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId) {
		return cameraService.findByCameraId(cameraId);
	}

	@PostMapping(value = "camera/{cameraId}/image", consumes = { "multipart/form-data" }, produces = {
			"application/json" })
	@Operation(summary = "Save camera image", description = "return success response if camera image is uploaded successfully", tags = {
			"Camera", "Camera Image" })
	@ApiResponses(value = {
			@ApiResponse(description = "Camera image successfully uploaded", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to upload camera image", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera id not valid or equipment is not active", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse uploadImage(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera Image", required = true) @RequestParam(name = "image", required = true) MultipartFile image) {
		return cameraService.uploadImage(cameraId, image);
	}

	@GetMapping(value = "camera/{cameraId}/image")
	@Operation(summary = "Fetch camera image", description = "return camera image", tags = { "Camera", "Camera Image" })
	@ApiResponses(value = {
			@ApiResponse(description = "Camera image successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = CameraImageResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch camera image", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera id not valid", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public CameraImageResponse fetchImage(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId) {
		CameraImageResponse cameraImage = cameraService.fetchImage(cameraId);
		return cameraImage;
	}

	@DeleteMapping(value = "camera/{cameraId}/image/{id}", produces = { "application/json" })
	@Operation(summary = "Delete camera image  by id", description = "return success message if image  successfully deleted", tags = {
			"Camera", "Camera Image" })
	@ApiResponses(value = {
			@ApiResponse(description = "Image  successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete camera image ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera id or image id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String id) {
		return cameraService.delete(id, cameraId);

	}
}
