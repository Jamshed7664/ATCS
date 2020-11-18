package com.nxtlife.efkon.enforcementconfigurator.entity.camera;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.nxtlife.efkon.enforcementconfigurator.entity.common.BaseEntity;

@SuppressWarnings("serial")
@Entity
@Table(name = "camera_image")
@DynamicUpdate(value = true)
@DynamicInsert(value = true)
public class CameraImage extends BaseEntity implements Serializable {

	@Id
	private String id;

	@Column(name = "url", unique = true)
	private String imageUrl;

	@NotNull(message = "image name can't be null")
	private String imageName;

	@NotNull(message = "image size can't be null")
	private Long imageSize;

	@Column(name = "image_type")
	private String imageType;

	@OneToOne
	private Camera camera;

	public CameraImage() {
		super();
	}

	public CameraImage(String id, String imageUrl, @NotNull(message = "image name can't be null") String imageName,
			@NotNull(message = "image size can't be null") Long imageSize, String imageType) {
		super();
		this.id = id;
		this.imageUrl = imageUrl;
		this.imageName = imageName;
		this.imageSize = imageSize;
		this.imageType = imageType;
		if (id != null) {
			this.camera = new Camera();
			this.camera.setId(id);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Long getImageSize() {
		return imageSize;
	}

	public void setImageSize(Long imageSize) {
		this.imageSize = imageSize;
	}

	public String getImageType() {
		return imageType;
	}

	public void setImageType(String imageType) {
		this.imageType = imageType;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}
}
