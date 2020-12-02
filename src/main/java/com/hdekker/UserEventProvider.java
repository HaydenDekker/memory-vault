package com.hdekker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class UserEventProvider<T> implements EventProvider<T>, UserNameAccessible {

	
	/**
	 *  Controls definition event release by username.
	 *  TODO will need to assess user scope to find the correct user for the thread if
	 *  any exist.
	 * 
	 */
	HashMap<String, List<Consumer<T>>> userListeners = new HashMap<>();
	
	@Override
	public List<Consumer<T>> getListeners() {
		
		return Optional.ofNullable(userListeners.putIfAbsent(getUserName(), new ArrayList<>()))
												.orElse(userListeners.get(getUserName()));
	
	}
	
}
