package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import javax.validation.Valid;

import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.CameraIncidentService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Camera Incident", description = "Camera incident api's for fetch and save the incidents for the camera")
@RequestMapping("/api/location/{locationId}/")
public class CameraIncidentController {

	@Autowired
	private CameraIncidentService cameraIncidentService;

	@PostMapping(value = "camera/{cameraId}/incidents", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "Save camera incidents", description = "return success response if camera incidents are successfully saved", tags = {
			"Camera", "Camera Incident" })
	@ApiResponses(value = {
			@ApiResponse(description = " Camera incidents successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save camera incidents", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If equipment or camera not found or camera not mapped with location", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If incident type not found or attribute value not according to requirement", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse save(
			@Parameter(description = "location Id", required = true) @PathVariable String locationId,
			@Parameter(description = "Camera Id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Camera incidents details", required = true) @Valid @RequestBody CameraIncidentRequest request) {
		return cameraIncidentService.saveCameraIncidents(locationId, cameraId, request);
	}

	@GetMapping(value = "camera/{cameraId}/incidents", produces = { "application/json" })
	@Operation(summary = "Find camera incidents details by camera id and incident type", description = "return camera incident details for given camera id and incident type", tags = {
			"Camera", "Camera Incident" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Camera Incidents successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CameraIncidentResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch camera incident details", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera id not found or camera not mapped with location or incident type not found ", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<CameraIncidentResponse> findByCameraIdAndIncidentType(
			@Parameter(description = "location Id", required = true) @PathVariable String locationId,
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId,
			@Parameter(description = "Incident type", required = true) @RequestParam(name = "incidentType", required = true) String incidentType) {
		return cameraIncidentService.findByCameraId(locationId, cameraId, incidentType);
	}

	@GetMapping(value = "camera/{cameraId}/incident-cameras", produces = { "application/json" })
	@Operation(summary = "Find define incident camera  names by other camera id ", description = "return list of camera names for given camera id ", tags = {
			"Camera", "Camera Incident" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Camera  Names successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CameraResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch camera  names", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera id not found or camera not mapped with location", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<CameraResponse> findCameraNamesByCameraId(
			@Parameter(description = "location Id", required = true) @PathVariable String locationId,
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId) {
		return cameraIncidentService.findCameraNamesByCameraId(locationId, cameraId);
	}

	@GetMapping(value = "camera/{cameraId}/incident/names", produces = { "application/json" })
	@Operation(summary = "Find camera incidents names by camera id ", description = "return list of incident names for given camera id ", tags = {
			"Camera", "Camera Incident" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Camera Incident Names successfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch camera incident names", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If camera id not found or camera not mapped with location", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<String> findIncidentNamesByCameraId(
			@Parameter(description = "location Id", required = true) @PathVariable String locationId,
			@Parameter(description = "Camera id", required = true) @PathVariable String cameraId) {
		return cameraIncidentService.findIncidentNamesByCameraId(locationId, cameraId);
	}

}
