package com.hdekker.media.slideshows.data;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBConvertedBool;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConvertedEnum;
import com.hdekker.SSDynamoDBLocalDateTimeTypeConverter;
import com.hdekker.database.DynamoObject;
import com.hdekker.media.slideshows.sessions.SlideShowSessionEventType;

/**
 * The session holds the state for the
 * slide show based on the current browser.
 * It ensure browsers remain in sync.
 * 
 * @author HDekker
 *
 */
@DynamoDBTable(tableName = "memory-vault")
public class SSSession extends DynamoObject{

	String name;
	String slideShowDefinitionSortKey;
	String mediaDisplayComponentSortKey;
	String controllerSortKey;
	
	Boolean isActive;
	Enum<SlideShowSessionEventType> sessionState;
	
	@DynamoDBTypeConvertedEnum
	public Enum<SlideShowSessionEventType> getSessionState() {
		return sessionState;
	}
	public void setSessionState(Enum<SlideShowSessionEventType> sessionState) {
		this.sessionState = sessionState;
	}
	@DynamoDBAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
		
	@DynamoDBAttribute
	public String getSlideShowDefinitionSortKey() {
		return slideShowDefinitionSortKey;
	}
	public void setSlideShowDefinitionSortKey(String slideShowDefinitionSortKey) {
		this.slideShowDefinitionSortKey = slideShowDefinitionSortKey;
	}
	
	@DynamoDBAttribute
	public String getMediaDisplayComponentSortKey() {
		return mediaDisplayComponentSortKey;
	}
	public void setMediaDisplayComponentSortKey(String mediaDisplayComponentSortKey) {
		this.mediaDisplayComponentSortKey = mediaDisplayComponentSortKey;
	}
	
	@DynamoDBAttribute
	public String getControllerSortKey() {
		return controllerSortKey;
	}
	public void setControllerSortKey(String controllerSortKey) {
		this.controllerSortKey = controllerSortKey;
	}
	
	@DynamoDBAttribute
	public Boolean getIsActive() {
		return isActive;
	}
	public void setIsActive(Boolean isActive) {
		this.isActive = isActive;
	}
		
}
