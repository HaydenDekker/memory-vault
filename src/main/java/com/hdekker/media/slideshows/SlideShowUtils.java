package com.hdekker.media.slideshows;

public interface SlideShowUtils {
	
//	/**
//	 * Created each time a Cloud SlideShow Controller 
//	 * executes a command on a slide show, to any
//	 * devices that may or may not be listening.
//	 * 
//	 * 
//	 * @author HDekker
//	 *
//	 */
//	public class SlideShowCloudCommandResult{
//		
//		final SlideShowCommandType forEvent;
//		final SlideShowCloudCommandResponseType eventType;
//		MediaDisplayComponent targetMediaDisplayComponent; 
//		
//		public SlideShowCloudCommandResult(SlideShowCommandType forEvent, SlideShowCloudCommandResponseType eventType) {
//			super();
//			this.forEvent = forEvent;
//			this.eventType = eventType;
//		}
//		public SlideShowCommandType getForEvent() {
//			return forEvent;
//		}
//		public SlideShowCloudCommandResponseType getEventType() {
//			return eventType;
//		}
//
//	}
//	
//	/**
//	 * Users may want multiple devices to display
//	 * the same SlideShow. To do this they need a
//	 * separate controller for each device.
//	 * 
//	 * e.g User selects ControlDevice1 in the Mobile UI
//	 * and issues a start command. They then subscribe Device2
//	 * to the SlideShow controller. Changing one changes both
//	 * devices simultaneously. Lastly on Device3 they want the
//	 * same SlideShow but to be able to control it separately.
//	 * In this situation they create another controller.
//	 * 
//	 * @param ssd
//	 * @return
//	 */
//	static SlideShowSessionController createSlideShowSessionController() {
//		
//		final List<Supplier<Optional<SlideShowCommandReceiver>>> ssConsumers = new ArrayList<>();
//		
//		return new SlideShowSessionController() {
//			
//			@Override
//			public SlideShowSessionController slideShowSubscribe(Supplier<Optional<SlideShowCommandReceiver>> mdc) {
//				ssConsumers.add(mdc);
//				return this;
//			}
//			
//			@Override
//			public List<CompletableFuture<SlideShowCloudCommandResult>> issueSSCommand(SlideShowCommandType event) {
//				
//				return ssConsumers.stream().map(
//										(optMDC)-> optMDC.get()
//														.map((mdc)->
//															CompletableFuture.supplyAsync(()-> mdc.receiveCommand(event))
//														).get()
//									).collect(Collectors.toList());													
//			}
//	
//		};
//
//	}

}
