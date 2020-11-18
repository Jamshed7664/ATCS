package com.nxtlife.efkon.enforcementconfigurator.entity.day.plan;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "day_plan")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class DayPlan extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private Integer sequence;

	@ManyToOne
	private Camera camera;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
