package com.nxtlife.efkon.enforcementconfigurator.view.vehicle.type;

import com.nxtlife.efkon.enforcementconfigurator.entity.vehicle.type.VehicleType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import io.swagger.v3.oas.annotations.media.Schema;

public class VehicleTypeResponse implements Response {

    @Schema(description = "Id of the vehicle type", example = "1")
    private Long id;

    @Schema(description = "Name of the vehicle type", example = "Truck/Bus")
    private String name;

    public VehicleTypeResponse() {
        super();
    }

    public VehicleTypeResponse(VehicleType vehicleType) {
        this.id = vehicleType.getId();
        this.name = vehicleType.getName();
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
}
