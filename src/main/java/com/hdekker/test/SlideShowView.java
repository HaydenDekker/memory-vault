package com.hdekker.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.util.Streams;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.Element;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route("video-player-test")
public class SlideShowView extends VerticalLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	InputStream in = null;
	
	public SlideShowView() {
		
		File mp4 = new File("src/test/resources/kk.mp4");

		try {
			in = new FileInputStream(mp4);
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		}
		
		MemoryBuffer memBuf = new MemoryBuffer();
		try {
			Streams.copy(in, memBuf.receiveUpload("kk.mp4", "video/mp4"), true);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
		StreamResource res = new StreamResource("kk.mp4", ()-> memBuf.getInputStream());
		
		Element video = new Element("video");
		video.setAttribute("name", "media");
		Element source = new Element("source");
		source.setAttribute("src", res);
		source.setAttribute("type", "video/mp4");
		video.appendChild(source);
				
		this.getElement().appendChild(video);
			//<body>
				//<video controls="" autoplay="" name="media">
				//<source src="file:///C:/Users/HDekker/workspace_062020/memoryvault/memoryvault/src/test/resources/kk.mp4" type="video/mp4">
				//</video>
			//</body>	
		
	}
	
}
