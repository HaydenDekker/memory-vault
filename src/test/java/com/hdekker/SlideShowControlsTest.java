package com.hdekker;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SlideShowControlsTest {

	/**
	 * A slide show defines a series of images and videos
	 * To start a slide show we need a definition of what the
	 * slideshow.
	 * 
	 * The client browser controls the flow of the slide show and reports back,
	 * as the SS changes.
	 * 
	 */
	@Test
	public void testSlideShowStart() {
		
		
//		// creates a ss
//		SlideShowSessionController controller = SlideShowUtils.createSlideShowSessionController();
//		SlideShowCommandReceiver cd = new SlideShowCommandReceiver() {
//			
//			@Override
//			public SlideShowCloudCommandResult receiveCommand(SlideShowCommandType ss) {
//				
//				assertTrue(ss.equals(SlideShowCommandType.PLAY));
//				return new SlideShowCloudCommandResult(ss, SlideShowCloudCommandResponseType.ACCEPTED);
//			}
//			
//		};
//		controller.slideShowSubscribe(() -> Optional.of(cd));
//		List<CompletableFuture<SlideShowCloudCommandResult>> cmdFuter = controller.issueSSCommand(SlideShowCommandType.PLAY);
//		assertTrue(cmdFuter.get(0).join().getEventType().equals(SlideShowCloudCommandResponseType.ACCEPTED));
//		
	}
	
}
