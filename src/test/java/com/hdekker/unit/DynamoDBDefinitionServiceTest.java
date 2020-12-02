package com.hdekker.unit;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.SlideShowMediaType;
import com.hdekker.media.slideshows.data.Definition;
import com.hdekker.media.slideshows.data.Media;
import com.hdekker.media.slideshows.definition.DefinitionsRepository;
import com.hdekker.users.UserConfig;

/**
 * @author HDekker
 *
 */
@SpringBootTest
public class DynamoDBDefinitionServiceTest {
	
	@Autowired
	DefinitionsRepository service;
	
	Logger log = LoggerFactory.getLogger(DynamoDBDefinitionServiceTest.class);
	
	@Autowired
	UserConfig config;
	
//	@Test
//	public void listAllDefinitions() {
//		
//		List<Definition> defs = service.findAll();
//										
//		log.info(defs.toString());
//	}
	
//	@Test
//	public void createDefinition() {
//		
//		Definition d1 = new Definition();
//		//d1.setKey("hayden.dekker.test-definition");
//		d1.setDefinitionName("Our boy");
//		d1.setDefinitionDescription("Pictures of our Henry getting old.");
//		d1.setMedia(Arrays.asList(new Media("url1.jpg", SlideShowMediaType.IMAGE_JPEG), 
//				new Media("url2.jpg", SlideShowMediaType.IMAGE_JPEG)));
//		
//		Definition def = service.create(d1);
//		log.info("Test definition created with sortKey of " + def.getSortKey());
//		
//	}
	
//	@Test
//	public void updateDefinition() {
//		
//		List<Definition> defs = service.findAll();
//		defs.get(0).setDefinitionName("This one is changed");
//		service.update(defs.get(0));
//		Definition def = service.get(defs.get(0));
//		assertThat(def.getDefinitionName(), Is.is("This one is changed"));
//		
//	}
	
	@Test
	public void createThenDeleteDefinition() {
		
		Definition d1 = new Definition();
		d1.setDefinitionName("To Delete");
		d1.setDefinitionDescription("Pictures of our Henry getting old.");
		d1.setMedia(Arrays.asList(new Media("url1.jpg", SlideShowMediaType.IMAGE_JPEG), 
				new Media("url2.jpg", SlideShowMediaType.IMAGE_JPEG)));
		
		Definition def = service.create(d1);
		
		service.delete(def);
		
		List<Definition> defs = service.findAll();
													
		assertTrue(!defs.stream()
						.filter(d->d.getDefinitionName().equals("To Delete"))
						.findAny()
						.isPresent());	
		
	}
	
	
}
