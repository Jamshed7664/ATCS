package com.nxtlife.efkon.enforcementconfigurator.view.equipment.type;

import java.util.Arrays;
import java.util.HashSet;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.type.EquipmentTypeAttribute;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;
import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class EquipmentTypeAttributeRequest implements Request {

	@Schema(description = "Id of the equipment type attribute which cannot be empty", example = "1")
	private String id;

	@Schema(description = "Name of the equipment type attribute which cannot be empty", example = "Processor", required = true)
	@NotEmpty(message = "name can't be empty")
	private String name;

	@Schema(description = "Data type of the equipment type attribute which cannot be empty", example = "Text", required = true, allowableValues = "[Integer, Boolean, Float, Double, Long, Text, Option, Range]")
	@NotEmpty(message = "data type can't be empty")
	private String dataType;

	@Schema(description = "Specific of the equipment type attribute which cannot be null", example = "true", required = true)
	@NotNull(message = "specific can't be null")
	private Boolean isSpecific;

	@Schema(description = "Options if data type is option or range", example = "A,B,C", maxLength = 1000)
	private String options;

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDataType() {
		return dataType;
	}

	public Boolean getIsSpecific() {
		return isSpecific;
	}

	public String getOptions() {
		return options;
	}

	public EquipmentTypeAttribute toEntity() {
		EquipmentTypeAttribute equipmentTypeAttribute = new EquipmentTypeAttribute();
		equipmentTypeAttribute.setName(name);
		equipmentTypeAttribute.setIsSpecific(isSpecific);
		if (dataType != null)
			equipmentTypeAttribute.setDataType(DataType.valueOf(dataType));
		if (options != null)
			equipmentTypeAttribute.setOptions(new HashSet<>(Arrays.asList(options.split(","))));
		return equipmentTypeAttribute;
	}
}
