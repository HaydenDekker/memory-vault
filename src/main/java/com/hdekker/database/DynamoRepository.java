package com.hdekker.database;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.hdekker.UserNameAccessible;

/**
 * Define how I want the database access to occur.
 * 
 * @author HDekker
 *
 */
public interface DynamoRepository<T extends DynamoObject> extends UserNameAccessible{

	public DynamoDBMapper getMapper();
	
	public default String getKeyNameDataTypePostFix() {
		return getObjectClass().getSimpleName();
	}
	public default String getDynamoKey() {
		return getUserName() + "-" + getKeyNameDataTypePostFix();
	}
	public Class<T> getObjectClass();
	
	public default List<T> findAll(){
		
		DynamoDBQueryExpression<T> getDefs = new DynamoDBQueryExpression<T>();
		getDefs.setKeyConditionExpression("#k = :hashVal");
		
		Map<String, AttributeValue> exp = new HashMap<String, AttributeValue>();
		exp.put(":hashVal", new AttributeValue(getDynamoKey()));
		Map<String, String> names = new HashMap<String, String>();
		names.put("#k", "key");
		
		getDefs.setExpressionAttributeValues(exp);
		getDefs.setExpressionAttributeNames(names);
		
		return getMapper().query(getObjectClass(), getDefs).stream().collect(Collectors.toList());
		
	}
	
	public default T create(T obj) {
		
		obj.setKey(getDynamoKey());
		obj.setFirstCreated(LocalDateTime.now());
		getMapper().save(obj);
		return getMapper().load(obj);
		
	}
	
	public default T update(T obj) {
		obj.setLastModified(LocalDateTime.now());
		getMapper().save(obj);
		return getMapper().load(obj);
	}
	public default void delete(T obj) {	
		getMapper().delete(obj);
	}
	public default T get(T obj) {
		return getMapper().load(obj);
	}
	
	public default T get(String key) {
		
		// must have a key, else don't bother calling
		if(key==null) return null;
		if(key=="") return null;
		
		T newObj = null;
	
		try {
			newObj = getObjectClass().getConstructor().newInstance();
			
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		newObj.setKey(getDynamoKey());
		newObj.setSortKey(key);
		return getMapper().load(newObj);
		
	}
	
}
