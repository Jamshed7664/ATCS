package com.nxtlife.efkon.enforcementconfigurator.entity.incident;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.IncidentAttributeIncidentKey;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "incident_attribute_incident")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class IncidentAttributeIncident implements Serializable {

    @EmbeddedId
    private IncidentAttributeIncidentKey incidentAttributeIncidentKey;

    @MapsId("incidentName")
    @ManyToOne
    private Incident incident;

    @MapsId("incidentAttributeId")
    @ManyToOne
    private IncidentAttribute incidentAttribute;

    public IncidentAttributeIncident() {
        super();
    }

    public IncidentAttributeIncident(IncidentAttributeIncidentKey incidentAttributeIncidentKey, Incident incident,
                                     IncidentAttribute incidentAttribute) {
        super();
        this.incidentAttributeIncidentKey = incidentAttributeIncidentKey;
        this.incident = incident;
        this.incidentAttribute = incidentAttribute;
    }

    public IncidentAttributeIncident(String incidentName,Long incidentAttributeId)
    {
        this.incidentAttributeIncidentKey=new IncidentAttributeIncidentKey(incidentName,incidentAttributeId);
        if(incidentName!=null)
        {
            this.incident=new Incident();
            this.incident.setName(incidentName);
        }
        if(incidentAttributeId!=null)
        {
            this.incidentAttribute=new IncidentAttribute();
            this.incidentAttribute.setId(incidentAttributeId);
        }
    }

    public IncidentAttributeIncidentKey getIncidentAttributeIncidentKey() {
        return incidentAttributeIncidentKey;
    }

    public void setIncidentAttributeIncidentKey(IncidentAttributeIncidentKey incidentAttributeIncidentKey) {
        this.incidentAttributeIncidentKey = incidentAttributeIncidentKey;
    }

    public Incident getIncident() {
        return incident;
    }

    public void setIncident(Incident incident) {
        this.incident = incident;
    }

    public IncidentAttribute getIncidentAttribute() {
        return incidentAttribute;
    }

    public void setIncidentAttribute(IncidentAttribute incidentAttribute) {
        this.incidentAttribute = incidentAttribute;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((incident == null) ? 0 : incident.hashCode());
        result = prime * result + ((incidentAttribute == null) ? 0 : incidentAttribute.hashCode());
        result = prime * result + ((incidentAttributeIncidentKey == null) ? 0 : incidentAttributeIncidentKey.hashCode());
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
        IncidentAttributeIncident other = (IncidentAttributeIncident) obj;
        if (incident == null) {
            if (other.incident != null)
                return false;
        } else if (!incident.equals(other.incident))
            return false;
        if (incidentAttribute == null) {
            if (other.incidentAttribute != null)
                return false;
        } else if (!incidentAttribute.equals(other.incidentAttribute))
            return false;
        if (incidentAttributeIncidentKey == null) {
            if (other.incidentAttributeIncidentKey != null)
                return false;
        } else if (!incidentAttributeIncidentKey.equals(other.incidentAttributeIncidentKey))
            return false;
        return true;
    }
}
