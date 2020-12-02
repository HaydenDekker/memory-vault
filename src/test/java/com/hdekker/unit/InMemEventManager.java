package com.hdekker.unit;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.hdekker.media.slideshows.sessions.InMemAbstractMappedEventManager;
import com.vaadin.flow.shared.Registration;

@SpringBootTest
public class InMemEventManager {

	class OneEventType{
		String prop1;
	}
	
	class TwoEventType{
		String prop2;
	}
	
	class MyManager extends InMemAbstractMappedEventManager<String, OneEventType>{
		
	}
	
//	class MyMan2 extends InMemAbstractMappedEventManager<String, OneEventType>{
//		
//	}
//	
//	MyMan2 m2 = new MyMan2();
//	
	MyManager m = new MyManager();
	
	String testPropVal;
	
	@Test
	public void canAddRemoveListener() {
	
		l1HasRun = false;

		// Any identifier can be used as a mapping key, in this case a simple string
		Registration reg = m.addListener("Key Name 1", (e)->{ 
			// using a property to check the condition that the event has successfully triggered
			assertTrue(e.prop1 == testPropVal);
			 l1HasRun = true;
		});
		
		OneEventType e = new OneEventType();
		testPropVal = "Sweet this works";
		e.prop1 = testPropVal;
		
		m.fireListeners("Key Name 1", e);
		
		assertTrue(m.getListeners().get("Key Name 1").size()==1);
		
		reg.remove();
		
		assertTrue(m.getListeners().get("Key Name 1")==null);
		
	}
	
	Boolean l1HasRun;
	Boolean l2HasRun;
	
	@Test
	public void canAddRemoveMulipleListeners() {
		
		l1HasRun = false;
		l2HasRun = false;
		
		// Any identifier can be used as a mapping key, in this case a simple string
		Registration reg = m.addListener("Key Name 1", (e)->{ 
			// using a property to check the condition that the event has successfully triggered
			assertTrue(e.prop1 == testPropVal);
			l1HasRun = true;
		});
		
		Registration reg2 = m.addListener("Key Name 1", (e)->{ 
			// using a property to check the condition that the event has successfully triggered
			assertTrue(e.prop1 == testPropVal);
			l2HasRun = true;
		});
		
		OneEventType e = new OneEventType();
		testPropVal = "Sweet this works";
		e.prop1 = testPropVal;
		
		m.fireListeners("Key Name 1", e);
		
		assertTrue(l1HasRun);
		assertTrue(l2HasRun);
		
		assertTrue(m.getListeners().get("Key Name 1").size()==2);
		
		reg.remove();
		reg2.remove();
		
		assertTrue(m.getListeners().get("Key Name 1")==null);
				
		
	}

	
}
