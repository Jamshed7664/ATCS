package com.nxtlife.efkon.enforcementconfigurator.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.IncidentService;
import com.nxtlife.efkon.enforcementconfigurator.view.ListRequest;
import com.nxtlife.efkon.enforcementconfigurator.view.SuccessResponse;
import com.nxtlife.efkon.enforcementconfigurator.view.incident.IncidentRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Incident", description = "Incident api's for fetch and save the incidents")
@RequestMapping("/api/")
public class IncidentController {

	@Autowired
	private IncidentService incidentService;

	@PostMapping(value = "incidents", produces = { "application/json" }, consumes = { "application/json" })
	@Operation(summary = "Save incidents", description = "return success response if incidents are successfully saved", tags = {
			"Incident" })
	@ApiResponses(value = {
			@ApiResponse(description = "Incidents successfully saved", responseCode = "200", content = @Content(schema = @Schema(implementation = SuccessResponse.class))),
			@ApiResponse(description = "If user doesn't have access to save list of incident", responseCode = "403", content = @Content(schema = @Schema(implementation = ApiError.class))),
			@ApiResponse(description = "If incident type not found or data type of attribute not found", responseCode = "400", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public SuccessResponse save(
			@Parameter(description = "Incidents details", required = true) @Valid @RequestBody ListRequest<IncidentRequest> request) {
		return incidentService.save(request.getList());
	}

}
