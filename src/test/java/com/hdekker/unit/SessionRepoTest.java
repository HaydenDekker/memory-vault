package com.hdekker.unit;

import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.data.SSSession;
import com.hdekker.media.slideshows.sessions.SessionRepository;

@SpringBootTest
public class SessionRepoTest {

	@Autowired
	SessionRepository repo;
	
	@Test
	public void createThenDelete() {
		
		SSSession d1 = new SSSession();
		d1.setControllerSortKey("sdrdf");
		LocalDateTime testTime = LocalDateTime.now();
		d1.setLastModified(testTime);
		
		SSSession def = repo.create(d1);
		
		assertTrue(def.getKey().contains("SSSession"));
		assertTrue(testTime.equals(def.getLastModified()));
		
		repo.delete(def);
		
		List<SSSession> defs = repo.findAll();
		
		// make sure database is clean
		assertTrue(!defs.stream()
						.filter(d->"sdrdf".equals(d.getControllerSortKey()))
						.findAny()
						.isPresent());	
		
	}
	
	
}
