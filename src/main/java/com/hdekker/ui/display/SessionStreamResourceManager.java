package com.hdekker.ui.display;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hdekker.component.MediaDefinitionDTO;
import com.hdekker.component.MediaManagementUtils;
import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.definition.DefinitionService;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.server.StreamResourceRegistry;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;

@Component
@VaadinSessionScope
public class SessionStreamResourceManager {
	
	VaadinSession session;
	private StreamResourceRegistry reg;

	SessionStreamResourceManager(){
		reg = VaadinSession.getCurrent().getResourceRegistry();
		session = VaadinSession.getCurrent();
	}
	
	@Autowired
	DefinitionService definitionManager;

	public Function<List<Media>, List<MediaDefinitionDTO>> getAndInitMediaDefinitions() {
		
		Function<Media, CompletableFuture<Optional<MemoryBuffer>>> getMedia = definitionManager::getMediaObject;
		
		Function<CompletableFuture<Optional<MemoryBuffer>>, MemoryBuffer> getBuf = comOpt -> comOpt.join()
																									.orElseThrow(() -> new RuntimeException("sdd"));
		// TODO need a memoizer to avoide creating too many client endpoints.
		// or could try delete them.
		Function<Media, String> mediaObjectStreamURI = 
				getMedia.andThen(getBuf)
				.andThen(MediaManagementUtils.memBufToStream())
				.andThen(MediaManagementUtils.registerResource(reg))
				.andThen(MediaManagementUtils.getURIOfStream());
		
		return ssms -> ssms.stream().map(media -> {
			
			return new MediaDefinitionDTO(
					mediaObjectStreamURI.apply(media),
					media.getKey(),
					media.getMediaType().getContentType(),
					10);
			
			})
			.collect(Collectors.toList());
	
	}

	
	
}
