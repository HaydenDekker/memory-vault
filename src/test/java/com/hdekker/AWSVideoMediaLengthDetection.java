package com.hdekker;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

@SpringBootTest
@WebAppConfiguration
public class AWSVideoMediaLengthDetection {

	String bucket = "memory-vault";
	
	public Function<ObjectListing, List<S3ObjectSummary>> getAllMP4sInBucket(){
		return (ol) -> {
			 return ol.getObjectSummaries()
						.stream()
						.filter((obj)-> obj.getKey().contains(".mp4"))
						.collect(Collectors.toList());
		};
	};
	
	Logger log = LoggerFactory.getLogger(AWSVideoMediaLengthDetection.class);
	
	/**
	 * 
	 * 
	 */
	
	@Test
	public void awsVideoLengthDetection() {
		
		CompletableFuture<S3Object> future = CompletableFuture.supplyAsync(()->{
			
			final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build(); 
			ObjectListing ol = s3.listObjects(bucket);
		    List<S3ObjectSummary> summaries = getAllMP4sInBucket().apply(ol);
		    return s3.getObject(summaries.get(0).getBucketName(), summaries.get(0).getKey());
		    
		});
		
		S3Object obj = future.join();
		MemoryBuffer buffer = AWSUtils.getMemoryBuffer().apply(obj);
		try {
			obj.close();
		} catch (IOException e1) {
			
			e1.printStackTrace();
		}
		ObjectMetadata metadata = obj.getObjectMetadata();
		log.info("Content Type = " + metadata.getContentType());
		
		Optional<Long> duration = MP4Utils.getMovieDuration().apply(buffer.getInputStream());
        assertTrue(duration.isPresent());
        log.info("The movie downloaded is " + duration + " miliseconds long.");

	}
	
}
