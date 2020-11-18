package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nxtlife.efkon.enforcementconfigurator.entity.camera.Camera;
import com.nxtlife.efkon.enforcementconfigurator.enums.CameraType;

import io.swagger.v3.oas.annotations.media.Schema;

public class CameraRequest {

	@Schema(description = "username for access camera", example = "camera")
	@NotNull(message = "username can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@Schema(description = "password for access camera", example = "abc123")
	@NotNull(message = "password can't be null")
	private String password;

	@Schema(description = "ip address of the cam", example = "192.1.1.0")
	@Pattern(regexp = "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.|$)){4}", message = "invalid ip address")
	@NotEmpty(message = "ip address can't be null")
	private String ipAddress;

	@Schema(description = "rtsp for access camera", example = "rtsp://username:password@ip")
	@NotNull(message = "rtsp can't be null")
	private String rtsp;

	@Schema(description = "It tells about camera type that it's front or back cam", example = "FRONT", allowableValues = "FRONT,REAR")
	@NotNull(message = "camera type can't be null")
	private String cameraType;

	@Schema(description = "Frequency per second", example = "1")
	private Long fps;

	@Schema(description = "Frame Width", example = "1")
	private Long frameWidth;

	@Schema(description = "Frame Height", example = "1")
	private Long frameHeight;

	@Schema(description = "Scale Factor", example = "1.1")
	private Double scaleFactor;

	private String codecs;

	@JsonIgnore(value = true)
	@Schema(description = "Equipment name will be set in this code", hidden = true)
	private String code;

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public String getRtsp() {
		return rtsp;
	}

	public String getCameraType() {
		return cameraType;
	}

	public Long getFps() {
		return fps;
	}

	public Long getFrameWidth() {
		return frameWidth;
	}

	public Long getFrameHeight() {
		return frameHeight;
	}

	public Double getScaleFactor() {
		return scaleFactor;
	}

	public String getCodecs() {
		return codecs;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Camera toEntity() {
		Camera camera = new Camera();
		camera.setCodecs(codecs);
		camera.setFps(fps);
		camera.setFrameHeight(frameHeight);
		camera.setFrameWidth(frameWidth);
		camera.setIpAddress(ipAddress);
		camera.setUsername(username);
		camera.setPassword(password);
		camera.setCode(code);
		if (rtsp == null) {
			camera.setRtsp(String.format("rtsp://%s:%s@%s", username, password, ipAddress));
		}else {
			camera.setRtsp(rtsp);
		}
		camera.setScaleFactor(scaleFactor);
		camera.setCameraType(CameraType.valueOf(cameraType));
		return camera;
	}
}
