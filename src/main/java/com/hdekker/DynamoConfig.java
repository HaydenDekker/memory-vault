package com.hdekker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

@Configuration
//EnableDynamoDBRepositories(basePackages = "com.hdekker.media.slideshows.data")
public class DynamoConfig {

	//@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
					.withRegion(Regions.AP_SOUTHEAST_2)
					.build();
	}
	
	@Bean
	public DynamoDBMapper getDynamoDBMapper() {
		return new DynamoDBMapper(amazonDynamoDB());
	}
	
}
