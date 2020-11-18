package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class LaneDirectionKey implements Serializable {

	private String laneId;
	private Long directionId;

	public LaneDirectionKey() {
	}

	public LaneDirectionKey(String laneId, Long directionId) {
		this.laneId = laneId;
		this.directionId = directionId;
	}

	public String getLaneId() {
		return laneId;
	}

	public void setLaneId(String laneId) {
		this.laneId = laneId;
	}

	public Long getDirectionId() {
		return directionId;
	}

	public void setDirectionId(Long directionId) {
		this.directionId = directionId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((laneId == null) ? 0 : laneId.hashCode());
		result = prime * result + ((directionId == null) ? 0 : directionId.hashCode());
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
		LaneDirectionKey other = (LaneDirectionKey) obj;
		if (laneId == null) {
			if (other.laneId != null)
				return false;
		} else if (!laneId.equals(other.laneId))
			return false;
		if (directionId == null) {
			if (other.directionId != null)
				return false;
		} else if (!directionId.equals(other.directionId))
			return false;

		return true;
	}
}
