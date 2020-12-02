package com.hdekker;

import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.SlideShowMediaType;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.sessions.SessionService;

@SpringBootTest
public class SlideShowSessionTest {

	Logger log = LoggerFactory.getLogger(SlideShowSessionTest.class);
	
	@Autowired
	SessionService sessionManager;
	
	@Test
	public void testSlideShowSessionCreation() {
		
		Definition ssd = new Definition();
		ssd.setDefinitionName("test-ss");
		ssd.setMedia(Arrays.asList(
				new Media("Book", SlideShowMediaType.IMAGE_JPEG),
				new Media("Cat Funny", SlideShowMediaType.VIDEO_MP4)
				)
		);
		
//		SlideShowDefinitionReciever ssdr = new SlideShowDefinitionReciever() {
//
//			@Override
//			public SlideShowCloudCommandResult receiveSlideShowEvent(DefinitionEvent ssd) {
//				assertTrue(ssd.getSlideShowDefinition().getSlideShowMedia().get(0).getKey().equals("Book"));
//				return new SlideShowCloudCommandResult(SlideShowCommandType.PLAY, SlideShowCloudCommandResponseType.ACCEPTED);
//			}
//		};
		
		//sessionManager.createSlideShowSession((MediaDisplayComponent) ssdr, CompletableFuture.supplyAsync(()->ssd));
			
	}
	
	// 16-10-2020 no longer valid. Can have multiple session with same definiton
//	@Test
//	public void slideShowSessionCreationForSameMDCDoesNotDuplicate() {
//		
//		SlideShowDefinition ssd = new SlideShowDefinition();
//		ssd.setSlideShowName("test-ss");
//		ssd.setSlideShowMedia(Arrays.asList(
//				new Media("Book", SlideShowMediaType.IMAGE_JPEG),
//				new Media("Cat Funny", SlideShowMediaType.VIDEO_MP4)
//				)
//		);
//		
//		MediaDisplayComponent mdc = new MediaDisplayComponent() {
//			
//			@Override
//			public void onSlideShowSessionEvent(SlideShowSessionEvent event) {
//				
//				log.info("SS event called of type " +  event.getEventType());
//				
//			}
//			
//			@Override
//			public SlideShowCloudCommandResult receiveCommand(SlideShowCommandType ss) {
//				// TODO Auto-generated method stub
//				return null;
//			}
//			
//			@Override
//			public MediaDisplayMetaData getDisplayMetaData() {
//				
//				MediaDisplayMetaData data = new MediaDisplayMetaData("Media-Screen-1");
//				
//				return data;
//			}
//		};
//		
//		sessionManager.createSlideShowSession(mdc, CompletableFuture.supplyAsync(()->ssd));
//		assertTrue(sessionManager.getCurrentSessions().size() == 1);
//		sessionManager.createSlideShowSession(mdc, CompletableFuture.supplyAsync(()->ssd));
//		assertTrue(sessionManager.getCurrentSessions().size() == 1);
//		
//	}
//	
}
