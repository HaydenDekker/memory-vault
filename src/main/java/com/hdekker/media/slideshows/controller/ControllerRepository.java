package com.hdekker.media.slideshows.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hdekker.EventProvider;
import com.hdekker.HasEventProvider;
import com.hdekker.UserEventProvider;
import com.hdekker.database.DynamoRepository;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.users.UserConfig;

@Repository
public class ControllerRepository implements DynamoRepository<Controller>,
									HasEventProvider<ControllerEvent>{

	@Autowired
	UserConfig userConfig;
	
	EventProvider<ControllerEvent> eventProvider = new UserEventProvider<ControllerEvent>() {

		@Override
		public String getUserName() {
			return userConfig.getUserName();
		}

		
	};

	@Override
	public EventProvider<ControllerEvent> getEventProvider() {
		return eventProvider;
	}
	
	@Autowired
	DynamoDBMapper mapper;
	
	@Override
	public String getUserName() {
		
		return userConfig.getUserName();
	}
	@Override
	public DynamoDBMapper getMapper() {
		
		return mapper;
	}
	@Override
	public Class<Controller> getObjectClass() {
		
		return Controller.class;
	}

}
