package com.hdekker;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ObjectListing;

@SpringBootTest
@WebAppConfiguration
public class AWSConnectionTest {

	Logger log = LoggerFactory.getLogger(AWSConnectionTest.class);
	
	@Test
	public void testRuns() {
		
	}
	
	
	@Test
	public void canConnectToAWSUsingStoredCredentials() {
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
      List<Bucket> buckets = s3.listBuckets();
      System.out.println("Your Amazon S3 buckets are:");
      for (Bucket b : buckets) {
          System.out.println("* " + b.getName());
      }
		
	}
	
	@Test
	public void canReadNamesOfFilesInMemoryVault() {
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_2).build();
	      List<Bucket> buckets = s3.listBuckets();
	      System.out.println("Your Amazon S3 buckets are:");
	      for (Bucket b : buckets) {
	          System.out.println("* " + b.getName());
	      }
	      
	      String bucket = "memory-vault";
	      ObjectListing ol = s3.listObjects(bucket);
	      List<String> data = ol.getObjectSummaries().stream().map(os -> { return os.getKey() + " of size " + os.getSize();}).collect(Collectors.toList());
	      data.forEach(d-> log.info(d));
	}

}
