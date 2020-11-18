package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import java.util.Date;

import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nxtlife.efkon.enforcementconfigurator.enums.CameraType;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = JsonInclude.Include.NON_ABSENT)
public class CameraResponse {

	@Schema(defaultValue = "EQP00001")
	private String id;

	@Schema(description = "Created at of camera property")
	private Date createdAt;

	@Schema(description = "Created by of camera property")
	private Long createdById;

	@Schema(description = "username for access camera", defaultValue = "camera")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@Schema(description = "password for access camera", defaultValue = "abc123")
	private String password;

	@Schema(description = "ip address of the cam", defaultValue = "192.1.1.0")
	@Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}", message = "invalid ip address")
	private String ipAddress;

	@Schema(description = "rtsp for access camera", example = "rtsp://username:password@ip")
	private String rtsp;

	@Schema(description = "It tells about camera type that it's front or back cam", defaultValue = "FRONT")
	private CameraType cameraType;

	@Schema(description = "Code of the camera", example = "CAM 1")
	private String code;

	@Schema(description = "Frequency per second", defaultValue = "1")
	private Long fps;

	@Schema(description = "Frame Width", defaultValue = "1")
	private Long frameWidth;

	@Schema(description = "Frame Height", defaultValue = "1")
	private Long frameHeight;

	@Schema(description = "Scale Factor", defaultValue = "1.1")
	private Double scaleFactor;

	private String codecs;

	public CameraResponse(String id,
			@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20") String username,
			String password,
			@Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}", message = "invalid ip address") String ipAddress,
			String rtsp, CameraType cameraType, String code, Long fps, Long frameWidth, Long frameHeight,
			Double scaleFactor, String codecs, Date createdAt, Long createdById) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.ipAddress = ipAddress;
		this.cameraType = cameraType;
		this.code = code;
		this.fps = fps;
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.scaleFactor = scaleFactor;
		this.codecs = codecs;
		this.createdAt = createdAt;
		this.rtsp = rtsp;
		this.createdById = createdById;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getRtsp() {
		return rtsp;
	}

	public void setRtsp(String rtsp) {
		this.rtsp = rtsp;
	}

	public CameraType getCameraType() {
		return cameraType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public Long getFps() {
		return fps;
	}

	public void setFps(Long fps) {
		this.fps = fps;
	}

	public Long getFrameWidth() {
		return frameWidth;
	}

	public void setFrameWidth(Long frameWidth) {
		this.frameWidth = frameWidth;
	}

	public Long getFrameHeight() {
		return frameHeight;
	}

	public void setFrameHeight(Long frameHeight) {
		this.frameHeight = frameHeight;
	}

	public Double getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(Double scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public String getCodecs() {
		return codecs;
	}

	public void setCodecs(String codecs) {
		this.codecs = codecs;
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

}
