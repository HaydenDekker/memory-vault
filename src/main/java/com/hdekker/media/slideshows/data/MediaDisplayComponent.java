package com.hdekker.media.slideshows.data;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.hdekker.database.DynamoObject;

/**
 * Implemented by any component that is capable
 * of displaying a list of media and receiving
 * commands to control the way it displays the
 * content.
 * 
 * @author HDekker
 *
 */
@DynamoDBTable(tableName = "memory-vault")
public class MediaDisplayComponent extends DynamoObject {
	
	String displayName;

	@DynamoDBAttribute
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

}
