package com.hdekker;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import com.vaadin.flow.shared.Registration;

/**
 * Interface for a class that provides events based
 * on a map to a list of consumers
 * 
 * A Stateful Interface
 * 
 * @author HDekker
 *
 * @param <K>
 * @param <V>
 */
public interface MappedEventProvider<K, V> {

	public Map<K, Set<Consumer<V>>> getListeners();
	public void setListeners(Map<K, Set<Consumer<V>>> listeners);
	
	public void fireListeners(K key, V event);
	public Registration addListener(K key, Consumer<V> listener);
	
	public static <K,V> BiConsumer<K, V> fireListeners(Map<K, Set<Consumer<V>>> map) {
		return (key, event) -> {
			map.get(key).forEach((listener)-> listener.accept(event));
		};
	}
	
//	public default void fireListeners(K key, V event) {
//		
//		getListeners().get(key).forEach((listener)-> listener.accept(event));
//		
//	}
	
	public static <K,V> BiFunction<K, Consumer<V>, Registration> addListener(Supplier<Map<K, Set<Consumer<V>>>> listenersGetter,
																			Consumer<Map<K, Set<Consumer<V>>>> listenersSetter, 
																			BiConsumer<K, Consumer<V>> keyRemover) {
		
		return (key, listener) ->{
				listenersSetter.accept(addValueToMappedList(listenersGetter.get()).apply(key, listener));
			return () -> {
					keyRemover.accept(key, listener);
			};
		};
	}
	
//	public default Registration addListener(K key, Consumer<V> listener) {
//		
//		synchronized (this) {
//			setListeners(addValueToMappedList(getListeners()).apply(key, listener));
//		}
//		return () -> {
//			synchronized (this) {
//				getListeners().get(key).remove(listener);
//			}
//		};
//		
//	}
	
	public static <T> BiFunction<T, Set<T>, Set<T>> createNewList(){
		return (t, listT) ->{
			Set<T> newList = new HashSet<>(listT);
			newList.add(t);
			return newList;
		};
	}
	
	public static <K, T> BiFunction<K, T, Map<K, Set<T>>> addValueToMappedList(Map<K, Set<T>> map){
		return (key, val)->{
			HashMap<K, Set<T>> newMap = new HashMap<>(map);
			
			newMap.computeIfPresent(key, (k, v)-> {
				BiFunction<T, Set<T>, Set<T>> listCreator = createNewList();
				return listCreator.apply(val, v);
				});
			newMap.computeIfAbsent(key, (k) -> Arrays.asList(val).stream().collect(Collectors.toSet()));
			return newMap;
		};
	}
	
	public static <K, T> BiFunction<K, Map<K, Set<T>>, Map<K, Set<T>>> removeKey(){
		
		return (key, map)->{
			HashMap<K, Set<T>> newMap = new HashMap<>(map);
			newMap.remove(key);
			return newMap;
		};
		
	};
	
	
}
