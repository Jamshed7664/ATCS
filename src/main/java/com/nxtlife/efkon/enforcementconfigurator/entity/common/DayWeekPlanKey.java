package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import java.io.Serializable;

import javax.persistence.Embeddable;

@SuppressWarnings("serial")
@Embeddable
public class DayWeekPlanKey implements Serializable {

	private Long dayPlanId;
	private Long weekPlanId;

	public DayWeekPlanKey() {
		super();
	}

	public DayWeekPlanKey(Long dayPlanId, Long weekPlanId) {
		super();
		this.dayPlanId = dayPlanId;
		this.weekPlanId = weekPlanId;
	}

	public Long getDayPlanId() {
		return dayPlanId;
	}

	public void setDayPlanId(Long dayPlanId) {
		this.dayPlanId = dayPlanId;
	}

	public Long getWeekPlanId() {
		return weekPlanId;
	}

	public void setWeekPlanId(Long weekPlanId) {
		this.weekPlanId = weekPlanId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dayPlanId == null) ? 0 : dayPlanId.hashCode());
		result = prime * result + ((weekPlanId == null) ? 0 : weekPlanId.hashCode());
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
		DayWeekPlanKey other = (DayWeekPlanKey) obj;
		if (dayPlanId == null) {
			if (other.dayPlanId != null)
				return false;
		} else if (!dayPlanId.equals(other.dayPlanId))
			return false;
		if (weekPlanId == null) {
			if (other.weekPlanId != null)
				return false;
		} else if (!weekPlanId.equals(other.weekPlanId))
			return false;

		return true;
	}
}
