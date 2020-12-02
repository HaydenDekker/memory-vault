package com.hdekker.unit;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;


/**
 * File System Strategy: 
	The user can structure the folders how they wish
	The user can create slide shows that capture the meta data against any number of folders or files.
	The application relies on the tagging of the media by the source to provide actual information of the media. 
				E.g date taken, camera used.
 * 
 * @author HDekker
 *
 */
@SpringBootTest
public class SlideShowDefinitionTest {

	Logger log = LoggerFactory.getLogger(SlideShowDefinitionTest.class);
	
	@Test
	public void getsFoldersAndFiles() {
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
	      List<Bucket> buckets = s3.listBuckets();
	      System.out.println("Your Amazon S3 buckets are:");
	      for (Bucket b : buckets) {
	          System.out.println("* " + b.getName());
	      }
	      
	      String bucket = "memory-vault";
	      ObjectListing ol = s3.listObjects(bucket);
	      List<S3ObjectSummary> data = ol.getObjectSummaries();
	      log.info(data.stream().map(obj->obj.getKey()).collect(Collectors.toList()).toString());
	      
	}
	
	// How will folders work?? need date time?? metadata stored separately?? or in folder structure?
	// TODO create API
	@Test
	public void listsAllSlideShows() {
		
	}
}
