package com.nxtlife.efkon.enforcementconfigurator.enums;

public enum IncidentType {
    VEHICLE_INCIDENT, TRAFFIC_INCIDENT, WEATHER_INCIDENT, OTHER;

    public static boolean matches(String incidentType) {
        for (IncidentType type : IncidentType.values()) {
            if (type.name().equals(incidentType)) {
                return true;
            }
        }
        return false;
    }

}
