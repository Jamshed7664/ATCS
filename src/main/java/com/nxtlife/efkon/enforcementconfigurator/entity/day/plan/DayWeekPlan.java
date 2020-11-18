package com.nxtlife.efkon.enforcementconfigurator.entity.day.plan;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.DayWeekPlanKey;
import com.nxtlife.efkon.enforcementconfigurator.entity.week.plan.WeekPlan;
import com.nxtlife.efkon.enforcementconfigurator.enums.Days;

@SuppressWarnings("serial")
@Entity
@Table(name = "day_week_plan")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class DayWeekPlan implements Serializable {

	@EmbeddedId
	private DayWeekPlanKey dayWeekPlanKey;

	@MapsId("dayPlanId")
	@ManyToOne
	private DayPlan dayPlan;

	@MapsId("weekPlanId")
	@ManyToOne
	private WeekPlan weekPlan;

	private Days days;

	public DayWeekPlan() {
		super();
	}

	public DayWeekPlan(DayWeekPlanKey dayWeekPlanKey, DayPlan dayPlan, WeekPlan weekPlan) {
		super();
		this.dayWeekPlanKey = dayWeekPlanKey;
		this.dayPlan = dayPlan;
		this.weekPlan = weekPlan;
	}

	public DayWeekPlanKey getDayWeekPlanKey() {
		return dayWeekPlanKey;
	}

	public void setDayWeekPlanKey(DayWeekPlanKey dayWeekPlanKey) {
		this.dayWeekPlanKey = dayWeekPlanKey;
	}

	public DayPlan getDayPlan() {
		return dayPlan;
	}

	public void setDayPlan(DayPlan dayPlan) {
		this.dayPlan = dayPlan;
	}

	public WeekPlan getWeekPlan() {
		return weekPlan;
	}

	public void setWeekPlan(WeekPlan weekPlan) {
		this.weekPlan = weekPlan;
	}

	public Days getDays() {
		return days;
	}

	public void setDays(Days days) {
		this.days = days;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((dayPlan == null) ? 0 : dayPlan.hashCode());
		result = prime * result + ((weekPlan == null) ? 0 : weekPlan.hashCode());
		result = prime * result + ((dayWeekPlanKey == null) ? 0 : dayWeekPlanKey.hashCode());
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
		DayWeekPlan other = (DayWeekPlan) obj;
		if (dayPlan == null) {
			if (other.dayPlan != null)
				return false;
		} else if (!dayPlan.equals(other.dayPlan))
			return false;
		if (weekPlan == null) {
			if (other.weekPlan != null)
				return false;
		} else if (!weekPlan.equals(other.weekPlan))
			return false;
		if (dayWeekPlanKey == null) {
			if (other.dayWeekPlanKey != null)
				return false;
		} else if (!dayWeekPlanKey.equals(other.dayWeekPlanKey))
			return false;
		return true;
	}
}
