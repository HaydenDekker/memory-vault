package com.hdekker.media.slideshows.displays;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hdekker.EventProvider;
import com.hdekker.media.slideshows.data.MediaDisplayComponent;

@Service
public class DisplayService implements EventProvider<DisplayEvent>{
	
	@Autowired
	DisplayRepo repo;

	public Runnable registerDisplay(MediaDisplayComponent mediaDisplayComponent) {
		
		// need to remove the display if an object already exists
		// that matches the display name.
		List<MediaDisplayComponent> displays = repo.findAll();
		checkIfExists(displays).apply(mediaDisplayComponent)
			.ifPresent((disp)-> repo.delete(disp));
		
		repo.create(mediaDisplayComponent); 
		fireListeners(new DisplayEvent(DisplayEventType.Connected, mediaDisplayComponent));
		
		return () -> {
			
			repo.delete(mediaDisplayComponent);
			fireListeners(new DisplayEvent(DisplayEventType.Disconnected, mediaDisplayComponent));
			
		};
		
	}
	
	public List<MediaDisplayComponent> getDisplayComponents(){
		return repo.findAll();
	}
	
	private List<Consumer<DisplayEvent>> displayListeners = new ArrayList<>();

	@Override
	public List<Consumer<DisplayEvent>> getListeners() {
		return displayListeners;
	}
	
	Function<MediaDisplayComponent, Optional<MediaDisplayComponent>> checkIfExists(List<MediaDisplayComponent> allDisplays){
		return (mdc) -> allDisplays.stream().filter((m)-> m.getDisplayName().equals(mdc.getDisplayName()))
				.findFirst();
	}

	public void delete(MediaDisplayComponent obj) {
		repo.delete(obj);
		
	}

	public MediaDisplayComponent getDisplay(String displayKey) {
		return repo.get(displayKey);
	}

	public MediaDisplayComponent createNewDisplay(String string) {
		
		MediaDisplayComponent c = new MediaDisplayComponent();
		c.setDisplayName(string);
		return repo.create(c);
		
	}

	/**
	 * Display needs to provide a unique id
	 * 
	 * @param parameter
	 * @return
	 */
	public MediaDisplayComponent checkInDisplay(String parameter) {
		
		Optional<MediaDisplayComponent> displ = getDisplayComponents().stream().filter(d->d.getDisplayName().equals(parameter)).findFirst();
		return displ.map((d)->d).orElseGet(()->createNewDisplay(parameter));
		
	}
	
}
