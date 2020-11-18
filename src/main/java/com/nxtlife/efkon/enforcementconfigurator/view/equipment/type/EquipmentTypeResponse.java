package com.nxtlife.efkon.enforcementconfigurator.view.equipment.type;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class EquipmentTypeResponse implements Response {

	@Schema(description = "Id of equipment type", example = "1")
	private String id;

	@Schema(description = "Name of equipment type", example = "Rsu")
	private String name;

	@Schema(description = "Code of equipment type", example = "RSU")
	private String code;

	@Schema(description = "Count of equipment added for this type", example = "20")
	private Long count;

	@Schema(description = "Available count of equipment added for this type. It will be always less than count", example = "10")
	private Long availableCount;

	@Schema(description = "Quantity of equipment type", example = "200")
	private Integer quantity;

	@Schema(description = "Equipment type fields detail")
	private List<EquipmentTypeAttributeResponse> equipmentTypeAttributes;

	public EquipmentTypeResponse(String id, String name, String code, Integer quantity) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.quantity = quantity;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Long getAvailableCount() {
		return availableCount;
	}

	public void setAvailableCount(Long availableCount) {
		this.availableCount = availableCount;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<EquipmentTypeAttributeResponse> getEquipmentTypeAttributes() {
		return equipmentTypeAttributes;
	}

	public void setEquipmentTypeAttributes(List<EquipmentTypeAttributeResponse> equipmentTypeAttributes) {
		this.equipmentTypeAttributes = equipmentTypeAttributes;
	}

	public EquipmentTypeResponse get(EquipmentType equipmentType) {
		if (equipmentType != null) {
			EquipmentTypeResponse response = new EquipmentTypeResponse(equipmentType.getId(), equipmentType.getName(),
					equipmentType.getCode(), equipmentType.getQuantity());
			return response;
		}
		return null;
	}
}
