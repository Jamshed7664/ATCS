package com.nxtlife.efkon.enforcementconfigurator.entity.lane;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.arm.Arm;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "lane_mst")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Lane extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotEmpty(message = "name can't be null or empty")
	private String name;

	@JoinColumn(name = "arm_mst_id")
	@NotNull(message = "arm can't be null")
	@ManyToOne
	private Arm arm;

	@JoinColumn(name = "location_mst_id")
	@NotNull(message = "location can't be null")
	@ManyToOne
	private Location location;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "lane")
	private List<LaneDirection> laneDirectionList;

	@OneToOne
	@JoinColumn(name = "zone_coordinate_id")
	private CameraImageCoordinate cameraImageCoordinate;

	@Transient
	private String tArmId;

	public Lane() {
		super();
	}

	public Lane(String id, @NotEmpty(message = "name can't be null or empty") String name,
			@NotNull(message = "arm can't be null") Arm arm,
			@NotNull(message = "location can't be null") Location location) {
		super();
		this.id = id;
		this.name = name;
		this.arm = arm;
		this.location = location;
	}

	public Lane(@NotEmpty(message = "name can't be null or empty") String name, String tArmId) {
		super();
		this.name = name;
		this.tArmId = tArmId;
	}

	public Lane(String id, @NotEmpty(message = "name can't be null or empty") String name, String armId,
			String locationId) {
		super();
		this.name = name;
		this.id = id;
		this.settArmId(armId);
		if (locationId != null) {
			this.location = new Location();
			this.location.setId(locationId);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Arm getArm() {
		return arm;
	}

	public void setArm(Arm arm) {
		this.arm = arm;
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

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<LaneDirection> getLaneDirectionList() {
		return laneDirectionList;
	}

	public void setLaneDirectionList(List<LaneDirection> laneDirectionList) {
		this.laneDirectionList = laneDirectionList;
	}

	public CameraImageCoordinate getCameraImageCoordinate() {
		return cameraImageCoordinate;
	}

	public void setCameraImageCoordinate(CameraImageCoordinate cameraImageCoordinate) {
		this.cameraImageCoordinate = cameraImageCoordinate;
	}
}
