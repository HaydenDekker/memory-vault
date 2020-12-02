package com.hdekker.media.slideshows.definition;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.hdekker.EventProvider;
import com.hdekker.HasEventProvider;
import com.hdekker.UserEventProvider;
import com.hdekker.database.DynamoRepository;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.users.UserConfig;

@Repository
public class DefinitionsRepository implements DynamoRepository<Definition>, 
														HasEventProvider<DefinitionEvent>{

	// TODO userName needs to associate with a set of credentials
	@Autowired
	UserConfig config;
	
	UserEventProvider<DefinitionEvent> definitionEventProvider = new UserEventProvider<DefinitionEvent>(){
		@Override
		public String getUserName() {
			return config.getUserName();
		}
		
	};
	
	
	@Autowired
	DynamoDBMapper dynamoDBMapper;
	
	// no longer using event sourcing.
//	public void createDefinition(String userName, String definitionName, String eventSource) {
//		
//		String key = DynamoDBUtils.getKeyForEvent(userName)
//					.apply(PrimaryKeyApplicationEventTypes.DefinitionEvent);
//		
//		// check event version
//		VersionDetails sortKey = new VersionDetails();
//		sortKey.setKey(key);
//		sortKey.setSortKey("v_0");
//		Optional<VersionDetails> currentVersion = Optional.ofNullable(dynamoDBMapper.load(sortKey));
//		
//		// log event
//		DefinitionEvent de = new DefinitionEvent();
//		de.setKey(key);
//		String newVersion = currentVersion
//			.map((v)-> DynamoDBUtils.incrementSortKey("v_").apply(v.getCurrentKey()))
//			.orElse("v_1");
//		de.setSortKey(newVersion);
//		de.setEventSource(eventSource);
//		de.setData(definitionName);
//		de.setPreviousData("");
//		de.setEventDateTime(LocalDateTime.now());
//		de.setType(DefinitionEventType.Created);
//		
//		dynamoDBMapper.save(de);
//		
//		// update version info
//		sortKey.setCurrentKey(newVersion);
//		dynamoDBMapper.save(sortKey);
//		
//	}
	
//	public Definition getDefinition(String key, String sortKey) {
//		
//		return dynamoDBMapper.load(Definition.class, key, sortKey);
//		
//	}

	/**
	 * Used by repository interfaces
	 * 
	 */
	@Override
	public DynamoDBMapper getMapper() {
		return dynamoDBMapper;
	}

	@Override
	public String getUserName() {
		
		return config.getUserName();
	}

	@Override
	public Class<Definition> getObjectClass() {
		return Definition.class;
	}

	@Override
	public EventProvider<DefinitionEvent> getEventProvider() {
		return definitionEventProvider;
	}

	
//	public List<Definition> getAllDefinitions(String userName) {
//		
//		String key = DynamoDBUtils.getKeyForEvent(userName)
//		.apply(PrimaryKeyApplicationEventTypes.DefinitionEvent);
//		
//		DynamoDBQueryExpression<DefinitionEvent> query = new DynamoDBQueryExpression<DefinitionEvent>();
//		query.setKeyConditionExpression("key = :" + key);
//		
//		HashMap<String, Boolean> map = new HashMap<>();
//		
//		PaginatedQueryList<DefinitionEvent> result = dynamoDBMapper.query(DefinitionEvent.class, query);
//		return result.stream()
//				.flatMap((de) -> {
//					
//					switch(de.getType()) {
//						case Created:{
//							SimpleEntry<String, Boolean> entry = new AbstractMap.SimpleEntry<String, Boolean>("", true);
//							return Stream.of(entry);
//						}
//						case Deleted: {
//							
//						}
//						case NameChange:{
//							
//						}
//						default : Stream.empty();
//					}
//					return Stream.of(new AbstractMap.SimpleEntry<String, Boolean>("", true));
//				})
//				.collect(Collectors.toMap((el)-> el.getKey(), (el)-> el.getValue()))
//				.entrySet().stream()
//				.filter((set)->set.getValue().equals(true))
//				.map((set)-> {
//					Definition def = new Definition();
//					def.setDefinitionName(set.getKey());
//					return def;
//				})
//				.collect(Collectors.toList());
//	}
	
}
