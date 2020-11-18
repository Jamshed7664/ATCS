package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


public class CameraIncidentAttributeValueRequest implements Request {

    @Schema(description = "Id of the incident attribute", example = "1")
    @NotNull(message = "Attribute Id can't be null")
    private Long attributeId;

    @Schema(description = "Value of the incident attribute", example = "23")
    @NotEmpty(message = "Attribute Value can't be null or empty")
    private String value;

    public Long getAttributeId() {
        return unmask(attributeId);
    }

    public String getValue() {
        return value;
    }
}
