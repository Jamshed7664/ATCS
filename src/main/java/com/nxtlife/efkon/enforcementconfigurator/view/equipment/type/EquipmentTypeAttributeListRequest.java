package com.nxtlife.efkon.enforcementconfigurator.view.equipment.type;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotEmpty;
import java.util.List;

public class EquipmentTypeAttributeListRequest {

    @Schema(description = "List of equipment type attributes", required = true)
    @NotEmpty(message = "equipment type attributes can't be empty")
    private List<EquipmentTypeAttributeRequest> list;

    public List<EquipmentTypeAttributeRequest> getList() {
        return list;
    }
}
