package com.hdekker.media.slideshows.sessions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hdekker.MappedEventProvider;
import com.vaadin.flow.shared.Registration;

/***
 * Thread safe implementation
 * 
 * @author HDekker
 *
 * @param <K>
 * @param <V>
 */
public class InMemAbstractMappedEventManager<K,V> implements MappedEventProvider<K, V>{

	Logger log = LoggerFactory.getLogger(InMemAbstractMappedEventManager.class);
	
	Map<K, Set<Consumer<V>>> consumers = new HashMap<>();
	
	@Override
	public synchronized Map<K, Set<Consumer<V>>> getListeners() {
		
		return consumers;
	}

	@Override
	public synchronized void setListeners(Map<K, Set<Consumer<V>>> map) {
		consumers = map;
	}

	@Override
	public synchronized void fireListeners(K key, V event) {
		
		log.info("Fireing Listeners for " + consumers.get(key).size() + " consumers.");
		MappedEventProvider.fireListeners(consumers).accept(key, event);
		
	}

	@Override
	public synchronized Registration addListener(K key, Consumer<V> listener) {
		
		return MappedEventProvider.addListener(
								()->{return consumers;}, 
								(l)-> {consumers = l;}, 
								(k,v) ->{
									removeKey(k,v);
								})
								.apply(key, listener);
	}

	private void removeKey(K k, Consumer<V> v) {
	
		synchronized (this) {
			
			Set<Consumer<V>> list = consumers.get(k);
			Set<Consumer<V>> newList = new HashSet<Consumer<V>>(list);
			newList.remove(v);
			if(newList.size()==0) consumers.remove(k);
			else consumers.put(k, newList);
			
		}
		
	}
	
	public Registration addListeners(List<K> keys, Consumer<V> consumer) {
		
		List<Registration> registrations = keys.stream().map(key-> addListener(key, consumer))
				.collect(Collectors.toList());
		
		return ()-> {
			
			registrations.forEach(r->r.remove());
			
		};
	}
	
	
}
