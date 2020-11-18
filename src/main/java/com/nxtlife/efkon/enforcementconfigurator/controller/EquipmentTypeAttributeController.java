package com.nxtlife.efkon.enforcementconfigurator.controller;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.EquipmentTypeAttributeService;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.equipment.type.EquipmentTypeAttributeResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "EquipmentType", description = "Equipment type attribute api's for fetch,save ,update and delete the equipment type attributes")
@RequestMapping("/api/")
public class EquipmentTypeAttributeController {

    @Autowired
    private EquipmentTypeAttributeService equipmentTypeAttributeService;

    /**
     * return a list of equipment type attributes from the given equipment type id.
     *
     * @return List of <tt>EquipmentTypeAttributeResponse</tt>
     * @throws NotFoundException if equipment type not found
     * @Param equipmentTypeId
     */

    @GetMapping(value = "equipment/type/{equipmentTypeId}/attributes", produces = {"application/json"})
    @Operation(summary = "Find all the equipment type attributes by equipment type id", description = "return a list of equipment type attributes from given equipment type id", tags = {
            "Equipment", "Equipment Type", "Equipment Type Attribute"})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Equipment type attributes succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentTypeAttributeResponse.class)))),
            @ApiResponse(responseCode = "403", description = "user don't have access to fetch equipment type attributes", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Equipment type not found", content = @Content(schema = @Schema(implementation = ApiError.class)))})
    public List<EquipmentTypeAttributeResponse> findAllByEquipmentId(@PathVariable String equipmentTypeId) {
        return equipmentTypeAttributeService.findAllByEquipmentTypeId(equipmentTypeId);
    }

    /**
     * save and update the equipment type attributes and
     * return a list of equipment type attributes which is saved or updated.
     *
     * @return List of <tt>EquipmentTypeAttributeResponse</tt>
     * @throws NotFoundException if equipment type not found
     * @Param equipmentTypeId
     * @Param request
     */

    @PostMapping(value = "equipment/type/{equipmentTypeId}/attributes",
            produces = {"application/json"}, consumes = {"application/json"})
    @Operation(summary = "Save equipment type attributes", description = "return list of equipment type attributes for an organization", tags = {
            "Equipment", "Equipment Type", "Equipment Type Attribute"})
    @ApiResponses(value = {
            @ApiResponse(description = "Equipment type attributes successfully saved", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentTypeAttributeResponse.class)))),
            @ApiResponse(description = "If user doesn't have access to save list of equipment type attributes", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(description = "If equipment type id not found", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class)))})
    public List<EquipmentTypeAttributeResponse> save(
            @Parameter(description = "Equipment type id for which attributes to be update", required = true) @PathVariable String equipmentTypeId,
            @Parameter(description = "Equipment type attributes", required = true) @Valid @RequestBody EquipmentTypeAttributeListRequest request) {
        return equipmentTypeAttributeService.save(equipmentTypeId, request.getList());
    }

    /**
     * Delete the attribute of  equipment type attribute and
     * return a list of equipment type attributes after deleted.
     *
     * @param equipmentTypeAttributeId
     * @return List of <tt>EquipmentTypeAttributeResponse</tt>
     * @throws NotFoundException   if equipment type and equipment type attribute not found
     * @throws ValidationException if user want to delete the mandatory attribute
     * @Param equipmentTypeId
     */

    @DeleteMapping(produces = {
            "application/json"}, value = "equipment/type/{equipmentTypeId}/attribute/{equipmentTypeAttributeId}")
    @Operation(summary = "Delete equipment type attribute", description = "return list of equipment type attributes for an organization after delete equipment type attribute", tags = {
            "Equipment", "Equipment Type", "Equipment Type Attribute"})
    @ApiResponses(value = {
            @ApiResponse(description = "Equipment type attributes successfully deleted", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = EquipmentTypeAttributeResponse.class)))),
            @ApiResponse(description = "If user doesn't have access to delete equipment type attribute", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(description = "If equipment type id or equipment type attribute id incorrect", responseCode = "404", content = @Content(schema = @Schema(implementation = ApiError.class)))})
    public List<EquipmentTypeAttributeResponse> delete(
            @Parameter(description = "Equipment type id for which attributes to be update", required = true) @PathVariable String equipmentTypeId,
            @Parameter(description = "Equipment type attribute id", required = true) @PathVariable String equipmentTypeAttributeId) {
        return equipmentTypeAttributeService.deleteByEquipmentTypeAttributeId(equipmentTypeId, equipmentTypeAttributeId);
    }


}
