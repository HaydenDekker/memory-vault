package com.hdekker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.cloud.aws.messaging.config.annotation.SqsConfiguration;
import org.springframework.cloud.aws.messaging.listener.QueueMessageHandler;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;

import com.amazonaws.services.s3.event.S3EventNotification;
import com.hdekker.media.slideshows.definition.MediaEvent;
import com.hdekker.media.slideshows.definition.MediaEvent.SlideShowMediaEventType;


@Configuration
@EnableSqs
public class AWSMessageSubscriber implements EventProvider<MediaEvent>{

	Logger log = LoggerFactory.getLogger(AWSMessageSubscriber.class);
	
	QueueMessageHandler j;
	
	@Bean
	public QueueMessageHandlerFactory queueMessageHandlerFactory() {
	    QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
	    MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();

	    //set strict content type match to false
	    messageConverter.setStrictContentTypeMatch(false);
	    factory.setArgumentResolvers(Collections.<HandlerMethodArgumentResolver>singletonList(new PayloadMethodArgumentResolver(messageConverter)));
	    return factory;
	}
	
	SqsConfiguration sds;
	
	@SqsListener("https://sqs.ap-southeast-2.amazonaws.com/440654054855/memory-vault")
	public void receive(S3EventNotification s3EventNotificationRecord) {
	    S3EventNotification.S3Entity s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
	    log.info("recieved "+ s3Entity.getBucket().getName() + " event");
	    
	    MediaEvent event = new MediaEvent(SlideShowMediaEventType.ADDED,
	    											s3Entity.getObject().getKey());
	    
	    consumers.forEach((c)-> c.accept(event));
	
	}

	List<Consumer<MediaEvent>> consumers = new ArrayList<>();

	@Override
	public List<Consumer<MediaEvent>> getListeners() {
		
		return consumers;
	}
	
//	public void receive(String s3EventNotificationRecord) {
//	   // S3EventNotification.S3Entity s3Entity = s3EventNotificationRecord.getRecords().get(0).getS3();
//	    log.info("recieved "+ s3EventNotificationRecord + " event");
//	    //S3EventNotification notification = S3EventNotification.parseJson(s3EventNotificationRecord);
//	    ObjectMapper m = new ObjectMapper();
//	    S3EventNotification notification = null;
//		try {
//			notification = m.readValue(s3EventNotificationRecord, S3EventNotification.class);
//		} catch (JsonMappingException e) {
//			
//			e.printStackTrace();
//		} catch (JsonProcessingException e) {
//			
//			e.printStackTrace();
//		}
//	    
//		log.info("Sweet converted " + notification.getRecords().get(0).getEventName());
//		
//		
//	}
//	
	
	
}
