package com.hdekker.media.slideshows.data;

import java.util.List;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.hdekker.database.DynamoObject;

@DynamoDBTable(tableName = "memory-vault")
public class Definition extends DynamoObject{

	String definitionName;
	String definitionDescription;
	List<Media> media;
	
	@DynamoDBAttribute
	public String getDefinitionName() {
		return definitionName;
	}
	public void setDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}
	
	@DynamoDBAttribute
	public String getDefinitionDescription() {
		return definitionDescription;
	}
	public void setDefinitionDescription(String definitionDescription) {
		this.definitionDescription = definitionDescription;
	}
	
	@DynamoDBAttribute
	public List<Media> getMedia() {
		return media;
	}
	public void setMedia(List<Media> media) {
		this.media = media;
	}

	
	
	
}
