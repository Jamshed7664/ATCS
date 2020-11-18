package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.ex.ApiError;
import com.nxtlife.efkon.enforcementconfigurator.service.DirectionService;
import com.nxtlife.efkon.enforcementconfigurator.view.direction.DirectionResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "Direction", description = "Direction api's for fetch the directions")
@RequestMapping("/api/")
public class DirectionController {

	@Autowired
	private DirectionService directionService;

	@GetMapping(value = "directions", produces = { "application/json" })
	@Operation(summary = "Find all the directions", description = "return a list of direction", tags = { "Direction" })
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Directions succesfully fetched", content = @Content(array = @ArraySchema(schema = @Schema(implementation = DirectionResponse.class)))),
			@ApiResponse(responseCode = "403", description = "user don't have access to fetch directions", content = @Content(schema = @Schema(implementation = ApiError.class))) })
	public List<DirectionResponse> findAll() {
		return directionService.findAll();
	}
}
