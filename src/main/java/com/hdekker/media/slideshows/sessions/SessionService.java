package com.hdekker.media.slideshows.sessions;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdekker.EventProvider;
import com.hdekker.media.slideshows.controller.ControllerEvent;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.media.slideshows.definition.DefinitionEvent;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.hdekker.media.slideshows.displays.DisplayService;

import com.vaadin.flow.shared.Registration;

@Service
public class SessionService implements EventProvider<SessionEvent>, Consumer<ControllerEvent>{
		
		Logger log = LoggerFactory.getLogger(SessionService.class);
		
		@Autowired
		SessionRepository repo;
		
		@Autowired
		DisplayService displayService;
		
		@Autowired
		ControllerService controllerService;
		
		@Autowired
		DefinitionService definitionService;
		
		@Autowired
		public InMemControllerEventManager controllerConsumers;
		
		@Autowired
		InMemDisplayEventManager displayConsumers;
		
		/**
		 * Displays will register controller listeners
		 * 
		 * @param controllerKey
		 * @param consumer
		 * @return
		 */
		public Registration registerControllerListener(List<String> controllerKeys, Consumer<ControllerEvent> consumer) {
			
			return controllerConsumers.addListeners(controllerKeys, consumer);
		}
		
//		public Registration registerDisplayListener(String displayKey, Consumer<DisplayEvent> consumer) {
//			
//			// Try connect registered controllers
//			
//			return sessionUis.registerDisplay(displayKey, consumer);
//		}
		
		public void updateSlideShowDefinition(DefinitionEvent event) {
			
			Definition definition = event.getSlideShowDefinition();
			log.info("Session Manager recieved an update with the following media included as the SS " + definition.getMedia().toString());

//			// 
//			List<MediaDisplayComponent> mdcs =  getCurrentSessions().stream()
//									.map((s)-> s.getMediaDiaplayComponent())
//									.collect(Collectors.toList());
//			
//			// no concept of update. If a MDC has a session already
//			// this is considered an updated.
//			mdcs.forEach((mdc)-> createSlideShowSession(mdc, 
//								CompletableFuture.supplyAsync(()-> definition))
//			);
			
		}
	
		// 16-10-2020: removed as services are forced to be stateless.
//		Set<SlideShowSession> activeSessions = 
//				new HashSet<>();
//		
//		
		/**
		 * Deactivates all other sessions where display matches
		 * Set's a session to active,
		 * connects controller to display
		 * Issue event to display
		 * @param s 
		 * 
		 */
		public SSSession activateSession(SSSession s, Boolean isActive) {
			
			s.setIsActive(isActive);
			// trigger the active event, may be an existing display updated
			// mdc.onSlideShowSessionEvent(event);
					
			// TODO can only have one active session for a single display component
//			Optional<SlideShowSession> existingSession = activeSessions.stream().filter((sess)-> sess
//									.getMediaDiaplayComponent().getDisplayMetaData().getDisplayName()
//									.equals(mdc.getDisplayMetaData().getDisplayName()))
//							.findFirst();
			return updateSession(s);

		}

		
		/**
		 * Creates slide show,
		 * adds slide show to MDC
		 * ensures only one SS is assigned to a display
		 * 
		 * Does not start SS
		 * 
		 * @param mdc
		 * @param gss
		 * @return
		 */
		public SSSession createSession(String sessionName, String mdcKey, 
				String defKey, String conKey) {

			// TODO MVP 0.3 - update views that display active sessions
			SSSession sss = new SSSession();
			sss.setName(sessionName);
			sss.setMediaDisplayComponentSortKey(mdcKey);
			sss.setSlideShowDefinitionSortKey(defKey);
			sss.setControllerSortKey(conKey);
			sss.setIsActive(false);
			
			return repo.create(sss);
			
		}
		
		
		public List<SSSession> getSessions() {
			
			return repo.findAll();
			
		}

		@Override
		public List<Consumer<SessionEvent>> getListeners() {
			
			return repo.getEventProvider().getListeners();
		}

		public SSSession updateSession(SSSession ssSession) {
		
			return repo.update(ssSession);
		}

		public void delete(SSSession s) {
			
			repo.delete(s);
		}

		@Override
		public void accept(ControllerEvent event) {
			
			log.info("Controller event triggered " + event.getController());
			// TODO best way with dyanamoDB?
			//List<SSSession> sessions = getAllSessionsWithController(event.getController());
			//List<String> displayKeys = sessions.stream().map(s->s.getMediaDiaplayComponentKey()).collect(Collectors.toList());
			controllerConsumers.fireListeners(event.getController().getSortKey(), event);
			
		}
		
		public List<SSSession> getAllSessionsWithDisplay(MediaDisplayComponent mdc){
			
			return repo.allSessionsWithDisplay(mdc);
			
		};
		
		/**
		 * Finds an active session for a display
		 * A display must only have a single active session per display
		 * 
		 * @param mdc
		 * @return
		 */
		public Optional<SSSession> getActiveSessionForDisplay(MediaDisplayComponent mdc) {
			
			return repo.allSessionsWithDisplay(mdc).stream().filter(s->s.getIsActive()).findFirst();
		}

		public List<SSSession> getAllSessionsWithController(Controller controller) {
			
			return repo.allSessionsWithController(controller.getSortKey());
		}

		public Registration subscribeSessionEvents(Consumer<SessionEvent> eventConsumber) {
			
			// TODO implement
			return ()->{};
			
		}

		
}
