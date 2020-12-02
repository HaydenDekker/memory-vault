package com.hdekker;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.handlers.AsyncHandler;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.event.S3EventNotification;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.CreateTopicRequest;
import com.amazonaws.services.sns.model.CreateTopicResult;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@WebAppConfiguration
public class AWSS3TopicNotifications {

	Logger log = LoggerFactory.getLogger(AWSS3TopicNotifications.class);
	
	String sqsTestTopic = "sqsTestTopic";
	
	@Test
	public void createSNSTopic() {
		
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
		
		// Create an Amazon SNS topic.
		final CreateTopicRequest createTopicRequest = new CreateTopicRequest("memory-vault");
		final CreateTopicResult createTopicResponse = snsClient.createTopic(createTopicRequest);

		// Print the topic ARN.
		System.out.println("TopicArn:" + createTopicResponse.getTopicArn());
		    
		// Print the request ID for the CreateTopicRequest action.
		System.out.println("CreateTopicRequest: " + snsClient.getCachedResponseMetadata(createTopicRequest));
		
	}
	
	@Test
	public void createSQSTopic() {
		
		AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
		log.info("the queue created is " + sqsClient.createQueue(sqsTestTopic).getQueueUrl());
		
	}
	
	@Test
	public void subScribeTestToMemoryVault() {
		
//		AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
//		CreateQueueResult createClientResponse = sqsClient.createQueue(sqsTestTopic);
//		
//		AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
//		// Create an Amazon SNS topic.
//		final CreateTopicRequest createTopicRequest = new CreateTopicRequest("memory-vault");
//		final CreateTopicResult createTopicResponse = snsClient.createTopic(createTopicRequest);
//
//		
//		sqsClient.addPermission(createClientResponse.getQueueUrl(), "mem-topicS", aWSAccountIds, actions)
//		snsClient.subscribe(createTopicResponse.getTopicArn(), protocol, endpoint)
	}
	
