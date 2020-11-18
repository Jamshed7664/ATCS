package com.nxtlife.efkon.enforcementconfigurator.entity.camera;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;
import com.nxtlife.efkon.enforcementconfigurator.entity.equipment.Equipment;
import com.nxtlife.efkon.enforcementconfigurator.enums.CameraType;

@SuppressWarnings("serial")
@Entity
@Table(name = "camera")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class Camera extends BaseEntity implements Serializable {

	@Id
	private String id;

	@NotNull(message = "username can't be null")
	@Pattern(regexp = "^[@A-Za-z0-9_]{3,20}$", message = "username should contains only alphabets/digit/@ and length should be in between 4 to 20")
	private String username;

	@NotNull(message = "password can't be null")
	private String password;

	@Column(name = "ip_address")
	private String ipAddress;

	@Column(name = "rtsp", unique = true)
	private String rtsp;

	@Enumerated(EnumType.STRING)
	@Column(name = "camera_type")
	private CameraType cameraType;

	@Column(name = "code", unique = true)
	private String code;

	private Long fps;

	private Long frameWidth;

	private Long frameHeight;

	private Double scaleFactor;

	private String codecs;

	private Boolean isRedSignalDetect = false;

	private Boolean isIncidentDetect = false;

	private Boolean isRoadAlignment = false;

	private Integer incidentScheduling = 0;

	@Column(name = "blinking_ts_count")
	private Integer blinkingTsCount;

	@OneToOne
	private Equipment equipment;

	@OneToOne(cascade = CascadeType.ALL, mappedBy = "camera")
	private CameraImage cameraImage;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "camera")
	private List<CameraIncident> cameraIncidents;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		if (id != null) {
			this.equipment = new Equipment();
			this.equipment.setId(id);
		}
		this.id = id;
	}

	public Boolean getIsRedSignalDetect() {
		return isRedSignalDetect;
	}

	public void setIsRedSignalDetect(Boolean isRedSignalDetect) {
		this.isRedSignalDetect = isRedSignalDetect;
	}

	public Boolean getIsIncidentDetect() {
		return isIncidentDetect;
	}

	public void setIsIncidentDetect(Boolean isIncidentDetect) {
		this.isIncidentDetect = isIncidentDetect;
	}

	public Boolean getIsRoadAlignment() {
		return isRoadAlignment;
	}

	public void setIsRoadAlignment(Boolean isRoadAlignment) {
		this.isRoadAlignment = isRoadAlignment;
	}

	public Integer getBlinkingTsCount() {
		return blinkingTsCount;
	}

	public void setBlinkingTsCount(Integer blinkingTsCount) {
		this.blinkingTsCount = blinkingTsCount;
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

	public void setCameraType(CameraType cameraType) {
		this.cameraType = cameraType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public CameraImage getCameraImage() {
		return cameraImage;
	}

	public void setCameraImage(CameraImage cameraImage) {
		this.cameraImage = cameraImage;
	}

	public List<CameraIncident> getCameraIncidents() {
		return cameraIncidents;
	}

	public void setCameraIncidents(List<CameraIncident> cameraIncidents) {
		this.cameraIncidents = cameraIncidents;
	}

	public Integer getIncidentScheduling() {
		return incidentScheduling;
	}

	public void setIncidentScheduling(Integer incidentScheduling) {
		this.incidentScheduling = incidentScheduling;
	}
}
