package com.nxtlife.efkon.enforcementconfigurator.view.incident;

import com.nxtlife.efkon.enforcementconfigurator.entity.incident.IncidentAttribute;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;

public class IncidentAttributeRequest {

    @NotNull(message = "Name can't be null")
    @Schema(description = "Name of the incident attribute", example = "Max Speed")
    private String name;

    @NotNull(message = "data type can't be null")
    @Schema(description = "Data type of the incident attribute", example = "Text")
    private String dataType;

    @Schema(description = "Unit of the incident attribute", example = "m/s")
    private String unit;

    public IncidentAttribute toEntity() {
        IncidentAttribute incidentAttribute = new IncidentAttribute();
        incidentAttribute.setName(name);
        incidentAttribute.setUnit(unit);
        return incidentAttribute;
    }

    public String getName() {
        return name;
    }

    public String getDataType() {
        return dataType;
    }

    public String getUnit() {
        return unit;
    }
}
