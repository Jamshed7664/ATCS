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
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraImageCoordinateService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateListResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraImageCoordinateResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.ZoneIncidentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Camera Image Coordinate", description = "Camera image coordinate api's to save,update,fetch and delete the image coordinates")
@RequestMapping("/api/camera/{cameraId}/image/{imageId}/")
public class CameraImageCoordinateController {

	@Autowired
	private CameraImageCoordinateService cameraImageCoordinateService;

	@PostMapping(value = "coordinates", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save image coordinates", description = "return Success response after saved the list of image coordinates", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate" })
	@ApiResponses(value = {
			@ApiResponse(description = "Image coordinates successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = CameraImageCoordinateResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save image coordinates", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera image id not valid", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse saveImageCoordinates(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId,
			@Parameter(description = "Image coordinates details", required = true) @Valid @RequestBody CameraImageCoordinateListRequest request) {
		return cameraImageCoordinateService.saveImageCoordinates(imageId, request);
	}

	@GetMapping(value = "coordinates", produces = { "application/json" })
	@Operation(summary = "Find camera image coordinates by camera image id", description = "return list of camera image coordinate response for the given camera image id", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "camera image coordinates successfully fetched", content = @Content(schema = @Schema(implementation = CameraImageCoordinateListResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch camera image coordinates", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera image id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public CameraImageCoordinateListResponse findCameraImageCoordinatesByCameraImageId(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId) {
		return cameraImageCoordinateService.findCameraImageCoordinates(imageId);
	}

	@PostMapping(value = "coordinates/zone", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save and update zone coordinates", description = "return zone coordinate response after saved the zone coordinates", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate" })
	@ApiResponses(value = {
			@ApiResponse(description = "Zone coordinates successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = CameraImageCoordinateResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save zone coordinates", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera image id or lane id not valid", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public CameraImageCoordinateResponse saveZoneCoordinates(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId,
			@Parameter(description = "zone coordinates details", required = true) @Valid @RequestBody CameraImageCoordinateRequest request) {
		return cameraImageCoordinateService.saveZoneCoordinates(imageId, request);
	}

	@GetMapping(value = "coordinates/zones", produces = { "application/json" })
	@Operation(summary = "Find all zones coordinates by camera image id", description = "return list of zone coordinates for the given camera image id", tags = {
			"Camera", "Camera Image", "Camera Image Id" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "zones coordinates successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CameraImageCoordinateResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch zones coordinates", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera image id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<CameraImageCoordinateResponse> findZoneCoordinatesByCameraImageId(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId) {
		return cameraImageCoordinateService.findZoneCoordinates(imageId);
	}

	@PostMapping(value = "zone/{zoneId}/incidents", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "Save and update zone incidents", description = "return success response after saved the zone incidents", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate", "Incident" })
	@ApiResponses(value = {
			@ApiResponse(description = "Zone incidents successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save zone incidents", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera image id not valid or zone id not exist", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse saveZoneIncidents(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId,
			@Parameter(description = "zone id", required = true) @PathVariable Long zoneId,
			@Parameter(description = "zone incident details", required = true) @Valid @RequestBody ZoneIncidentRequest request) {
		return cameraImageCoordinateService.saveZoneIncidents(imageId, zoneId, request);
	}

	@GetMapping(value = "zone/{zoneId}/incidents", produces = { "application/json" })
	@Operation(summary = "fetch zone incidents", description = "return list of zone incidents by the given zone id", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate", "Incident" })
	@ApiResponses(value = {
			@ApiResponse(description = "Zone incidents successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ZoneIncidentResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch zone incidents", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If camera image id not valid or zone id not exist", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<ZoneIncidentResponse> findAllIncidentsByZoneId(
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId,
			@Parameter(description = "zone id", required = true) @PathVariable Long zoneId) {
		return cameraImageCoordinateService.findAllIncidentsByZoneId(imageId, zoneId);
	}

	@DeleteMapping(value = "coordinate/{id}", produces = { "application/json" })
	@Operation(summary = "Delete image coordinate by id", description = "return success message if image coordinates successfully deleted", tags = {
			"Camera", "Camera Image", "Camera Image Coordinate" })
	@ApiResponses(value = {
			@ApiResponse(description = "Image coordinate successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete image coordinate", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If image coordinate id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera image id", required = true) @PathVariable String imageId,
			@Parameter(description = "Image coordinate id", required = true) @PathVariable Long id) {
		return cameraImageCoordinateService.delete(id, imageId);
	}
}
