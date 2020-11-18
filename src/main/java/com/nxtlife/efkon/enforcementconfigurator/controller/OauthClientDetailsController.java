package com.nxtlife.efkon.enforcementconfigurator.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nxtlife.efkon.enforcementconfigurator.dao.jpa.OauthClientDetailsJpaDao;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@Tag(name = "OauthClientDetails", description = "OauthClientDetails api")
public class OauthClientDetailsController {

	@Autowired
	private OauthClientDetailsJpaDao oauthClientDetailsDao;

	@GetMapping("/clients")
	@ApiResponse(description = "fetch oauth client ids", responseCode = "200", content = @Content(array = @ArraySchema(schema = @Schema(implementation = String.class))))
	public List<String> fetchOauthClientIds() {
		return oauthClientDetailsDao.findClientIds();
	}

}
