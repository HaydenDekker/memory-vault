package com.hdekker.media.slideshows.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBDocument;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.hdekker.media.slideshows.SlideShowMediaType;

@DynamoDBDocument
public class Media {
	
	String key;
	SlideShowMediaType mediaType;
	
	public Media() {
		
	}

	public Media(String key, SlideShowMediaType mediaType) {
		super();
		this.key = key;
		this.mediaType = mediaType;

	}


	@DynamoDBAttribute
	@DynamoDBTypeConvertedEnum
	public SlideShowMediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(SlideShowMediaType mediaType) {
		this.mediaType = mediaType;
	}

	@DynamoDBAttribute
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}