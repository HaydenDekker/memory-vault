package com.hdekker;

import java.time.LocalDateTime;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;


public class SSDynamoDBLocalDateTimeTypeConverter<T extends Object> implements DynamoDBTypeConverter<String, LocalDateTime>{

	 @Override
     public String convert( final LocalDateTime time ) {

         return time.toString();
     }

     @Override
     public LocalDateTime unconvert( final String stringValue ) {

         return LocalDateTime.parse(stringValue);
     }
	
}
