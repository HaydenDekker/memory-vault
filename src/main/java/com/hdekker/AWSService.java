package com.hdekker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.hdekker.media.slideshows.SlideShowMediaType;
import com.hdekker.media.slideshows.data.Media;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.shared.Registration;

@Service
public class AWSService {

	Logger log = LoggerFactory.getLogger(AWSService.class);
	
	List<Consumer<Media>> ssConsumers = new ArrayList<>();
	
	final String primaryBucketName = "memory-vault";
	
	@Autowired
	AWSMessageSubscriber messageSubscriber;
	
	final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
	
	Function<String, CompletableFuture<Optional<MemoryBuffer>>> memoizer;
	
	public AWSService() {
		
		this.memoizer = Memoizer.memoize(this::getMediaByMemoizer);
		
	}
	/**
	 * This is being removed in place of the slide show controller
	 * 
	 */

	
	/**
	 * Adds session, not related to any User at this point.
	 * 
	 * 
	 * @param ui
	 * @return
	 */
	public Registration addSession(Consumer<Media> ssConsumer) {
		
		this.ssConsumers.add(ssConsumer);
		return () -> ssConsumers.remove(ssConsumer);
		
	}

	public CompletableFuture<List<Media>> getAllAvailableMedia() {
		
		return CompletableFuture.supplyAsync(()->{
	
	      ObjectListing ol = s3.listObjects(primaryBucketName);
	      return ol.getObjectSummaries()
							.stream().map((obj)-> {
					    	  Media m = new Media();
					    	  m.setKey(obj.getKey());
					    	  String contentType = s3.getObjectMetadata(primaryBucketName, obj.getKey()).getContentType();
					    	  m.setMediaType(SlideShowMediaType.getByContentType(contentType));
		
					    	  return m;
					      }).collect(Collectors.toList());	
			
		});

	}
	
	//Memoizer<String, CompletableFuture<Optional<MemoryBuffer>>> mediaMemoizer = new Memoizer<>();
	
	/**
	 * No longer using this memoizer for media objects.
	 * 
	 * Objects can be too big. The EC2 instances only 
	 * have 1 GB of memory and a 100MB movie would consume
	 * too much of the heap. 
	 * 
	 * The new method is to supply the stream and then remove
	 * the resource as soon as the client has downloaded the resource.
	 * 
	 * @param key
	 * @return
	 */
	private CompletableFuture<Optional<MemoryBuffer>> getMediaByMemoizer(String key) {
		
		return CompletableFuture.supplyAsync(()->{
			
		      S3Object obj = s3.getObject(this.primaryBucketName, key);
		      log.info(""+ key + " has been included");
		      return Optional.of(AWSUtils.getMemoryBuffer().apply(obj));
				
		});
	}

	/**
	 *  TODO this needs to be made a direct connection from
	 *  client to the S3 bucket.
	 * 
	 * @param key
	 * @return
	 */
	public CompletableFuture<Optional<MemoryBuffer>> getMedia(String key) {
	
		return CompletableFuture.supplyAsync(()->{
			
			S3Object obj = s3.getObject(this.primaryBucketName, key);
			log.info(""+ key + " has been retrieved from the S3 bucket");
			return Optional.of(AWSUtils.getMemoryBuffer().apply(obj));
			
		});
	}

	
}
