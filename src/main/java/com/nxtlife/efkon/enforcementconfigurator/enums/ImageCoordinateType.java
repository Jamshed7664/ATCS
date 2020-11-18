package com.nxtlife.efkon.enforcementconfigurator.enums;

public enum ImageCoordinateType {
	INCIDENT_DETECTION, RED_LAMP, ROAD_ALIGNMENT, ZONE;

	public static boolean matches(String imageCoordinateType) {
		for (ImageCoordinateType type : ImageCoordinateType.values()) {
			if (type.name().equals(imageCoordinateType)) {
				return true;
			}
		}
		return false;
	}

}
