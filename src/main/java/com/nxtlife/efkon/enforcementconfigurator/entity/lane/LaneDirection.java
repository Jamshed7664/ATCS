package com.nxtlife.efkon.enforcementconfigurator.entity.lane;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.LaneDirectionKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.direction.Direction;

@SuppressWarnings("serial")
@Entity
@Table(name = "lane_direction")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class LaneDirection implements Serializable {

	@EmbeddedId
	private LaneDirectionKey laneDirectionKey;

	@MapsId("laneId")
	@ManyToOne
	private Lane lane;

	@MapsId("directionId")
	@ManyToOne
	private Direction direction;

	public LaneDirection() {
	}

	public LaneDirection(LaneDirectionKey laneDirectionKey, Lane lane, Direction direction) {
		this.laneDirectionKey = laneDirectionKey;
		this.lane = lane;
		this.direction = direction;
	}

	public LaneDirection(String laneId, Long directionId) {
		this.laneDirectionKey = new LaneDirectionKey(laneId, directionId);
		if (laneId != null) {
			this.lane = new Lane();
			this.lane.setId(laneId);
		}
		if (directionId != null) {
			this.direction = new Direction();
			this.direction.setId(directionId);
		}
	}

	public LaneDirectionKey getLaneDirectionKey() {
		return laneDirectionKey;
	}

	public void setLaneDirectionKey(LaneDirectionKey laneDirectionKey) {
		this.laneDirectionKey = laneDirectionKey;
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lane == null) ? 0 : lane.hashCode());
		result = prime * result + ((direction == null) ? 0 : direction.hashCode());
		result = prime * result + ((laneDirectionKey == null) ? 0 : laneDirectionKey.hashCode());
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
		LaneDirection other = (LaneDirection) obj;
		if (lane == null) {
			if (other.lane != null)
				return false;
		} else if (!lane.equals(other.lane))
			return false;
		if (direction == null) {
			if (other.direction != null)
				return false;
		} else if (!direction.equals(other.direction))
			return false;
		if (laneDirectionKey == null) {
			if (other.laneDirectionKey != null)
				return false;
		} else if (!laneDirectionKey.equals(other.laneDirectionKey))
			return false;
		return true;
	}

}
