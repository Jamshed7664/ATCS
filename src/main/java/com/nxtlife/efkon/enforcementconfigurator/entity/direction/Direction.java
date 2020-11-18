package com.nxtlife.efkon.enforcementconfigurator.entity.direction;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.LaneDirection;

@SuppressWarnings("serial")
@Entity
@Table(name = "direction_fx")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Direction extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotNull(message = "name can't be null")
	@Column(unique = true)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "direction")
	private List<LaneDirection> laneDirections;

	public Direction() {
		super();
	}

	public Direction(@NotNull(message = "name can't be null") String name) {
		super();
		this.name = name;
	}

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<LaneDirection> getLaneDirections() {
		return laneDirections;
	}

	public void setLaneDirections(List<LaneDirection> laneDirections) {
		this.laneDirections = laneDirections;
	}
}
