package com.hdekker;

import java.util.function.Function;

import com.hdekker.media.slideshows.SlideShowMediaType;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.server.StreamResource;

public interface MemoryVaultUtils {

	/**
	 *  This may still be relevant but will be requested by the
	 *  client as required.
	 * 
	 */
	public static Function<MemoryBuffer, Element> getImageOrVideoVaadinElement() {
		
		return (media) -> {
			if(media.getFileData().getMimeType().equals(SlideShowMediaType.VIDEO_MP4.getContentType())) {
				
				StreamResource res = new StreamResource(media.getFileName(), ()-> media.getInputStream());
				
				Element video = new Element("video");
				video.setAttribute("name", "media");
				video.setAttribute("autoplay", "");
				
				Element source = new Element("source");
				source.setAttribute("src", res);
				source.setAttribute("type", "video/mp4");
				video.appendChild(source);
						
				return video;
			}else {
				
				Image img = new Image();
				StreamResource res = new StreamResource(media.getFileName(), ()-> media.getInputStream());
				img.setSrc(res);
				return img.getElement();
			}
		};
		
	}
	
}
