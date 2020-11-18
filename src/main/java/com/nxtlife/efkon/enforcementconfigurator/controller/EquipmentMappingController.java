package com.nxtlife.efkon.enforcementconfigurator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.ex.NotFoundException;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentMappingService;
import com.nxtlife.efkon.enforcementconfigurator.view.ListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentLocationMappingResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentMappingRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Equipment Mapping", description = "Equipment mapping api's for mapped the equipment to location")
@RequestMapping("/api/")
public class EquipmentMappingController {

	@Autowired
	private EquipmentMappingService equipmentMappingService;

	/**
	 * This method used to map equipment with the location location can be
	 * junction,arm,lane,highway etc..
	 *
	 * @return {@Link SuccessResponse} if equipment successfully maped with
	 *         location
	 * @throws NotFoundException
	 *             if location id not found
	 * @Param locationId
	 */

	@PostMapping(value = "location/{locationId}/equipment", produces = { "application/json" }, consumes = {
			"application/json" })
	@Operation(summary = "Save equipment mapping with location", description = "return successful response if saved successfully", tags = {
			"Equipment Mapping" })
	@ApiResponses(value = {
			@ApiResponse(description = "Equipment successfully Mapped with location", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save map equipment", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If arm id or lane id or group id or equipment ids are not valid", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If location id not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse save(@PathVariable String locationId,
			@Parameter(description = "Equipment mapping details", required = true) @Valid @RequestBody ListRequest<EquipmentMappingRequest> request) {
		return equipmentMappingService.saveEquipmentMappingForLocation(locationId, request.getList());
	}

	@GetMapping(value = "location/{locationId}/equipment-mapping", produces = { "application/json" })
	@Operation(summary = "Find all available equipments with location mapped count", description = "return a list of available equipments with count in an organization", tags = {
			"Equipment Mapping", "Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment mapping succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentLocationMappingResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipments", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If location id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentLocationMappingResponse findByLocationIdAndGroupByGroupId(
			@Parameter(description = "Location id", required = true) @PathVariable String locationId) {
		return equipmentMappingService.findEquipmentMappingByLocationId(locationId);
	}

}
