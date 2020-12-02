package com.hdekker.database;

import java.util.function.Function;

public interface DynamoDBUtils {

	public static Function<PrimaryKeyApplicationEventTypes, String> getKeyForEvent(String userName){
		return s-> userName + "+" +  s.eventAddon;
	}
	
	public static Function<String, String> incrementSortKey(String prefixSplit){
		return (key)->{
			String[] split = key.split(prefixSplit);
			Integer i = Integer.valueOf(split[1]);
			i++;
			return prefixSplit + i.toString();
		};
	}
	
	public static <T extends DynamoObject> Function<T, String> concatKeyAndSortKey(){
		return (obj)->{return obj.getKey() + "-" + obj.getSortKey();};
	}
	
}
