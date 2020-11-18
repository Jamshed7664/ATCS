package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.VehicleTypeService;
import com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type.VehicleTypeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Vehicle Type", description = "Vehicle Type api's for fetch the vehicle types")
@RequestMapping("/api/")
public class VehicleTypeController {

	@Autowired
	private VehicleTypeService vehicleTypeService;

	@GetMapping(value = "vehicle/types", produces = { "application/json" })
	@Operation(summary = "Find all the vehicle types", description = "return a list of types of vehicle", tags = {
			"Vehicle Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Vehicle types succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = VehicleTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "User don't have access to fetch vehicle types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<VehicleTypeResponse> findAll() {
		return vehicleTypeService.findAll();
	}

}
