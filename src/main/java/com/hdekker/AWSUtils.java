package com.hdekker;

import java.io.IOException;
import java.util.function.Function;

import org.apache.commons.fileupload.util.Streams;

import com.amazonaws.services.s3.model.S3Object;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;

public interface AWSUtils {

	public static Function<S3Object, MemoryBuffer> getMemoryBuffer(){
		
		return (obj)->{
			MemoryBuffer buf = new MemoryBuffer();
	    	  try {
				Streams.copy(obj.getObjectContent(), buf.receiveUpload(obj.getKey(), obj.getObjectMetadata().getContentType()), true);
				obj.getObjectContent().close();
				
				} catch (IOException e) {
					
					e.printStackTrace();
				}
	    	  return buf;
		};
		
	}
	
}
