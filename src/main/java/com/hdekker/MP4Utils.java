package com.hdekker;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.Optional;
import java.util.function.Function;

import org.mp4parser.IsoFile;
import org.mp4parser.boxes.iso14496.part12.MovieHeaderBox;
import org.mp4parser.tools.Path;

public interface MP4Utils {

	static Function<InputStream, Optional<Long>> getMovieDuration(){
		return (is)->{
			IsoFile isoFile = null;
			try {
				isoFile = new IsoFile(Channels.newChannel(is));
				MovieHeaderBox headerBox = Path.getPath(isoFile, "moov/mvhd");
				isoFile.close();
				return Optional.of(headerBox.getDuration());
			} catch (IOException e) {
				e.printStackTrace();
				return Optional.empty();
			}
			
		};
	}
	
}
