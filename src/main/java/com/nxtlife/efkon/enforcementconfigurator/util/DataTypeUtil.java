package com.nxtlife.efkon.enforcementconfigurator.util;

import java.util.regex.Pattern;

import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;

public class DataTypeUtil {

	public static Boolean validValue(DataType dataType, String value) {
		if (value == null) {
			return false;
		}
		try {
			switch (dataType) {
			case Checkbox:
				if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
					return true;
				}
				break;
			case Number:
				if (value.contains(".")) {
					Double.parseDouble(value);
				} else {
					Long.parseLong(value);
				}
				return true;
			case IPADDRESS:
				if (Pattern.matches("((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}", value)) {
					return true;
				}
				break;
			default:
				return true;
			}
		} catch (Exception e) {
			return false;
		}

		return false;
	}

}
