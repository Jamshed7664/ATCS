//package com.nxtlife.efkon.enforcementconfigurator.view.location;
//
//import com.fasterxml.jackson.annotation.JsonInclude;
//import com.nxtlife.efkon.enforcementconfigurator.view.Response;
//import com.nxtlife.efkon.enforcementconfigurator.view.equipment.EquipmentResponse;
//import io.swagger.v3.oas.annotations.media.Schema;
//
//import java.util.List;
//
//@JsonInclude(JsonInclude.Include.NON_ABSENT)
//public class LocationEquipmentMappingResponse implements Response {
//
//    @Schema(description = "Id of the location", example = "LOCANOI0001")
//    private String locationId;
//
//    @Schema(description = "Id of the arm", example = "ARM1LOCANOI0001")
//    private String armId;
//
//    @Schema(description = "Id of the lane", example = "L1A1LOCANOI0001")
//    private String laneId;
//
//    private List<EquipmentResponse> equipments;
//
//    private List<LocationEquipmentMappingResponse> armEquipments;
//
//    private List<LocationEquipmentMappingResponse> laneEquipments;
//
//    public LocationEquipmentMappingResponse() {
//        super();
//    }
//
//    public LocationEquipmentMappingResponse(String locationId, String armId, String laneId, List<EquipmentResponse> equipments) {
//        super();
//        this.locationId = locationId;
//        this.armId = armId;
//        this.laneId = laneId;
//        this.equipments = equipments;
//    }
//
//    public String getLocationId() {
//        return locationId;
//    }
//
//    public void setLocationId(String locationId) {
//        this.locationId = locationId;
//    }
//
//    public String getArmId() {
//        return armId;
//    }
//
//    public void setArmId(String armId) {
//        this.armId = armId;
//    }
//
//    public String getLaneId() {
//        return laneId;
//    }
//
//    public void setLaneId(String laneId) {
//        this.laneId = laneId;
//    }
//
//    public List<EquipmentResponse> getEquipments() {
//        return equipments;
//    }
//
//    public void setEquipments(List<EquipmentResponse> equipments) {
//        this.equipments = equipments;
//    }
//
//    public List<LocationEquipmentMappingResponse> getArmEquipments() {
//        return armEquipments;
//    }
//
//    public void setArmEquipments(List<LocationEquipmentMappingResponse> armEquipments) {
//        this.armEquipments = armEquipments;
//    }
//
//    public List<LocationEquipmentMappingResponse> getLaneEquipments() {
//        return laneEquipments;
//    }
//
//    public void setLaneEquipments(List<LocationEquipmentMappingResponse> laneEquipments) {
//        this.laneEquipments = laneEquipments;
//    }
//}
