package com.alexecollins.appletmvc.example;

import com.alexecollins.appletmvc.core.Controller;
import com.alexecollins.appletmvc.core.DispatcherApplet;
import com.alexecollins.appletmvc.example.menu.MenuView;

/**
 * This changes the view to the carousel.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class DefaultController implements Controller {

    private final DispatcherApplet applet;

    public DefaultController(DispatcherApplet applet) {
        this.applet = applet;
    }

    public void execute() throws Exception {
        applet.setView(new MenuView(applet));
    }
}
