package com.hdekker;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.junit.jupiter.api.Test;

public class MP4ReadTests {

	@Test
	public void canOpenMP4Files() {
		
		File mp4 = new File("src/test/resources/kk.mp4");
		try {
			FileInputStream in = new FileInputStream(mp4);
			assertTrue(in!=null);
			in.close();
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
