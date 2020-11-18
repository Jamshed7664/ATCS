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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentTypeService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Equipment", description = "Equipment Type api's for fetch all the equipment's types")
@RequestMapping("/api/")
public class EquipmentTypeController {

	@Autowired
	private EquipmentTypeService equipmentTypeService;

	@PostMapping(value = "equipment/type", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "save equipment type", description = "return equipment type if successfully saved in an organization", tags = {
			"Equipment", "Equipment Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment type save successfully", content = @Content(schema = @Schema(implementation = EquipmentTypeResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to save equipment type", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "400", description = "If equipment type code already exists", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentTypeResponse save(
			@Parameter(description = "Equipment type request", required = true) @Valid @RequestBody EquipmentTypeRequest equipmentType) {
		return equipmentTypeService.save(equipmentType);
	}

	/**
	 * return a list of equipment types available if equipment types not exist ,it
	 * will return empty list
	 *
	 * @return List of <tt>EquipmentTypeResponse</tt>
	 */
	@GetMapping(value = "equipment/types", produces = { "application/json" })
	@Operation(summary = "Find all the equipment types", description = "return a list of equipment types in an organization", tags = {
			"Equipment", "Equipment Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment types succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<EquipmentTypeResponse> findAll() {
		return equipmentTypeService.findAll();
	}

	@GetMapping(value = "equipment/types-count", produces = { "application/json" })
	@Operation(summary = "Find all the equipment types with total count and available count", description = "return a list of equipment types with total count and available count in an organization", tags = {
			"Equipment", "Equipment Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment types succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentTypeResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<EquipmentTypeResponse> findAllWithTotalCountAndAvailableCount() {
		return equipmentTypeService.findAllWithItsTotalCountAndAvailableCount();
	}

	@PutMapping(value = "equipment/type/{id}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Update equipment type quantity", description = "return equipment type if successfully updated in an organization", tags = {
			"Equipment", "Equipment Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment type save successfully", content = @Content(schema = @Schema(implementation = EquipmentTypeResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to save equipment type", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment type not exists", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentTypeResponse save(
			@Parameter(description = "Equipment type id", required = true) @PathVariable String id,
			@Parameter(description = "Equipment type request", required = true) @RequestParam Integer quantity) {
		return equipmentTypeService.updateQuantity(id, quantity);
	}

	@DeleteMapping(value = "equipment/type/{id}", consumes = { "application/json" })
	@Operation(summary = "Delete equipment type", description = "return success message if successfully deleted in an organization", tags = {
			"Equipment", "Equipment Type" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment type deleted successfully", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to save equipment type", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment type not exists", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse save(
			@Parameter(description = "Equipment type id", required = true) @PathVariable String id) {
		return equipmentTypeService.delete(id);
	}
}
