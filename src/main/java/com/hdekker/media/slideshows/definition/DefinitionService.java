package com.hdekker.media.slideshows.definition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdekker.AWSService;
import com.hdekker.EventProvider;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.definition.DefinitionEvent.SlideShowDefinitionEventType;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

@Service
public class DefinitionService implements
								EventProvider<DefinitionEvent>{
	
	@Autowired
	AWSService awsService;
	
	@Autowired
	DefinitionsRepository definitionRepo;
	
	public void updateMediaInSlideShowDefinition(MediaEvent event) {
		
		//TODO MVP 0.3 - dodgy implementation
		CompletableFuture<Definition> ss = getDefaultSlideShow();
		fireListeners(new DefinitionEvent(SlideShowDefinitionEventType.ADDED, 
							ss.join()));
		
	}
	
	public CompletableFuture<Optional<MemoryBuffer>> getMediaObject(String key){
		
		return awsService.getMedia(key);
		
	}
	
	public CompletableFuture<Optional<MemoryBuffer>> getMediaObject(Media media){
		
		return awsService.getMedia(media.getKey());
		
	}
	
	public CompletableFuture<Definition> getDefaultSlideShow() {
		
		return awsService.getAllAvailableMedia().thenApply((media)-> {
			
			Definition ssd = new Definition();
			ssd.setDefinitionName("default-slideshow");
			ssd.setMedia(media);
			return ssd;
			
		});
		
	}
	
	// 13-10-2020 moved to the generic even provider interface
//	List<Consumer<SlideShowDefinitionEvent>> definitionEventSubscribers = new ArrayList<>();
//
//	private void messageSubscribers(SlideShowDefinitionEvent event) {
//		
//		definitionEventSubscribers.forEach((s)-> s.accept(event));
//		
//	}
//	
//	@Override
//	public Registration subscribeToUpdates(Consumer<SlideShowDefinitionEvent> event) {
//		
//		definitionEventSubscribers.add(event);
//		return ()-> definitionEventSubscribers.remove(event);
//	}

	public Definition createNewDefinition(String definitionName, String definitionDescription) {
		
		Definition def = new Definition();
		def.setDefinitionName(definitionName);
		def.setDefinitionDescription(definitionDescription);
		def.setMedia(new ArrayList<>());
		
		return definitionRepo.create(def);
		
	}

	public List<Definition> findAllDefinitions() {
		return definitionRepo.findAll();
	}
	
	@Override
	public List<Consumer<DefinitionEvent>> getListeners() {
		return definitionRepo.getEventProvider().getListeners();
	}

	public void deleteDefinition(Definition selectedDefinition) {
		
		definitionRepo.delete(selectedDefinition);
	}

	public void updateDefinition(Definition selectedDefinition) {
		definitionRepo.update(selectedDefinition);
	}

	public CompletableFuture<List<Media>> getMedia() {
		return awsService.getAllAvailableMedia();
	}

	/**
	 * SOoo o many issues
	 * TODO needs Async and immutable data
	 * 
	 * @param selectedDefinition
	 * @param string
	 * @return
	 */
	public Definition addMediaToDefinition(Definition selectedDefinition, Media media) {
		
		if(selectedDefinition.getMedia()==null) selectedDefinition.setMedia(new ArrayList<>());
		selectedDefinition.getMedia().add(media);
		definitionRepo.update(selectedDefinition);
		return definitionRepo.get(selectedDefinition);
	}

	public Definition getDefinition(String testDefinitionKey) {
		return definitionRepo.get(testDefinitionKey);
	}
	
}
