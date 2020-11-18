package com.nxtlife.efkon.enforcementconfigurator.entity.camera;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.lane.Lane;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;

@SuppressWarnings("serial")
@Entity
@Table(name = "camera_image_coordinate")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class CameraImageCoordinate extends BaseEntity implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull
	@Column(name = "a_point_x_coordinate")
	private BigDecimal aPointXCoordinate;

	@NotNull
	@Column(name = "a_point_y_coordinate")
	private BigDecimal aPointYCoordinate;

	@NotNull
	@Column(name = "b_point_x_coordinate")
	private BigDecimal bPointXCoordinate;

	@NotNull
	@Column(name = "b_point_y_coordinate")
	private BigDecimal bPointYCoordinate;

	@NotNull
	@Column(name = "c_point_x_coordinate")
	private BigDecimal cPointXCoordinate;

	@NotNull
	@Column(name = "c_point_y_coordinate")
	private BigDecimal cPointYCoordinate;

	@NotNull
	@Column(name = "d_point_x_coordinate")
	private BigDecimal dPointXCoordinate;

	@NotNull
	@Column(name = "d_point_y_coordinate")
	private BigDecimal dPointYCoordinate;

	private Integer sequence;

	@NotNull(message = "image_coordinate_type can't be null")
	@Enumerated(EnumType.STRING)
	private ImageCoordinateType imageCoordinateType;

	private Double roadLength;

	private Double roadBreadth;

	private BigDecimal maxDistance;

	@NotNull
	@ManyToOne
	private CameraImage cameraImage;

	@Transient
	private String tCameraImageId;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "cameraImageCoordinate")
	private Lane lane;

	@Transient
	private String tLaneId;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "cameraImageCoordinate")
	private List<ZoneIncident> zoneIncidents;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<ZoneIncident> getZoneIncidents() {
		return zoneIncidents;
	}

	public void setZoneIncidents(List<ZoneIncident> zoneIncidents) {
		this.zoneIncidents = zoneIncidents;
	}

	public BigDecimal getaPointXCoordinate() {
		return aPointXCoordinate;
	}

	public void setaPointXCoordinate(BigDecimal aPointXCoordinate) {
		this.aPointXCoordinate = aPointXCoordinate;
	}

	public BigDecimal getaPointYCoordinate() {
		return aPointYCoordinate;
	}

	public void setaPointYCoordinate(BigDecimal aPointYCoordinate) {
		this.aPointYCoordinate = aPointYCoordinate;
	}

	public BigDecimal getbPointXCoordinate() {
		return bPointXCoordinate;
	}

	public void setbPointXCoordinate(BigDecimal bPointXCoordinate) {
		this.bPointXCoordinate = bPointXCoordinate;
	}

	public BigDecimal getbPointYCoordinate() {
		return bPointYCoordinate;
	}

	public void setbPointYCoordinate(BigDecimal bPointYCoordinate) {
		this.bPointYCoordinate = bPointYCoordinate;
	}

	public BigDecimal getcPointXCoordinate() {
		return cPointXCoordinate;
	}

	public void setcPointXCoordinate(BigDecimal cPointXCoordinate) {
		this.cPointXCoordinate = cPointXCoordinate;
	}

	public BigDecimal getcPointYCoordinate() {
		return cPointYCoordinate;
	}

	public void setcPointYCoordinate(BigDecimal cPointYCoordinate) {
		this.cPointYCoordinate = cPointYCoordinate;
	}

	public BigDecimal getdPointXCoordinate() {
		return dPointXCoordinate;
	}

	public void setdPointXCoordinate(BigDecimal dPointXCoordinate) {
		this.dPointXCoordinate = dPointXCoordinate;
	}

	public BigDecimal getdPointYCoordinate() {
		return dPointYCoordinate;
	}

	public void setdPointYCoordinate(BigDecimal dPointYCoordinate) {
		this.dPointYCoordinate = dPointYCoordinate;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public ImageCoordinateType getImageCoordinateType() {
		return imageCoordinateType;
	}

	public void setImageCoordinateType(ImageCoordinateType imageCoordinateType) {
		this.imageCoordinateType = imageCoordinateType;
	}

	public String gettCameraImageId() {
		return tCameraImageId;
	}

	public void settCameraImageId(String tCameraImageId) {
		if (tCameraImageId != null) {
			this.cameraImage = new CameraImage();
			this.cameraImage.setId(tCameraImageId);
		}
		this.tCameraImageId = tCameraImageId;
	}

	public String gettLaneId() {
		return tLaneId;
	}

	public void settLaneId(String tLaneId) {
		this.tLaneId = tLaneId;
	}

	public Double getRoadLength() {
		return roadLength;
	}

	public void setRoadLength(Double roadLength) {
		this.roadLength = roadLength;
	}

	public Double getRoadBreadth() {
		return roadBreadth;
	}

	public void setRoadBreadth(Double roadBreadth) {
		this.roadBreadth = roadBreadth;
	}

	public BigDecimal getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(BigDecimal maxDistance) {
		this.maxDistance = maxDistance;
	}

	public CameraImage getCameraImage() {
		return cameraImage;
	}

	public void setCameraImage(CameraImage cameraImage) {
		this.cameraImage = cameraImage;
	}

	public Lane getLane() {
		return lane;
	}

	public void setLane(Lane lane) {
		this.lane = lane;
	}
}
