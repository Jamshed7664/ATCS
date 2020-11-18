package com.nxtlife.efkon.enforcementconfigurator.view.equipment;

import java.util.Collections;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class EquipmentAttributeValueRequest implements Request {

	@Schema(description = "Id of the equipment type attribute which cannot be empty", example = "1")
	@NotEmpty(message = "attribute id can't be empty")
	private String attributeId;

	@Schema(description = "Value of the equipment type attribute which cannot be empty", example = "iOS", required = true)
	private String value;

	@Schema(description = "If attribute is specific for a equipment then we need this property otherwise it will be null", example = "EQMST000001")
	private String equipmentId;

	@Schema(description = "Equipment attribute values are specific for a equipment then we set this property")
	private List<EquipmentAttributeValueRequest> attributeValues;

	public String getAttributeId() {
		return attributeId;
	}

	public String getValue() {
		return value;
	}

	public String getEquipmentId() {
		return equipmentId;
	}

	public List<EquipmentAttributeValueRequest> getAttributeValues() {
		if (attributeValues != null) {
			Collections.sort(attributeValues, (attributeValue1, attributeValue2) -> {
				if (attributeValue1.getEquipmentId() == null && attributeValue2.getEquipmentId() == null) {
					return 0;
				} else if (attributeValue1.getEquipmentId() == null) {
					return -1;
				} else if (attributeValue2.getEquipmentId() == null) {
					return 1;
				}
				return 0;
			});
		}
		return attributeValues;
	}

}
