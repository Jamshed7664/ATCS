package com.nxtlife.efkon.enforcementconfigurator.entity.location.type;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;

@SuppressWarnings("serial")
@Entity
@Table(name = "location_type_fx", uniqueConstraints = {
		@UniqueConstraint(columnNames = { "code", "organization_id" }) })
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class LocationType extends BaseEntity implements Serializable {

	@Id
	private String id;
	
	@NotNull(message = "code can't be null")
	private String code;

	@NotNull(message = "name can't be null")
	private String name;

	@ManyToOne
	private Organization organization;

	public LocationType() {
		super();
	}

	public LocationType(String id,@NotNull String code, @NotNull(message = "name can't be null") String name,
			Long organizationId) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		if (organizationId != null) {
			this.organization = new Organization();
			this.organization.setId(organizationId);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

}
