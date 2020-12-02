package com.hdekker.unit;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.data.MediaDisplayComponent;
import com.hdekker.media.slideshows.displays.DisplayRepo;

@SpringBootTest
public class DisplayRepoTest {

	@Autowired
	DisplayRepo repo;
	
	@Test
	public void createThenDelete() {
		
		MediaDisplayComponent d1 = new MediaDisplayComponent();
		d1.setDisplayName("Kitchen Display - Test");
		
		MediaDisplayComponent def = repo.create(d1);
		
		assertTrue(def.getKey().contains("MediaDisplay"));
		
		repo.delete(def);
		
		List<MediaDisplayComponent> defs = repo.findAll();
		
		// make sure database is clean
		assertTrue(!defs.stream()
						.filter(d->d.getDisplayName().equals("Kitchen Display - Test"))
						.findAny()
						.isPresent());	
		
	}
	
}
