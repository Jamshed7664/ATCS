package com.nxtlife.efkon.enforcementconfigurator.view.equipment.type;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class EquipmentTypeAttributeResponse implements Response {

	@Schema(description = "Id of equipment type attribute", example = "1")
	private String id;

	@Schema(description = "Name of equipment type attribute", example = "Processor")
	private String name;

	@Schema(description = "Data type of equipment type attribute", example = "Text")
	private DataType dataType;

	@Schema(description = "Data type of equipment type attribute for html input type", example = "text")
	private String htmlInputType;

	@Schema(description = "Equipment type attribute is static or not", defaultValue = "false", example = "true")
	private Boolean fixed;

	@Schema(description = "Equipment type attribute is specific or not", defaultValue = "false", example = "true")
	private Boolean isSpecific;

	@Schema(description = "Equipment type details")
	private EquipmentTypeResponse equipmentType;

	@Schema(description = "Equipment type attribute options if required", example = "1,2,3", nullable = true)
	private Set<String> options;

	public EquipmentTypeAttributeResponse(String id, String name, DataType dataType, Boolean fixed, Boolean isSpecific,
			Set<String> options) {
		super();
		this.id = id;
		this.name = name;
		this.dataType = dataType;
		if (dataType != null) {
			this.htmlInputType = dataType.getHtmlInputType();
		}
		this.isSpecific = isSpecific;
		this.fixed = fixed;
		this.options = options;
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

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public String getHtmlInputType() {
		return htmlInputType;
	}

	public void setHtmlInputType(String htmlInputType) {
		this.htmlInputType = htmlInputType;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public Boolean getIsSpecific() {
		return isSpecific;
	}

	public void setIsSpecific(Boolean isSpecific) {
		this.isSpecific = isSpecific;
	}

	public EquipmentTypeResponse getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentTypeResponse equipmentType) {
		this.equipmentType = equipmentType;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

}
