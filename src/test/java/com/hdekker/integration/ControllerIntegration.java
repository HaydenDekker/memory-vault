package com.hdekker.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.web.WebAppConfiguration;

import com.github.mvysny.kaributesting.v10.MockVaadin;
import com.github.mvysny.kaributesting.v10.Routes;
import com.github.mvysny.kaributesting.v10.spring.MockSpringServlet;
import com.hdekker.media.slideshows.controller.ControllerRepository;
import com.hdekker.media.slideshows.controller.ControllerService;
import com.hdekker.media.slideshows.data.Controller;
import com.hdekker.mobileapp.navigation.components.AcceptButton;
import com.hdekker.mobileapp.navigation.components.AddButton;
import com.hdekker.ui.controller.ControllerDetails;
import com.hdekker.ui.controller.ControllerList;
import com.hdekker.ui.controller.ControllerOverview;
import com.hdekker.ui.controller.ControllerUIState;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.spring.SpringServlet;

import kotlin.jvm.functions.Function0;

import static com.github.mvysny.kaributesting.v10.GridKt.*;
import static com.github.mvysny.kaributesting.v10.LocatorJ.*;

@SpringBootTest
@WebAppConfiguration
@DirtiesContext
public class ControllerIntegration {
	
	private static Routes routes = null;
	
    @BeforeAll
    public static void createRoutes() {
        routes = new Routes().autoDiscoverViews("com.hdekker.ui");
    } 

    @BeforeEach
    public void setupVaadin() { 
        
    	final Function0<UI> uiFactory = UI::new;
        final SpringServlet servlet = new MockSpringServlet(routes, ctx, uiFactory);
        MockVaadin.setup(uiFactory, servlet);
        
    }
    
    @Autowired
    private ApplicationContext ctx;
    
    @Autowired
    ControllerRepository repo;
	
	/**
	 * 
	 */
    
    @Autowired
	public ControllerService controllerService;
    
	@Test
	public void uiAddsANewController() {
		
		UI.getCurrent().navigate(ControllerList.class);
		
		// Screen 1 List
		_get(AddButton.class).click();
		ControllerDetails controller = _get(ControllerDetails.class);
		assertTrue(controller!=null);
		ControllerUIState state = ctx.getBean(ControllerUIState.class);
		assertTrue(state.getActiveController().isEmpty());
		assertTrue(state.getPreviousNavigationTarget().isPresent());
		
		// Screen 2 - Details
		_get(TextField.class, spec->spec.withCaption("Controller Name")).setValue("test-controller");
		_get(AcceptButton.class).click();
		
		Controller contEntity = repo.findAll().stream()
								.filter(c->c.getControllerName().equals("test-controller"))
								.findFirst().get();
		// Test it's stored on the database
		assertTrue(contEntity.getKey().contains("Controller"));
		
		// Test state has updated
		assertTrue(state.getActiveController().get().getKey().contains("Controller"));
		
		// Test navigation to overview
		_get(ControllerOverview.class);
		
		// Done - clean up
		controllerService.delete(contEntity);
		
	}
	
	/**
	 * 
	 */
	@Test
	public void uiSelectsExistingController() {
		
		UI.getCurrent().navigate(ControllerList.class);
		ControllerList ui = _get(ControllerList.class);
		Controller mustHave = ui.controllersList.stream().filter(f->f.getControllerName().contains("test-before")).findFirst().get();
		Grid<Controller> cg = _get(Grid.class);
		_clickItem(cg, ui.controllersList.indexOf(mustHave));
		_get(ControllerOverview.class);
		
	}
	
	@Test
	public void displaysActiveSessions() {
		fail();
	}
	
	
	String testControllerKey;
	
	@BeforeEach
	public void addATestEntity() {
		List<Controller> controllers = controllerService.findAll();
		controllers.stream().filter(f->f.getControllerName().contains("test-before")).findFirst()
							.ifPresentOrElse((x)->{testControllerKey = x.getKey();}, ()->controllerService.createNewController("test-before"));
	}
	
	@AfterEach
	public void deleteTestEntity() {
		List<Controller> controllers = controllerService.findAll();
		controllers.stream().filter(f->f.getControllerName().contains("test-before")).findFirst()
							.ifPresentOrElse((x)->{controllerService.delete(x);}, ()->{});
	}
	
	
}
