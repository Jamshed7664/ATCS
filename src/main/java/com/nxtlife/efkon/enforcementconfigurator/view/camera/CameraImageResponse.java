package com.nxtlife.efkon.enforcementconfigurator.view.camera;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.v3.oas.annotations.media.Schema;

@JsonInclude(value = Include.NON_ABSENT)
public class CameraImageResponse {

	@Schema(example = "1")
	private String id;

	@Schema(example = "http://www.efkon.com/image.jpg")
	private String imageUrl;

	@Schema(example = "image.jpg")
	private String imageName;

	@Schema(example = "120")
	private Long imageSize;

	@Schema(example = "application/octet-stream")
	private String imageType;

	private byte[] imageData;

	public CameraImageResponse(String id, String imageUrl, String imageName, Long imageSize, String imageType) {
		super();
		this.id = id;
		this.imageUrl = imageUrl;
		this.imageName = imageName;
		this.imageSize = imageSize;
		this.imageType = imageType;
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

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

}
