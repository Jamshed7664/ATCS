package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.enums.ImageCoordinateType;
import com.nxtlife.efkon.enforcementconfigurator.view.Response;
import com.nxtlife.efkon.enforcementconfigurator.view.lane.LaneResponse;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(JsonInclude.Include.NON_ABSENT)
public class CameraImageCoordinateResponse implements Response {

	@Schema(description = "Id of the camera image coordinate ", example = "1")
	private Long id;

	@Schema(name = "aPointXCoordinate", description = "A Point X coordinate ", example = "123.12")
	private BigDecimal aPointXCoordinate;

	@Schema(name = "aPointYCoordinate", description = "A Point Y coordinate ", example = "123.12")
	private BigDecimal aPointYCoordinate;

	@Schema(name = "bPointXCoordinate", description = "B Point X coordinate ", example = "123.12")
	private BigDecimal bPointXCoordinate;

	@Schema(name = "bPointYCoordinate", description = "B Point Y coordinate ", example = "123.12")
	private BigDecimal bPointYCoordinate;

	@Schema(name = "cPointXCoordinate", description = "C Point X coordinate ", example = "123.12")
	private BigDecimal cPointXCoordinate;

	@Schema(name = "cPointYCoordinate", description = "C Point Y coordinate ", example = "123.12")
	private BigDecimal cPointYCoordinate;

	@Schema(name = "dPointXCoordinate", description = "D Point X coordinate ", example = "123.12")
	private BigDecimal dPointXCoordinate;

	@Schema(name = "dPointYCoordinate", description = "D Point Y coordinate ", example = "123.12")
	private BigDecimal dPointYCoordinate;

	@Schema(description = "Image Coordinate Type ", example = "MAIN")
	private ImageCoordinateType imageCoordinateType;

	@JsonIgnore
	private BigDecimal maxDistance;

	@JsonIgnore
	private Double roadLength;

	@JsonIgnore
	private Double roadBreadth;

	@Schema(description = "Sequence of the zone", example = "1")
	private Integer sequence;

	@Schema(description = "Created at")
	private Date createdAt;

	@Schema(description = "Id of created by", example = "1")
	private Long createdById;

	@Schema(description = "Lane details for particular zone")
	private LaneResponse lane;

	public CameraImageCoordinateResponse(Long id, BigDecimal aPointXCoordinate, BigDecimal aPointYCoordinate,
			BigDecimal bPointXCoordinate, BigDecimal bPointYCoordinate, BigDecimal cPointXCoordinate,
			BigDecimal cPointYCoordinate, BigDecimal dPointXCoordinate, BigDecimal dPointYCoordinate,
			ImageCoordinateType imageCoordinateType, BigDecimal maxDistance, Double roadLength, Double roadBreadth,
			Integer sequence, Date createdAt, Long createdById) {
		super();
		this.id = id;
		this.aPointXCoordinate = aPointXCoordinate;
		this.aPointYCoordinate = aPointYCoordinate;
		this.bPointXCoordinate = bPointXCoordinate;
		this.bPointYCoordinate = bPointYCoordinate;
		this.cPointXCoordinate = cPointXCoordinate;
		this.cPointYCoordinate = cPointYCoordinate;
		this.dPointXCoordinate = dPointXCoordinate;
		this.dPointYCoordinate = dPointYCoordinate;
		this.imageCoordinateType = imageCoordinateType;
		this.maxDistance = maxDistance;
		this.roadLength = roadLength;
		this.roadBreadth = roadBreadth;
		this.sequence = sequence;
		this.createdAt = createdAt;
		this.createdById = createdById;
	}

	public Long getId() {
		return mask(id);
	}

	public void setId(Long id) {
		this.id = id;
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

	public ImageCoordinateType getImageCoordinateType() {
		return imageCoordinateType;
	}

	public void setImageCoordinateType(ImageCoordinateType imageCoordinateType) {
		this.imageCoordinateType = imageCoordinateType;
	}

	public BigDecimal getMaxDistance() {
		return maxDistance;
	}

	public void setMaxDistance(BigDecimal maxDistance) {
		this.maxDistance = maxDistance;
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

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Long getCreatedById() {
		return createdById;
	}

	public void setCreatedById(Long createdById) {
		this.createdById = createdById;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public static CameraImageCoordinateResponse get(CameraImageCoordinate cameraImageCoordinate) {
		if (cameraImageCoordinate != null) {
			CameraImageCoordinateResponse response = new CameraImageCoordinateResponse(cameraImageCoordinate.getId(),
					cameraImageCoordinate.getaPointXCoordinate(), cameraImageCoordinate.getaPointYCoordinate(),
					cameraImageCoordinate.getbPointXCoordinate(), cameraImageCoordinate.getbPointYCoordinate(),
					cameraImageCoordinate.getcPointXCoordinate(), cameraImageCoordinate.getcPointYCoordinate(),
					cameraImageCoordinate.getdPointXCoordinate(), cameraImageCoordinate.getdPointYCoordinate(),
					cameraImageCoordinate.getImageCoordinateType(), cameraImageCoordinate.getMaxDistance(),
					cameraImageCoordinate.getRoadLength(), cameraImageCoordinate.getRoadBreadth(),
					cameraImageCoordinate.getSequence(), cameraImageCoordinate.getCreatedAt(),
					cameraImageCoordinate.getCreatedBy().getId());
			return response;
		}
		return null;
	}

	public LaneResponse getLane() {
		return lane;
	}

	public void setLane(LaneResponse lane) {
		this.lane = lane;
	}
}
