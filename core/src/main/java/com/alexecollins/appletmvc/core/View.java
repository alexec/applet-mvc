package com.alexecollins.appletmvc.core;

import java.awt.*;

/**
 * A view is like a page that is drawn onto a graphics.
 * The graphics will be related to an applet.
 *
 * @author alexec (alex.e.c@gmail.com)
 * @see AbstractView
 */
public interface View extends Activatable {
    /** Draw the view onto the graphics. */
    void draw(Graphics graphics) throws Exception;
    /** Add a listener for when the view changes. */
    void addViewChangedListener(ViewChangedListener listener);
    /** Remove a listener. */
    void removeViewChangedListener(ViewChangedListener listener);
}
