package com.hdekker.media.slideshows.data;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.hdekker.database.DynamoObject;

@DynamoDBTable(tableName = "memory-vault")
public class Controller extends DynamoObject{

	String controllerName;
	List<String> mediaDisplayComponentKeys;
	
	@DynamoDBAttribute
	public String getControllerName() {
		return controllerName;
	}
	public void setControllerName(String controllerName) {
		this.controllerName = controllerName;
	}
	
	@DynamoDBAttribute
	public List<String> getMediaDisplayComponentKeys() {
		return mediaDisplayComponentKeys;
	}
	public void setMediaDisplayComponentKeys(List<String> mediaDisplayComponentKeys) {
		this.mediaDisplayComponentKeys = mediaDisplayComponentKeys;
	}
	
	
	
}
