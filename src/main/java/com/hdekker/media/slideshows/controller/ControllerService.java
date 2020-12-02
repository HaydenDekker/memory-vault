package com.hdekker.media.slideshows.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdekker.media.slideshows.data.Controller;

@Service
public class ControllerService {

	@Autowired
	ControllerRepository repo;
	
	public Controller createNewController(String controllerName) {
		
		Controller c = new Controller();
		c.setControllerName(controllerName);
		return repo.create(c);
		
	}

	public void updateController(Controller c) {
		
		repo.update(c);
	}

	public List<Controller> findAll() {
		return repo.findAll();
	}

	public void delete(Controller c) {
		repo.delete(c);
	}

	public Controller getController(String controllerKey) {
		return repo.get(controllerKey);
	}
	
	// 16-01-2020 removed as service is now stateless
	//HashMap<SlideShowController, Set<SlideShowSession>> slideShowControllers
									//= new HashMap<>();
	
	// 16-01-2020 removed as service is now stateless	
	//List<SlideShowController> controllers = new ArrayList<SlideShowController>();
	
	// TODO need to model the controller as data and the manager provides the function
//	public SlideShowController createSlideShowSessionController() {
//		SlideShowController controller = SlideShowUtils.createSlideShowSessionController();
//		controllers.add(controller);
//		return controller;
//	}
//	
//	public void assignControllerToSession(SlideShowController controller, SlideShowSession slideShowSession) {
//		
//		slideShowControllers.computeIfAbsent(controller, (con)-> Sets.newHashSet(slideShowSession));
//		slideShowControllers.computeIfPresent(controller, (cont, set) -> {
//			set.add(slideShowSession);
//			return set;
//		});
//		
//		controller.slideShowSubscribe(
//				()-> Optional.of(slideShowSession.getMediaDiaplayComponent())
//		);
//		
//	}
	
}