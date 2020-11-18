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
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationTypeAttributeService;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeAttributeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "LocationType", description = "Location type attributes api's for fetch, create, delete")
@RequestMapping("/api/")
public class LocationTypeAttributeController {

	@Autowired
	private LocationTypeAttributeService locationTypeAttributeService;

	/**
	 * return a list of location type attributes from the given location type id.
	 *
	 * @return List of <tt>LocationTypeAttributeResponse</tt>
	 * @throws NotFoundException
	 *             if location type not found
	 * @Param locationTypeId
	 */

	@GetMapping(value = "location/type/{locationTypeId}/attributes", produces = { "application/json" })
	@Operation(summary = "Find all the location type attributes by location type id", description = "return a list of location type attributes from given location type id", tags = {
			"Location", "Location Type", "Location Type Attribute" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Location type attributes succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationTypeAttributeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch location type attributes", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "Location type not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LocationTypeAttributeResponse> findAllByLocationTypeId(@PathVariable String locationTypeId) {
		return locationTypeAttributeService.findAllByLocationTypeId(locationTypeId);
	}

	@PostMapping(produces = { "application/json" }, consumes = {
			"application/json" }, value = "location/type/{locationTypeId}/attributes")
	@Operation(summary = "Save location type attributes", description = "return list of location type attributes for an organization", tags = {
			"Location", "LocationType", "LocationTypeAttribute" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location type attributes successfully saved", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationTypeAttributeResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to save list of location type attributes", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location type id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LocationTypeAttributeResponse> save(
			@Parameter(description = "Location type id for which attributes to be update", required = true) @PathVariable String locationTypeId,
			@Parameter(description = "Location type attributes", required = true) @Valid @RequestBody LocationTypeAttributeListRequest request) {
		return locationTypeAttributeService.save(locationTypeId, request.getList());
	}

	@DeleteMapping(produces = {
			"application/json" }, value = "location/type/{locationTypeId}/attribute/{locationTypeAttributeId}")
	@Operation(summary = "Delete location type attribute", description = "return list of location type attributes for an organization after delete location type attribute", tags = {
			"Location", "LocationType", "LocationTypeAttribute" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location type attributes successfully deleted", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationTypeAttributeResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to delete location type attribute", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location type id or location type attribute id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LocationTypeAttributeResponse> delete(
			@Parameter(description = "Location type id for which attributes to be update", required = true) @PathVariable String locationTypeId,
			@Parameter(description = "Location type attribute id", required = true) @PathVariable String locationTypeAttributeId) {
		return locationTypeAttributeService.deleteByLocationTypeAttributeId(locationTypeId, locationTypeAttributeId);
	}

}
