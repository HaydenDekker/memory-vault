package com.hdekker.unit;

import static org.junit.Assert.assertTrue;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.controller.ControllerCommand;
import com.hdekker.media.slideshows.controller.ControllerEvent;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.sessions.SessionService;

@SpringBootTest
public class SessionEventManger {

	@Autowired
	SessionService sService;
	
	@Autowired
	ControllerService cService;
	
	Logger log = LoggerFactory.getLogger(SessionEventManger.class);
	
	Boolean hasRun;
	
	@Test
	public void registersAControllerListener() {
		
		hasRun = false;
		
		Controller c1 = cService.createNewController("test-controller");
		Controller c2 = cService.createNewController("test-controller-2");
		
		sService.registerControllerListener(Arrays.asList(c1.getSortKey(), c2.getSortKey()), (e)-> {
			
			log.info(e.getCommand().toString() + " from " + e.getController().getControllerName());
			hasRun = true;
			
		});
		
		ControllerEvent e = new ControllerEvent(c2, ControllerCommand.FORWARD);
		
		sService.accept(e);
		
		cService.delete(c1);
		cService.delete(c2);
		
		assertTrue(hasRun);
		
	}
	
}
