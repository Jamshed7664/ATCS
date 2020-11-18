package com.nxtlife.efkon.enforcementconfigurator.entity.location.type;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.common.StringSetConverter;
import com.nxtlife.efkon.enforcementconfigurator.entity.organization.Organization;
import com.nxtlife.efkon.enforcementconfigurator.enums.DataType;

@SuppressWarnings("serial")
@Entity
@Table(name = "location_type_attribute_mst")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class LocationTypeAttribute extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "name can't be null")
	private String name;

	@NotNull(message = "data type can't be null")
	@Enumerated(EnumType.STRING)
	private DataType dataType;

	@Convert(converter = StringSetConverter.class)
	@Column(length = 1000)
	private Set<String> options;

	@NotNull(message = "fixed can't be null")
	private Boolean fixed;

	@NotNull(message = "location type can't be null")
	@ManyToOne
	private LocationType locationType;

	@Transient
	private String tLocationTypeId;

	@NotNull(message = "organization can't be null")
	@ManyToOne
	private Organization organization;

	@Transient
	private Long tOrganizationId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "locationTypeAttribute")
	private List<LocationAttributeValue> locationAttributeValues;


	public LocationTypeAttribute() {
	}

	public LocationTypeAttribute(String id, @NotNull(message = "name can't be null") String name, @NotNull(message = "data type can't be null") DataType dataType,
								 @NotNull(message = "fixed can't be null") Boolean fixed, String tLocationTypeId, Long tOrganizationId) {
		super();
		this.id = id;
		this.name = name;
		this.dataType = dataType;
		this.fixed = fixed;
		if(tLocationTypeId!=null)
		{
			this.locationType=new LocationType();
			this.locationType.setId(tLocationTypeId);
		}
		this.tLocationTypeId = tLocationTypeId;
		if(tOrganizationId!=null)
		{
			this.organization=new Organization();
			this.organization.setId(tOrganizationId);
		}
		this.tOrganizationId = tOrganizationId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getDataType() {
		return dataType;
	}

	public void setDataType(DataType dataType) {
		this.dataType = dataType;
	}

	public Set<String> getOptions() {
		return options;
	}

	public void setOptions(Set<String> options) {
		this.options = options;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public String gettLocationTypeId() {
		return tLocationTypeId;
	}

	public void settLocationTypeId(String tLocationTypeId) {
		if (tLocationTypeId != null) {
			this.locationType = new LocationType();
			this.locationType.setId(tLocationTypeId);
		}
		this.tLocationTypeId = tLocationTypeId;
	}

	public Boolean getFixed() {
		return fixed;
	}

	public void setFixed(Boolean fixed) {
		this.fixed = fixed;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Long gettOrganizationId() {
		return tOrganizationId;
	}

	public void settOrganizationId(Long tOrganizationId) {
		if (tOrganizationId != null) {
			this.organization = new Organization();
			this.organization.setId(tOrganizationId);
		}
		this.tOrganizationId = tOrganizationId;
	}

	public List<LocationAttributeValue> getLocationAttributeValues() {
		return locationAttributeValues;
	}

	public void setLocationAttributeValues(List<LocationAttributeValue> locationAttributeValues) {
		this.locationAttributeValues = locationAttributeValues;
	}

}
