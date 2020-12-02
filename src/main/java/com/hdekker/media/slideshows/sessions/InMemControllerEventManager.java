package com.hdekker.media.slideshows.sessions;

import org.springframework.stereotype.Component;

import com.hdekker.media.slideshows.controller.ControllerEvent;

/**
 *  Manages the controllers and sessions that need persistent connection
 * 	TODO need to make this threadsafe
 * 
 * 
 * @author HDekker
 *
 */
@Component
public class InMemControllerEventManager extends InMemAbstractMappedEventManager<String, ControllerEvent>{

//	@Autowired
//	UserConfig config;
	
	//Map<String, List<Consumer<DisplayEvent>>> displayEventConsumers = new HashMap<>();
	
//	public synchronized Registration registerControllerConsumer(String controllerKey, EventConsumer<ControllerEvent> controller) {
//		
//		controllerEventConsumers = addValueToMappedList(controllerEventConsumers).apply(controllerKey, controller);
//		return ()-> {
//			remove(controllerKey, controllerEventConsumers);
//		};
//		
//	}
//	
//	public synchronized Registration registerDisplayConsumer(String displayKey, EventConsumer<DisplayEvent> display) {
//			
//			displayEventConsumers = addValueToMappedList(displayEventConsumers).apply(displayKey, display);
//			
//		return ()-> {
//			remove(displayKey, displayEventConsumers);
//		};
//		
//	}
	
//	private synchronized <T> void remove(String key, Map<String, List<T>> map) {
//		BiFunction<String, Map<String, List<T>>, Map<String, List<T>>> remover = removeKey();
//		map = remover.apply(key, map);
//	}

	/**
	 * Anything can consumer a controller event. Just have to register here.
	 * Seems familiar
	 * 
	 * Made MappedEventProvider interface. To go along side the baseic event
	 * provider interface
	 * 
	 * @param controllerEvent
	 */
//	public void notifyAllControllerConsumers(ControllerEvent controllerEvent) {
//		
//		controllerEventConsumers.get(controllerEvent.getController().getKey()).forEach(consumer-> consumer.accept(controllerEvent));
//		
//	}

//	@Override
//	public Map<String, List<Consumer<ControllerEvent>>> getListeners() {
//		
//		return controllerEventConsumers;
//	}
//
//	@Override
//	public void setListeners(Map<String, List<Consumer<ControllerEvent>>> map) {
//		controllerEventConsumers = map;
//	}
//	
}
