package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import javax.persistence.Embeddable;
import java.io.Serializable;

@SuppressWarnings("serial")
@Embeddable
public class IncidentAttributeIncidentKey implements Serializable {
    private String incidentName;
    private Long incidentAttributeId;

    public IncidentAttributeIncidentKey() {
        super();
    }

    public IncidentAttributeIncidentKey(String incidentName, Long incidentAttributeId) {
        super();
        this.incidentName = incidentName;
        this.incidentAttributeId = incidentAttributeId;
    }

    public String getIncidentName() {
        return incidentName;
    }

    public void setIncidentName(String incidentName) {
        this.incidentName = incidentName;
    }

    public Long getIncidentAttributeId() {
        return incidentAttributeId;
    }

    public void setIncidentAttributeId(Long incidentAttributeId) {
        this.incidentAttributeId = incidentAttributeId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incidentName == null) ? 0 : incidentName.hashCode());
        result = prime * result + ((incidentAttributeId == null) ? 0 : incidentAttributeId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        IncidentAttributeIncidentKey other = (IncidentAttributeIncidentKey) obj;
        if (incidentName == null) {
            if (other.incidentName != null)
                return false;
        } else if (!incidentName.equals(other.incidentName))
            return false;
        if (incidentAttributeId == null) {
            if (other.incidentAttributeId != null)
                return false;
        } else if (!incidentAttributeId.equals(other.incidentAttributeId))
            return false;

        return true;
    }
}
