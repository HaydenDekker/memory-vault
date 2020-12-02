package com.hdekker;

import java.util.List;
import java.util.function.Consumer;

import com.vaadin.flow.shared.Registration;

public interface EventProvider<T> {

	public List<Consumer<T>> getListeners();
	
	public default void fireListeners(T event) {
		
		getListeners().forEach((listener)-> listener.accept(event));
		
	}
	
	public default Registration addListener(Consumer<T> listener) {
		getListeners().add(listener);
		return () -> getListeners().remove(listener);
	}
	
}
