package com.nxtlife.efkon.enforcementconfigurator.entity.day.plan;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.schedule.Schedule;

@SuppressWarnings("serial")
@Entity
@Table(name = "day_plan_slot", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "schedule_id", "day_plan_id" }) })
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class DayPlanSlot extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "start time can't be null")
	private LocalTime startTime;

	@NotNull(message = "end time can't be null")
	private LocalTime endTime;

	@ManyToOne
	@NotNull
	private Schedule schedule;

	@ManyToOne
	@NotNull
	private DayPlan dayPlan;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public LocalTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalTime startTime) {
		this.startTime = startTime;
	}

	public LocalTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalTime endTime) {
		this.endTime = endTime;
	}

	public Schedule getSchedule() {
		return schedule;
	}

	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}

	public DayPlan getDayPlan() {
		return dayPlan;
	}

	public void setDayPlan(DayPlan dayPlan) {
		this.dayPlan = dayPlan;
	}
}
