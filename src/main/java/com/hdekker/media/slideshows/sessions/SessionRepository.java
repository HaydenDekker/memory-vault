package com.hdekker.media.slideshows.sessions;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hdekker.EventProvider;
import com.hdekker.HasEventProvider;
import com.hdekker.UserEventProvider;
import com.hdekker.database.DynamoRepository;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.users.UserConfig;

@Repository
public class SessionRepository implements DynamoRepository<SSSession>, 
												HasEventProvider<SessionEvent>{

	@Autowired
	UserConfig config;
	
	@Override
	public String getUserName() {
		return config.getUserName();
	}
	
	@Autowired
	DynamoDBMapper mapper;

	@Override
	public DynamoDBMapper getMapper() {
		return mapper;
	}

	@Override
	public Class<SSSession> getObjectClass() {
		return SSSession.class;
	}
	
	
	UserEventProvider<SessionEvent> eventProvider = new UserEventProvider<SessionEvent>(){
		@Override
		public String getUserName() {
			return config.getUserName();
		}
		
	};

	@Override
	public EventProvider<SessionEvent> getEventProvider() {
		return eventProvider;
	}

	public List<SSSession> allSessionsWithController(String sortKey) {
		
		// TODO need to create a secondary index to replace this horrible operation
		List<SSSession> seshs = findAll();
		return seshs.stream().filter(f->f.getControllerSortKey().equals(sortKey)).collect(Collectors.toList());
		
	}

	public List<SSSession> allSessionsWithDisplay(MediaDisplayComponent mdc) {
		
		// TODO need to create a secondary index to replace this horrible operation
		// Will likely only be one for a while
		List<SSSession> seshs = findAll();
		return seshs.stream().filter(f->mdc.getSortKey().equals(f.getMediaDisplayComponentSortKey())).collect(Collectors.toList());
		
		
	}

}
