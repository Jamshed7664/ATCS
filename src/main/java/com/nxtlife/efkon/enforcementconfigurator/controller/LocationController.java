package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Location", description = "Location api's for fetch,save,update and delete the location")
@RequestMapping("/api/")
public class LocationController {

	@Autowired
	private LocationService locationService;

	@GetMapping(value = "location/{id}", produces = { "application/json" })
	@Operation(summary = "Find by location id", description = "return location detail by the given location id", tags = {
			"Location" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location  successfully fetched", responseCode = "200", content = @Content(schema = @Schema(implementation = LocationResponse.class))),
			@ApiResponse(description = "If user doesn't have access to fetch  location ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location id not found ", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public LocationResponse findById(@PathVariable String id) {
		return locationService.findById(id);
	}

	@GetMapping(value = "locations", produces = { "application/json" })
	@Operation(summary = "Find locations for an organization", description = "return list of locations", tags = {
			"Location" })
	@ApiResponses(value = {
			@ApiResponse(description = "Locations  successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch  location ", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LocationResponse> findAll() {
		return locationService.findAll();
	}

	@PostMapping(value = "location", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save location ", description = "return the same location after saved", tags = { "Location" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = LocationResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save the location", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If details are not filled properly and location type not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public LocationResponse save(
			@Parameter(description = "Location details", required = true) @Valid @RequestBody LocationRequest request) {
		return locationService.save(request);
	}

	@PutMapping(value = "location/{id}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Update location ", description = "return the same location after updated", tags = {
			"Location" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location successfully updated", responseCode = "200", content = @Content(schema = @Schema(implementation = LocationResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update the location", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If details are not filled properly and location type  not exist", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location id not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public LocationResponse update(@PathVariable String id, @RequestBody LocationRequest request) {
		return locationService.update(id, request);
	}

	@DeleteMapping(value = "location/{id}", produces = { "application/json" })
	@Operation(summary = "Delete location by id", description = "return success message if location successfully deleted", tags = {
			"Location" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to delete location", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(@Parameter(description = "Location id", required = true) @PathVariable String id) {
		return locationService.delete(id);

	}
}
