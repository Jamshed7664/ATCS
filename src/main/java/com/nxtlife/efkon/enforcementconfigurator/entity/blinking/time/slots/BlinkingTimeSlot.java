package com.nxtlife.efkon.enforcementconfigurator.entity.blinking.time.slots;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "blinking_time_slot")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class BlinkingTimeSlot extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "start_time can't be null")
	private LocalTime startTime;

	@NotNull(message = "end_time can't be null")
	private LocalTime endTime;

	@NotNull
	@ManyToOne
	private Camera camera;

	@Transient
	private String tCameraId;

	public BlinkingTimeSlot() {
		super();
	}

	public BlinkingTimeSlot(LocalTime startTime, LocalTime endTime, String tCameraId) {
		this.startTime = startTime;
		this.endTime = endTime;
		if (tCameraId != null) {
			this.camera = new Camera();
			this.camera.setId(tCameraId);
		}
		this.tCameraId = tCameraId;
	}

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

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public String gettCameraId() {
		return tCameraId;
	}

	public void settCameraId(String tCameraId) {
		this.tCameraId = tCameraId;
	}
}
