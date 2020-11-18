package com.nxtlife.efkon.enforcementconfigurator.view.incident;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class IncidentAttributeResponse implements Response {

    @Schema(description = "Id of the incident attribute", example = "1")
    private Long id;

    @Schema(description = "Name of the incident attribute", example = "Max Speed")
    private String name;

    @Schema(description = "Datatype of the incident attribute", example = "Text")
    private DataType dataType;

    @Schema(description = "Unit of the attribute", example = "km/hr")
    private String unit;

    public IncidentAttributeResponse(Long id, String name, DataType dataType, String unit) {
        super();
        this.id = id;
        this.name = name;
        this.dataType = dataType;
        this.unit = unit;
    }

    public Long getId() {
        return mask(id);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DataType getDataType() {
        return dataType;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
