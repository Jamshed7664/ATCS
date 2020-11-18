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
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentService;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;

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
public class EquipmentController {

	@Autowired
	private EquipmentService equipmentService;

	@PostMapping(value = "equipment", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save equipment", description = "return saved equipment details for an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(description = "Equipment successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save equipment", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If equipment type id not valid or all fields values not assigned or values are according to its data type", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentResponse save(
			@Parameter(description = "Equipment detail", required = true) @Valid @RequestBody EquipmentRequest request) {
		return equipmentService.save(request);
	}

	@GetMapping(value = "equipments", produces = { "application/json" })
	@Operation(summary = "Find all the equipments", description = "return a list of equipments in an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipments", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<EquipmentResponse> findAll(@RequestParam(required = false) String equipmentTypeId) {
		if (equipmentTypeId != null) {
			return equipmentService.findByEquipmentTypeIdAndGroupByGroupId(equipmentTypeId);
		}
		return equipmentService.findAllGroupByGroupId();
	}

	@GetMapping(value = "equipments/{groupId}/equipments", produces = { "application/json" })
	@Operation(summary = "Find all the equipments related to a group", description = "return a list of equipments for equipment group id in an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment group id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<EquipmentResponse> findByGroupId(
			@Parameter(description = "Equipment group id", required = true) @PathVariable String groupId) {
		return equipmentService.findByGroupId(groupId);
	}

	@GetMapping(value = "location/{locationId}/equipments/{groupId}", produces = { "application/json" })
	@Operation(summary = "Find all the equipments related to a group", description = "return a list of equipments for equipment group id in an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment group id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<EquipmentResponse> findByLocationIdAndGroupId(
			@Parameter(description = "Location id", required = true) @PathVariable String locationId,
			@Parameter(description = "Equipment group id", required = true) @PathVariable String groupId,
			@Parameter(description = "Arm id for equipment mapped", required = false) @RequestParam(required = false) String armId,
			@Parameter(description = "Lane id for equipment mapped", required = false) @RequestParam(required = false) String laneId) {
		return equipmentService.findByLocationIdAndGroupIdAndArmIdAndLaneId(locationId, groupId, armId, laneId);
	}

	@GetMapping(value = "equipments/{groupId}", produces = { "application/json" })
	@Operation(summary = "Find equipment details related to a group", description = "return equipment details for equipment group id in an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment group id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentResponse findByGroupIdAndGroupByGroupId(
			@Parameter(description = "Equipment group id", required = true) @PathVariable String groupId) {
		return equipmentService.findByGroupIdAndGroupByGroupId(groupId);
	}

	@GetMapping(value = "equipment/{id}", produces = { "application/json" })
	@Operation(summary = "Find equipment details related to a group", description = "return equipment details for equipment group id in an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Equipment succesfully fetched", content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment types", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(responseCode = "404", description = "If equipment group id not found", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentResponse findById(
			@Parameter(description = "Equipment id", required = true) @PathVariable String id) {
		return equipmentService.findById(id);
	}

	@PutMapping(value = "equipment/{groupId}", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Update equipment by group id", description = "return updated equipment details for an organization", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(description = "Equipment successfully updated", responseCode = "200", content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update equipment", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If all fields values not assigned or values are according to its data type", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If group id of equipment not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public EquipmentResponse update(
			@Parameter(description = "Equipment detail", required = true) @PathVariable String groupId,
			@Parameter(description = "Equipment detail", required = true) @Valid @RequestBody EquipmentRequest request) {
		return equipmentService.updateByGroupId(groupId, request);
	}

	@DeleteMapping(value = "equipment/{groupId}", produces = { "application/json" })
	@Operation(summary = "Delete equipment by group id", description = "return success message after deleting a group of equipment", tags = {
			"Equipment" })
	@ApiResponses(value = {
			@ApiResponse(description = "Equipment successfully deleted", responseCode = "200", content = @Content(schema = @Schema(implementation = EquipmentResponse.class))),
			@ApiResponse(description = "If user doesn't have access to update equipment", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "if equipment mapped with any arm or lane or location", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If group id of equipment not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse delete(
			@Parameter(description = "Equipment detail", required = true) @PathVariable String groupId) {
		return equipmentService.delete(groupId);
	}

}
