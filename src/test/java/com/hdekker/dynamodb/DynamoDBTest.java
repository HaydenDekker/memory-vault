package com.hdekker.dynamodb;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DynamoDBTest {
	
	//@Autowired
	//AmazonDynamoDB dynamoDB;
	
	//DynamoDBMapper dynamoDBMapper;
	
	
	
	Logger log = LoggerFactory.getLogger(DynamoDBTest.class);
	
//	@Test
//	public void canConnectToDynamoAndCreateTable() {
//		
//		dynamoDBMapper = new DynamoDBMapper(dynamoDB);
//        
//        CreateTableRequest tableRequest = dynamoDBMapper
//          .generateCreateTableRequest(DynamoEvent.class);
//        tableRequest.setProvisionedThroughput(
//          new ProvisionedThroughput(1L, 1L));
//        
//        try {
//            CreateTableResult result = dynamoDB.createTable(tableRequest);
//            System.out.println(result.getTableDescription().getTableName());
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//        }
//	}
	
//	@Test
//	public void createEventTable() {
//		
//		dynamoDBMapper = new DynamoDBMapper(dynamoDB);
//        
//        CreateTableRequest tableRequest = dynamoDBMapper
//          .generateCreateTableRequest(DynamoEvent.class);
//        tableRequest.setProvisionedThroughput(
//          new ProvisionedThroughput(1L, 1L));
//        
//        try {
//        	CreateTableResult result = dynamoDB.createTable(tableRequest);
//            System.out.println(result.getTableDescription().getTableName());
//        } catch (AmazonServiceException e) {
//            System.err.println(e.getErrorMessage());
//        }
//		
//	}
	
//	@Test
//	public void writeTestObjectToMemoryVaultEventTable() {
//		
//		DynamoDB db = new DynamoDB(dynamoDB);
//		
//		DefinitionEvent de = new DefinitionEvent();
//		
//        DynamoEvent<DefinitionEvent> dynE = new DynamoEvent<>();
//        dynE.setEventId("hayden-dekker/definition-desc/Our Boy");
//    
//        dynE.setObjectClassName("DefinitionEvent");
//        dynE.setEventDateTime(LocalDateTime.now());
//        dynE.setEventType(EventType.Added);
//        dynE.setEventData(de);
//        
//        //repo.save(dynE);
//        
//       // DynamoDBMapperConfig config = DynamoDBMapperConfig.builder().wit
//        
//        
////        Table table = db.getTable("memory-vault");
////        
////        Item item = new Item();
////        PutItemSpec spec = new PutItemSpec();
//        //table.putItem(spec)
//        
//	}
	
//	@Test
//	public void readTestObjectFromMemoryVaultEventTable() {
//		
//		DynamoEventId eventId = new DynamoEventId();
//		eventId.setEventId("hayden-dekker/definition-desc/Our Boy");
//		eventId.setVersion(2);
//		
//		Optional<DynamoEvent<DefinitionEvent>> event = repo.findById(eventId);
//		event.ifPresent(e-> log.info(e.getEventData().toString()));
//		
//		ObjectMapper om = new ObjectMapper();
//		DefinitionEvent eventOutput = om.convertValue(event.get().getEventData(), DefinitionEvent.class);
//		assertTrue(eventOutput!=null);
//		log.info("Event object is " + eventOutput.getDefinitionDescriptor().getSlideShowName());
//	}
//	
	@Test
	public void twoObjectsSingleTable() {
		
//		dynamoDBMapper = new DynamoDBMapper(dynamoDB);
//		
//		TestObjectOne t1 = new TestObjectOne();
//		t1.setKey("testObj");
//		t1.setSortKey("t1_v0");
//		t1.setValue("This is a value attribute");
//		
//		TestObjectTwo t2 = new TestObjectTwo();
//		t2.setKey("testObj");
//		t2.setSortKey("t2_v2");
//		t2.setName("This is a name attribute");
//
//		TestObjectTwo t3 = new TestObjectTwo();
//		t3.setKey("testObj");
//		t3.setSortKey("t2_v3");
//		t3.setName("This is a name attribute");
//		
//		dynamoDBMapper.save(t1);
//		dynamoDBMapper.save(t2);
//		dynamoDBMapper.save(t3);
		
	}
	
	@Test
	public void getAllTestObjects() {
		
		
		
	}
	
}
