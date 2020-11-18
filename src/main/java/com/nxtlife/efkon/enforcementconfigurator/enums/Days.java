package com.nxtlife.efkon.enforcementconfigurator.enums;

public enum Days {
	SUN, MON, TUE, WED, THU, FRI, SAT;

	public static boolean matches(String days) {
		for (Days day : Days.values()) {
			if (day.name().equals(days)) {
				return true;
			}
		}
		return false;
	}
}
