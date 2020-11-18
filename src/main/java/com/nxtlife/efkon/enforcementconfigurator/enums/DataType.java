package com.nxtlife.efkon.enforcementconfigurator.enums;

import java.util.Arrays;

public enum DataType {

	Checkbox("checkbox"), Text("text"), Number("number"), Option("dropdown"), Range("dropdown"), IPADDRESS("text");

	private String htmlInputType;

	private DataType(String htmlInputType) {
		this.htmlInputType = htmlInputType;
	}

	public static boolean matches(String Type) {
		for (DataType at : DataType.values()) {
			if (at.name().equals(Type)) {
				return true;
			}
		}
		return false;
	}

	public static String[] strValues() {
		return Arrays.toString(DataType.values()).replaceAll("^.|.$", "").split(", ");
	}

	public String getHtmlInputType() {
		return htmlInputType;
	}

}
