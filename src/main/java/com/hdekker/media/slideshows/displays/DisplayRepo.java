package com.hdekker.media.slideshows.displays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hdekker.EventProvider;
import com.hdekker.HasEventProvider;
import com.hdekker.UserEventProvider;
import com.hdekker.database.DynamoRepository;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.users.UserConfig;

@Repository
public class DisplayRepo implements DynamoRepository<MediaDisplayComponent>, HasEventProvider<DisplayEvent>{

	@Autowired
	UserConfig userConfig;
	
	EventProvider<DisplayEvent> eventProvider = new UserEventProvider<DisplayEvent>() {

		@Override
		public String getUserName() {
			
			return userConfig.getUserName();
		};
	};

	@Override
	public EventProvider<DisplayEvent> getEventProvider() {
		return eventProvider;
	}

	@Override
	public String getUserName() {
		return userConfig.getUserName();
	}
	
	@Autowired
	DynamoDBMapper mapper;

	@Override
	public DynamoDBMapper getMapper() {
		
		return mapper;
	}

	@Override
	public Class<MediaDisplayComponent> getObjectClass() {
		return MediaDisplayComponent.class;
	}

}
