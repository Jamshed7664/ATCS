package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.nxtlife.efkon.enforcementconfigurator.entity.camera.CameraImageCoordinate;
import com.nxtlife.efkon.enforcementconfigurator.view.Request;

import io.swagger.v3.oas.annotations.media.Schema;

public class CameraImageCoordinateRequest implements Request {

	@Schema(description = "Id of the camera image coordinate", example = "1")
	private Long id;

	@Schema(description = "Lane id will be required at the time of zone mapping with lane otherwise lane id won't be used", example = "1")
	private String laneId;

	@NotNull(message = "A Point X coordinate can't be null")
	@Schema(name = "aPointXCoordinate", description = "A Point X coordinate ", example = "123.12")
	private BigDecimal aPointXCoordinate;

	@NotNull(message = "A Point Y coordinate can't be null")
	@Schema(name = "aPointYCoordinate", description = "A Point Y coordinate ", example = "125.12")
	private BigDecimal aPointYCoordinate;

	@NotNull(message = "B Point X coordinate can't be null")
	@Schema(name = "bPointXCoordinate", description = "B Point X coordinate ", example = "123.15")
	private BigDecimal bPointXCoordinate;

	@NotNull(message = "B Point Y coordinate can't be null")
	@Schema(name = "bPointYCoordinate", description = "B Point Y coordinate ", example = "123.12")
	private BigDecimal bPointYCoordinate;

	@NotNull(message = "C Point X coordinate can't be null")
	@Schema(name = "cPointXCoordinate", description = "C Point X coordinate ", example = "123.11")
	private BigDecimal cPointXCoordinate;

	@NotNull(message = "C Point Y coordinate can't be null")
	@Schema(name = "cPointYCoordinate", description = "C Point Y coordinate ", example = "123.12")
	private BigDecimal cPointYCoordinate;

	@NotNull(message = "D Point X coordinate can't be null")
	@Schema(name = "dPointXCoordinate", description = "D Point X coordinate ", example = "123.12")
	private BigDecimal dPointXCoordinate;

	@NotNull(message = "D Point Y coordinate can't be null")
	@Schema(name = "dPointYCoordinate", description = "D Point Y coordinate ", example = "123.12")
	private BigDecimal dPointYCoordinate;

	@Schema(description = "Type of the image coordinate", example = "RED_LAMP", allowableValues = { "INCIDENT_DETECTION", "RED_LAMP",
			"ROAD_ALIGNMENT" })
	private String imageCoordinateType;

	public CameraImageCoordinate toEntity() {
		CameraImageCoordinate cameraImageCoordinate = new CameraImageCoordinate();
		cameraImageCoordinate.setaPointXCoordinate(aPointXCoordinate);
		cameraImageCoordinate.setaPointYCoordinate(aPointYCoordinate);
		cameraImageCoordinate.setbPointXCoordinate(bPointXCoordinate);
		cameraImageCoordinate.setbPointYCoordinate(bPointYCoordinate);
		cameraImageCoordinate.setcPointXCoordinate(cPointXCoordinate);
		cameraImageCoordinate.setcPointYCoordinate(cPointYCoordinate);
		cameraImageCoordinate.setdPointXCoordinate(dPointXCoordinate);
		cameraImageCoordinate.setdPointYCoordinate(dPointYCoordinate);
		return cameraImageCoordinate;

	}

	public Long getId() {
		return id;
	}

	public String getLaneId() {
		return laneId;
	}

	public BigDecimal getaPointXCoordinate() {
		return aPointXCoordinate;
	}

	public BigDecimal getaPointYCoordinate() {
		return aPointYCoordinate;
	}

	public BigDecimal getbPointXCoordinate() {
		return bPointXCoordinate;
	}

	public BigDecimal getbPointYCoordinate() {
		return bPointYCoordinate;
	}

	public BigDecimal getcPointXCoordinate() {
		return cPointXCoordinate;
	}

	public BigDecimal getcPointYCoordinate() {
		return cPointYCoordinate;
	}

	public BigDecimal getdPointXCoordinate() {
		return dPointXCoordinate;
	}

	public BigDecimal getdPointYCoordinate() {
		return dPointYCoordinate;
	}

	public String getImageCoordinateType() {
		return imageCoordinateType;
	}
}
