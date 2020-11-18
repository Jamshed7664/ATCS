package com.nxtlife.efkon.enforcementconfigurator.enums;

public enum CameraType {
	REAR, FRONT;

	public static boolean matches(String Type) {
		for (CameraType at : CameraType.values()) {
			if (at.name().equals(Type)) {
				return true;
			}
		}
		return false;
	}

}
