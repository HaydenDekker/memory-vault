package com.hdekker.database;

import java.util.List;

import com.hdekker.mobileapp.config.AppLayoutConfig;
import com.hdekker.mobileapp.layout.ApplicationSideBarLayout;
import com.hdekker.mobileapp.navigation.MenubarNavigable;
import com.vaadin.flow.component.page.Push;

@Push
public class PushLayout extends ApplicationSideBarLayout{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PushLayout(List<MenubarNavigable> views, AppLayoutConfig appLayoutConfig) {
		super(views, appLayoutConfig);
		
	}

}
