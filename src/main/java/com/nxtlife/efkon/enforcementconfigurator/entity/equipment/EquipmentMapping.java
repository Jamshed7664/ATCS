package com.nxtlife.efkon.enforcementconfigurator.entity.equipment;

import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@SuppressWarnings("serial")
@Entity
@Table(name = "equipment_mapping")
public class EquipmentMapping extends BaseEntity implements Serializable {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "location_mst_id")
    private Location location;

    @ManyToOne
    private Arm arm;

    @ManyToOne
    private Lane lane;

    @Transient
    private String tLocationId;

    @Transient
    private String tArmId;

    @Transient
    private String tLaneId;

    @NotNull(message = "equipment id can't be null")
    @OneToOne
    @JoinColumn(name = "equipment_mst_id")
    private Equipment equipment;

    @Transient
    private String tEquipmentId;

    public EquipmentMapping() {
        super();
    }


    public EquipmentMapping(String id, String tLocationId, String tArmId, String tLaneId, String tEquipmentId) {
        this.id = id;
        if (tLocationId != null)
            this.settLocationId(tLocationId);
        this.tLocationId = tLocationId;
        if (tArmId != null)
            this.settArmId(tArmId);
        this.tArmId = tArmId;
        if (tLaneId != null)
            this.settLaneId(tLaneId);
        this.tLaneId = tLaneId;
        if (tEquipmentId != null)
            this.settEquipmentId(tEquipmentId);
        this.tEquipmentId = tEquipmentId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Arm getArm() {
        return arm;
    }

    public void setArm(Arm arm) {
        this.arm = arm;
    }

    public Lane getLane() {
        return lane;
    }

    public void setLane(Lane lane) {
        this.lane = lane;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public String gettLocationId() {
        return tLocationId;
    }

    public void settLocationId(String tLocationId) {
        if (tLocationId != null) {
            this.location = new Location();
            this.location.setId(tLocationId);
        }
        this.tLocationId = tLocationId;
    }

    public String gettArmId() {
        return tArmId;
    }

    public void settArmId(String tArmId) {
        if (tArmId != null) {
            this.arm = new Arm();
            this.arm.setId(tArmId);
        }
        this.tArmId = tArmId;
    }

    public String gettLaneId() {
        return tLaneId;
    }

    public void settLaneId(String tLaneId) {
        if (tLaneId != null) {
            this.lane = new Lane();
            this.lane.setId(tLaneId);
        }
        this.tLaneId = tLaneId;
    }

    public String gettEquipmentId() {
        return tEquipmentId;
    }

    public void settEquipmentId(String tEquipmentId) {
        if (tEquipmentId != null) {
            this.equipment = new Equipment();
            this.equipment.setId(tEquipmentId);
        }
        this.tEquipmentId = tEquipmentId;
    }
}
