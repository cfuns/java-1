package de.benjaminborbe.vaadin.gui;

import com.vaadin.ui.Label;
import com.vaadin.ui.Window;

@SuppressWarnings("serial")
public class VaadinGuiApplication extends com.vaadin.Application {

	@Override
	public void init() {
		final Window window = new Window("My Window");
		window.addComponent(new Label("Hello World!"));
		window.addComponent(new Label("Hallo Welt!"));
		window.addComponent(new Label("Ben!"));
		setMainWindow(window);
	}

}
