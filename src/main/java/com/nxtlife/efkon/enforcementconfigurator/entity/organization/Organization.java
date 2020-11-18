package com.nxtlife.efkon.enforcementconfigurator.entity.organization;

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

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.user.User;

@SuppressWarnings("serial")
@Entity
@Table(name = "organization")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Organization extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(unique = true)
	public String name;

	@Column(unique = true)
	public String code;
	@Column(length = 1000)
	public String description;
	public String centreLongitude;
	public String centreLatitude;
	public String pointA;
	public String pointB;
	public String pointC;
	public String pointD;
	public Boolean isCodeAutoIncremented;
	public Integer incidentDurationLimit;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
	public List<User> users;

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCentreLongitude() {
		return centreLongitude;
	}

	public void setCentreLongitude(String centreLongitude) {
		this.centreLongitude = centreLongitude;
	}

	public String getCentreLatitude() {
		return centreLatitude;
	}

	public void setCentreLatitude(String centreLatitude) {
		this.centreLatitude = centreLatitude;
	}

	public String getPointA() {
		return pointA;
	}

	public void setPointA(String pointA) {
		this.pointA = pointA;
	}

	public String getPointB() {
		return pointB;
	}

	public void setPointB(String pointB) {
		this.pointB = pointB;
	}

	public String getPointC() {
		return pointC;
	}

	public void setPointC(String pointC) {
		this.pointC = pointC;
	}

	public String getPointD() {
		return pointD;
	}

	public void setPointD(String pointD) {
		this.pointD = pointD;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getIsCodeAutoIncremented() {
		return isCodeAutoIncremented;
	}

	public void setIsCodeAutoIncremented(Boolean isCodeAutoIncremented) {
		this.isCodeAutoIncremented = isCodeAutoIncremented;
	}

	public Integer getIncidentDurationLimit() {
		return incidentDurationLimit;
	}

	public void setIncidentDurationLimit(Integer incidentDurationLimit) {
		this.incidentDurationLimit = incidentDurationLimit;
	}
}
