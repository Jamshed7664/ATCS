package com.nxtlife.efkon.enforcementconfigurator.view.equipment.type;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentType;

import io.swagger.v3.oas.annotations.media.Schema;

public class EquipmentTypeRequest {

	@NotEmpty(message = "name can't be empty")
	@Schema(description = "Name of equipment type", example = "Rsu")
	private String name;

	@NotEmpty(message = "code can't be empty")
	@Schema(description = "Code of equipment type", example = "RSU")
	private String code;

	@NotNull(message = "quantity can't be null")
	@Schema(description = "Quantity of equipment type", example = "200")
	private Integer quantity;

	@Schema(description = "Equipment type fields detail")
	private List<EquipmentTypeAttributeRequest> equipmentTypeAttributes;

	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public List<EquipmentTypeAttributeRequest> getEquipmentTypeAttributes() {
		return equipmentTypeAttributes;
	}

	public EquipmentType toEntity() {
		EquipmentType equipmentType = new EquipmentType(null, code, name, quantity, null);
		return equipmentType;
	}
}
