package com.hdekker.unit;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.controller.ControllerRepository;
import com.hdekker.media.slideshows.data.Controller;

@SpringBootTest
public class ControllerRepoTest {

	@Autowired
	ControllerRepository repo;
	
	@Test
	public void createThenDelete() {
		
		Controller d1 = new Controller();
		d1.setControllerName("Main");
		d1.setMediaDisplayComponentKeys(Arrays.asList("mdc1", "mdc2"));
		
		Controller def = repo.create(d1);
		
		assertTrue(def.getKey().contains("Controller"));
		
		repo.delete(def);
		
		List<Controller> defs = repo.findAll();
		
		// make sure database is clean
		assertTrue(!defs.stream()
						.filter(d->d.getControllerName().equals("Main"))
						.findAny()
						.isPresent());	
		
	}
	
}
