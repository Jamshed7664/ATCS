package com.nxtlife.efkon.enforcementconfigurator.entity.common;

import static com.nxtlife.efkon.enforcementconfigurator.service.BaseService.getUser;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;
import com.nxtlife.efkon.enforcementconfigurator.service.BaseService;

@MappedSuperclass
public class BaseEntity {

	@NotNull
	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "modified_at")
	private Date modifiedAt;

	@ManyToOne
	@JoinColumn(name = "created_by")
	private User createdBy;

	@ManyToOne
	@JoinColumn(name = "modified_by")
	private User modifiedBy;

	@NotNull
	@Column(name = "active", columnDefinition = "boolean default true")
	private Boolean active = true;

	/**
	 * this method will work whenever we call save method in repository. It will
	 * set following property
	 *
	 * <ul>
	 * <li>createdAt to current date and time</li>
	 * <li>active default value (true)</li>
	 * <li>createdBy to whoever user is login it will set according to that</li>
	 * </ul>
	 *
	 */
	@PrePersist
	protected void prePersist() {
		this.createdAt = new Date();
		this.active = true;
		Long userId;
		if ((userId = BaseService.getUserId()) != null) {
			this.createdBy = new User();
			this.createdBy.setId(userId);
		}
	}

	/**
	 * this method will work whenever we call update method in repository. It
	 * will set following property
	 *
	 * <ul>
	 * <li>createdAt to current date and time</li>
	 * <li>active default value (true)</li>
	 * <li>createdBy to whoever user is login it will set according to that</li>
	 * </ul>
	 *
	 */
	@PreUpdate
	protected void preUpdate() {
		this.modifiedAt = new Date();
		this.active = true;
		Long userId;
		if ((userId = getUser().getUserId()) != null) {
			this.modifiedBy = new User();
			this.modifiedBy.setId(userId);
		}
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getModifiedAt() {
		return modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public User getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(User modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

}
