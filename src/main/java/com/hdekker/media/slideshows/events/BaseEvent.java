package com.hdekker.media.slideshows.events;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.hdekker.SSDynamoDBLocalDateTimeTypeConverter;

@DynamoDBTable(tableName = "BaseEvent")
public class BaseEvent {
	
	String key;
	
	@DynamoDBHashKey
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	String sortKey;

	@DynamoDBRangeKey
	public String getSortKey() {
		return sortKey;
	}

	public void setSortKey(String sortKey) {
		this.sortKey = sortKey;
	}
	
	LocalDateTime eventDateTime;

	@DynamoDBTypeConverted(converter = SSDynamoDBLocalDateTimeTypeConverter.class)
	public LocalDateTime getEventDateTime() {
		return eventDateTime;
	}

	public void setEventDateTime(LocalDateTime eventDateTime) {
		this.eventDateTime = eventDateTime;
	}
	
	String eventSource;
	
	@DynamoDBAttribute
	public String getEventSource() {
		return eventSource;
	}

	public void setEventSource(String eventSource) {
		this.eventSource = eventSource;
	}
	
}
