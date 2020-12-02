package com.hdekker.ui.definitions;

import java.util.Optional;

import com.hdekker.media.slideshows.data.Definition;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

@SpringComponent
@UIScope
public class DefinitionUIState {

	Optional<Definition> selectedDefinition = Optional.empty();

	public Optional<Definition> getSelectedDefinition() {
		return selectedDefinition;
	}

	public void setSelectedDefinition(Optional<Definition> selectedDefinition) {
		this.selectedDefinition = selectedDefinition;
	}

	Class<? extends Component> cancelButtonNavTarget;

	public Class<? extends Component> getCancelButtonNavTarget() {
		return cancelButtonNavTarget;
	}

	public void setCancelButtonNavTarget(Class<? extends Component> cancelButtonNavTarget) {
		this.cancelButtonNavTarget = cancelButtonNavTarget;
	}
	
	
	
}