	@Test
	public void createNotificationConfigurationForBucket() {
		
//		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
//	    
//		TopicConfiguration conf = new TopicConfiguration("arn:aws:sns:ap-southeast-2:440654054855:memory-vault", S3Event.ObjectCreated.toString());
//		BucketNotificationConfiguration config = new BucketNotificationConfiguration("created-events", conf);
//		s3.setBucketNotificationConfiguration("memory-vault", config);

	}
	/**
	 * https://docs.aws.amazon.com/AWSSimpleQueueService/latest/SQSDeveloperGuide/working-with-messages.html#setting-up-long-polling
	 * 
	 */
	@Test
	public void subscribesAndRecievesEvent() {
		
		fail();

		AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
		Future<ReceiveMessageResult> message = sqsClient.receiveMessageAsync("memory-vault", new AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult>() {
			
			@Override
			public void onSuccess(ReceiveMessageRequest request, ReceiveMessageResult result) {
				
				log.info("Success!!!" + result.getMessages().toString());
			}
			
			@Override
			public void onError(Exception exception) {
				
				log.info("Error");
			}
			
		});
		
		try {
			message.get();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			// 
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	};
	
	@Test
	public void springAWSSQSSubscription() {
		
		
	}
	
	@Autowired
	AWSMessageSubscriber conf;
	
	NotificationMessagingTemplate te;
	
	@Test
	public void jacksonMapperTestSNSObject() {
		
		String test = "{\r\n  \"Type\" : \"Notification\",\r\n  \"MessageId\" : \"502b9eee-c702-51f1-bada-c9584a1b833b\",\r\n  \"TopicArn\" : \"arn:aws:sns:ap-southeast-2:440654054855:memory-vault\",\r\n  \"Subject\" : \"Amazon S3 Notification\",\r\n  \"Message\" : \"{\\\"Service\\\":\\\"Amazon S3\\\",\\\"Event\\\":\\\"s3:TestEvent\\\",\\\"Time\\\":\\\"2020-08-19T02:13:45.482Z\\\",\\\"Bucket\\\":\\\"memory-vault\\\",\\\"RequestId\\\":\\\"4460F7F17E602B69\\\",\\\"HostId\\\":\\\"jyQrK7fjCZqyf1vEhBmaHSBwAdyz7bB85xFlnsK1LIlbKXRkAvjLwJ8z6CdHStCQX1sJ/3QJfX4=\\\"}\",\r\n  \"Timestamp\" : \"2020-08-19T02:13:45.505Z\",\r\n  \"SignatureVersion\" : \"1\",\r\n  \"Signature\" : \"JgW6fyyM1deT+yEFH8G51anrG0rs1xPkLYDMtuCDPFV25IEYmBIQSjQEriygnCuoWFaGxsFcdK1lW9//IZ2PlSS87ivP2WETKIhTVzv0u+KuBBSQEPzwhz/emhqkBY79aMksIAeok9ArITBX3UN1O0pDXen0dEJ32t6KD0CZPs6qku9Tv30P2vg6zNhpMHKKP94dtyV5Y9E0zIkXR4SogEULbAW9zAo/Qd+fAfcBehf1yFzCZKxEGFUxqiw6kNloI93Ilvl5JoFNLLK60ujD9TcGFPE4CdK6ledS7xY6MMXD27aX88LSNMiMd4IqsYAG8gO6z+58o3N0YpRX/XRoOw==\",\r\n  \"SigningCertURL\" : \"https://sns.ap-southeast-2.amazonaws.com/SimpleNotificationService-a86cb10b4e1f29c941702d737128f7b6.pem\",\r\n  \"UnsubscribeURL\" : \"https://sns.ap-southeast-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:ap-southeast-2:440654054855:memory-vault:eb024649-d030-4fc6-b509-0025e43e16c8\"\r\n}";
		
		 ObjectMapper m = new ObjectMapper();
		    //S3EventNotification notification = null;
			try {
				JSONObject message = new JSONObject(test);
				log.info("Sweet converted " + message.toString() );
				
				Object messagestr = message.get("Message");
				log.info("this is the message text" + messagestr);
				
				fail(); // parse JSON does work
				S3EventNotification notification = S3EventNotification.parseJson(messagestr.toString());
				log.info("Sweet converted " + notification.getRecords().get(0).getEventName());
				
				
				//notification = m.readValue(test, S3EventNotification.class);
			} catch (JSONException e) {
				
				e.printStackTrace();
			}
				
		log.info(test);
	}
	
	@Test
	public void tryS3EventNotificationParseOnSNSObject() {
		
		String test = "{\r\n  \"Type\" : \"Notification\",\r\n  \"MessageId\" : \"502b9eee-c702-51f1-bada-c9584a1b833b\",\r\n  \"TopicArn\" : \"arn:aws:sns:ap-southeast-2:440654054855:memory-vault\",\r\n  \"Subject\" : \"Amazon S3 Notification\",\r\n  \"Message\" : \"{\\\"Service\\\":\\\"Amazon S3\\\",\\\"Event\\\":\\\"s3:TestEvent\\\",\\\"Time\\\":\\\"2020-08-19T02:13:45.482Z\\\",\\\"Bucket\\\":\\\"memory-vault\\\",\\\"RequestId\\\":\\\"4460F7F17E602B69\\\",\\\"HostId\\\":\\\"jyQrK7fjCZqyf1vEhBmaHSBwAdyz7bB85xFlnsK1LIlbKXRkAvjLwJ8z6CdHStCQX1sJ/3QJfX4=\\\"}\",\r\n  \"Timestamp\" : \"2020-08-19T02:13:45.505Z\",\r\n  \"SignatureVersion\" : \"1\",\r\n  \"Signature\" : \"JgW6fyyM1deT+yEFH8G51anrG0rs1xPkLYDMtuCDPFV25IEYmBIQSjQEriygnCuoWFaGxsFcdK1lW9//IZ2PlSS87ivP2WETKIhTVzv0u+KuBBSQEPzwhz/emhqkBY79aMksIAeok9ArITBX3UN1O0pDXen0dEJ32t6KD0CZPs6qku9Tv30P2vg6zNhpMHKKP94dtyV5Y9E0zIkXR4SogEULbAW9zAo/Qd+fAfcBehf1yFzCZKxEGFUxqiw6kNloI93Ilvl5JoFNLLK60ujD9TcGFPE4CdK6ledS7xY6MMXD27aX88LSNMiMd4IqsYAG8gO6z+58o3N0YpRX/XRoOw==\",\r\n  \"SigningCertURL\" : \"https://sns.ap-southeast-2.amazonaws.com/SimpleNotificationService-a86cb10b4e1f29c941702d737128f7b6.pem\",\r\n  \"UnsubscribeURL\" : \"https://sns.ap-southeast-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:ap-southeast-2:440654054855:memory-vault:eb024649-d030-4fc6-b509-0025e43e16c8\"\r\n}";
		
		 ObjectMapper m = new ObjectMapper();
		    //S3EventNotification notification = null;
			try {
				JSONObject message = new JSONObject(test);
				log.info("Sweet converted " + message.toString() );
				
				Object messagestr = message.get("Message");
				log.info("this is the message text" + messagestr);
				
				//S3EventNotification notification = S3EventNotificationRecord.parseJson(messagestr.toString());
				fail(); // wont detect event class as it looks for a record
				S3EventNotification notification = m.readValue(messagestr.toString(), S3EventNotification.class);
				log.info("Sweet converted " + notification.getRecords().get(0).getEventName());
				
				
				//notification = m.readValue(test, S3EventNotification.class);
			} catch (JSONException | JsonProcessingException e) {
				
				e.printStackTrace();
			}
		
		log.info(test);
		
	}
	
	@Test
	public void tryS3EventNotificationRecordParseOnSNSObject() {
		
		String test = "{\r\n  \"Type\" : \"Notification\",\r\n  \"MessageId\" : \"502b9eee-c702-51f1-bada-c9584a1b833b\",\r\n  \"TopicArn\" : \"arn:aws:sns:ap-southeast-2:440654054855:memory-vault\",\r\n  \"Subject\" : \"Amazon S3 Notification\",\r\n  \"Message\" : \"{\\\"Service\\\":\\\"Amazon S3\\\",\\\"Event\\\":\\\"s3:TestEvent\\\",\\\"Time\\\":\\\"2020-08-19T02:13:45.482Z\\\",\\\"Bucket\\\":\\\"memory-vault\\\",\\\"RequestId\\\":\\\"4460F7F17E602B69\\\",\\\"HostId\\\":\\\"jyQrK7fjCZqyf1vEhBmaHSBwAdyz7bB85xFlnsK1LIlbKXRkAvjLwJ8z6CdHStCQX1sJ/3QJfX4=\\\"}\",\r\n  \"Timestamp\" : \"2020-08-19T02:13:45.505Z\",\r\n  \"SignatureVersion\" : \"1\",\r\n  \"Signature\" : \"JgW6fyyM1deT+yEFH8G51anrG0rs1xPkLYDMtuCDPFV25IEYmBIQSjQEriygnCuoWFaGxsFcdK1lW9//IZ2PlSS87ivP2WETKIhTVzv0u+KuBBSQEPzwhz/emhqkBY79aMksIAeok9ArITBX3UN1O0pDXen0dEJ32t6KD0CZPs6qku9Tv30P2vg6zNhpMHKKP94dtyV5Y9E0zIkXR4SogEULbAW9zAo/Qd+fAfcBehf1yFzCZKxEGFUxqiw6kNloI93Ilvl5JoFNLLK60ujD9TcGFPE4CdK6ledS7xY6MMXD27aX88LSNMiMd4IqsYAG8gO6z+58o3N0YpRX/XRoOw==\",\r\n  \"SigningCertURL\" : \"https://sns.ap-southeast-2.amazonaws.com/SimpleNotificationService-a86cb10b4e1f29c941702d737128f7b6.pem\",\r\n  \"UnsubscribeURL\" : \"https://sns.ap-southeast-2.amazonaws.com/?Action=Unsubscribe&SubscriptionArn=arn:aws:sns:ap-southeast-2:440654054855:memory-vault:eb024649-d030-4fc6-b509-0025e43e16c8\"\r\n}";
		
		 ObjectMapper m = new ObjectMapper();
		    //S3EventNotification notification = null;
			try {
				JSONObject message = new JSONObject(test);
				log.info("Sweet converted " + message.toString() );
				
				Object messagestr = message.get("Message");
				log.info("this is the message text" + messagestr);
				
				//S3EventNotification notification = S3EventNotificationRecord.parseJson(messagestr.toString());
				fail(); // wont detect event class as it looks for a record
				S3EventNotificationRecord notification = m.readValue(messagestr.toString(), S3EventNotificationRecord.class);
				log.info("Sweet converted " + notification.getEventName());
				
				
				//notification = m.readValue(test, S3EventNotification.class);
			} catch (JSONException | JsonProcessingException e) {
				
				e.printStackTrace();
			}
			
		
		log.info(test);
		
		//Message
		
	}
	
	@Test
	public void writeToSNS() {
		
		AmazonSNS snsClient = AmazonSNSClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
		CreateTopicResult sns = snsClient.createTopic("memory-vault");
		
		PublishRequest pr = new PublishRequest();
		pr.setMessage("Test");
		pr.setTopicArn(sns.getTopicArn());
		snsClient.publish(pr);
		
		ReceiveMessageRequest rmr = new ReceiveMessageRequest("memory-vault");
		rmr.withWaitTimeSeconds(2);
		AmazonSQSAsync sqsClient = AmazonSQSAsyncClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
		Future<ReceiveMessageResult> message = sqsClient.receiveMessageAsync("memory-vault", new AsyncHandler<ReceiveMessageRequest, ReceiveMessageResult>() {
			
			@Override
			public void onSuccess(ReceiveMessageRequest request, ReceiveMessageResult result) {
				
				log.info("Success!!!" + result.getMessages().toString());
			}
			
			@Override
			public void onError(Exception exception) {
				
				log.info("Error");
			}
			
		});
		
		try {
			
			boolean s = message.get().getMessages().get(0).getBody().equals("Test");
			assertTrue(s);
			
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} catch (ExecutionException e) {
			
			e.printStackTrace();
		}
		
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
		
		
		
	}
	
}
