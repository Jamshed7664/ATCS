package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.List;

import com.nxtlife.efkon.enforcementconfigurator.view.location.LocationResponse;

public class EquipmentLocationMappingResponse {

	private List<EquipmentGroupByResponse> equipments;

	private LocationResponse location;

	public List<EquipmentGroupByResponse> getEquipments() {
		return equipments;
	}

	public void setEquipments(List<EquipmentGroupByResponse> equipments) {
		this.equipments = equipments;
	}

	public LocationResponse getLocation() {
		return location;
	}

	public void setLocation(LocationResponse location) {
		this.location = location;
	}

}
