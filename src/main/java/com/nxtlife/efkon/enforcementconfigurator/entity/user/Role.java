package com.nxtlife.efkon.enforcementconfigurator.entity.user;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;

@SuppressWarnings("serial")
@Entity
@Table(name = "role")
@DynamicInsert(value = true)
@DynamicUpdate(value = true)
public class Role extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotEmpty(message = "name can't be empty")
	@Column(name = "name")
	private String name;

	@NotNull(message = "max assigned city can't be null")
	@Min(value = 0, message = "min assigned city value should be 0")
	@Column(name = "max_assigned_city")
	private BigInteger maxAssignedCity = new BigInteger("0");

	@ManyToOne(fetch = FetchType.LAZY)
	private Organization organization;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private List<RoleAuthority> roleAuthorities;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "role")
	private List<UserRole> roleUsers;

	public Role(Long id, String name, BigInteger maxAssignedCity) {
		super();
		this.setId(id);
		this.name = name;
		this.maxAssignedCity = maxAssignedCity;
	}

	public Role(String name) {
		super();
		this.name = name;
	}

	public Role() {
		super();
	}

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

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public List<UserRole> getRoleUsers() {
		return roleUsers;
	}

	public void setRoleUsers(List<UserRole> roleUsers) {
		this.roleUsers = roleUsers;
	}

	public List<RoleAuthority> getRoleAuthorities() {
		return roleAuthorities;
	}

	public void setRoleAuthorities(List<RoleAuthority> roleAuthorities) {
		this.roleAuthorities = roleAuthorities;
	}

	public BigInteger getMaxAssignedCity() {
		return maxAssignedCity;
	}

	public void setMaxAssignedCity(BigInteger maxAssignedCity) {
		this.maxAssignedCity = maxAssignedCity;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((maxAssignedCity == null) ? 0 : maxAssignedCity.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((organization == null) ? 0 : organization.hashCode());
		result = prime * result + ((roleAuthorities == null) ? 0 : roleAuthorities.hashCode());
		result = prime * result + ((roleUsers == null) ? 0 : roleUsers.hashCode());
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
		Role other = (Role) obj;
		if (maxAssignedCity == null) {
			if (other.maxAssignedCity != null)
				return false;
		} else if (!maxAssignedCity.equals(other.maxAssignedCity))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (organization == null) {
			if (other.organization != null)
				return false;
		} else if (!organization.equals(other.organization))
			return false;
		if (roleAuthorities == null) {
			if (other.roleAuthorities != null)
				return false;
		} else if (!roleAuthorities.equals(other.roleAuthorities))
			return false;
		if (roleUsers == null) {
			if (other.roleUsers != null)
				return false;
		} else if (!roleUsers.equals(other.roleUsers))
			return false;
		return true;
	}

}
