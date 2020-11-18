package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.LocationTypeService;
import com.nxtlife.efkon.enforcementconfigurator.view.location.type.LocationTypeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Location", description = "Location type api's for fetch, create, delete")
@RequestMapping("/api/")
public class LocationTypeController {

	@Autowired
	private LocationTypeService locationTypeService;

	@GetMapping(produces = { "application/json" }, value = "location/types")
	@Operation(summary = "Find all location types", description = "return list of location types for an organization", tags = {
			"Location", "LocationType" })
	@ApiResponses(value = {
			@ApiResponse(description = "Location types successfully fetched", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = LocationTypeResponse.class)))),
			@ApiResponse(description = "If user doesn't have access to fetch list of location types", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<LocationTypeResponse> findAll() {
		return locationTypeService.findAll();
	}

}
