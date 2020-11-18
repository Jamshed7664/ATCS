package com.nxtlife.efkon.enforcementconfigurator.entity.arm;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotEmpty;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.entity.location.Location;

@SuppressWarnings("serial")
@Entity
@Table(name = "arm_mst", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "location_mst_id" }) })
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Arm extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotEmpty(message = "name can't be null or empty")
	private String name;

	@NotEmpty(message= "direction can't be null or empty")
	private String direction;
	
	@JoinColumn(name = "location_mst_id")
	@ManyToOne
	private Location location;

	@Transient
	private String tLocationId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "arm")
	private List<Lane> lanes;

	public Arm() {
		super();
	}

	public Arm(String id, @NotEmpty(message = "name can't be null or empty") String name,
			@NotEmpty(message = "direction can't be null or empty") String direction, Location location,
			List<Lane> lanes) {
		super();
		this.id = id;
		this.name = name;
		this.direction = direction;
		this.location = location;
		this.lanes = lanes;
	}

	public String gettLocationId() {
		return tLocationId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public void settLocationId(String tLocationId) {
		if (tLocationId != null) {
			this.location = new Location();
			this.location.setId(tLocationId);
		}
		this.tLocationId = tLocationId;
	}

	public List<Lane> getLanes() {
		return lanes;
	}

	public void setLanes(List<Lane> lanes) {
		this.lanes = lanes;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
