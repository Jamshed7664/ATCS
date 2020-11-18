package com.nxtlife.efkon.enforcementconfigurator.view.incident;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.view.camera.CameraIncidentAttributeValueResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class IncidentResponse {

    @Schema(description = "Name of the incident", example = "Over Speeding")
    private String name;

    @Schema(description = "Incident attributes")
    private List<IncidentAttributeResponse> attributes;

    @Schema(description = "Incident attributes value details")
    private List<CameraIncidentAttributeValueResponse> attributeValues;


    public IncidentResponse() {
        super();
    }

    public IncidentResponse(String name) {super();
        this.name = name;
    }

    public IncidentResponse(String name, List<IncidentAttributeResponse> attributes) {
        super();
        this.name = name;
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<IncidentAttributeResponse> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<IncidentAttributeResponse> attributes) {
        this.attributes = attributes;
    }

    public List<CameraIncidentAttributeValueResponse> getAttributeValues() {
        return attributeValues;
    }

    public void setAttributeValues(List<CameraIncidentAttributeValueResponse> attributeValues) {
        this.attributeValues = attributeValues;
    }
}
