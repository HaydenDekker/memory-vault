package com.hdekker.media.slideshows;

import java.util.Arrays;

public enum SlideShowMediaType{
	
		IMAGE_JPEG("image/jpeg"), 
		VIDEO_MP4("video/mp4"),
		UNKNOWN("");
		
		String contentType;
		
		SlideShowMediaType(String contentType){
			this.contentType = contentType;
		}
		
		public static SlideShowMediaType getByContentType(String contentType) {
			return Arrays.asList(SlideShowMediaType.values())
					.stream()
					.filter((type)->contentType.equals(type.getContentType()))
					.findFirst()
					.orElse(SlideShowMediaType.UNKNOWN);
		}

		public String getContentType() {
			return contentType;
		}

		public void setContentType(String contentType) {
			this.contentType = contentType;
		}
		
		
}
	

