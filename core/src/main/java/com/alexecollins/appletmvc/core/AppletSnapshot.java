package com.alexecollins.appletmvc.core;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Arrays;

/**
 * A snapshot of applet state.
 *
 * TODO - complete code.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
class AppletSnapshot {

    private final Cursor cursor;
    private final MouseListener[] mouseListeners;
    private final MouseMotionListener[] mouseMotionListeners;

    AppletSnapshot(final Applet applet) {
        cursor = Cursor.getPredefinedCursor(applet.getCursor().getType());
        mouseListeners = Arrays.copyOf(applet.getMouseListeners(), applet.getMouseListeners().length);
        mouseMotionListeners = Arrays.copyOf(applet.getMouseMotionListeners(), applet.getMouseMotionListeners().length);
    }

    Cursor getCursor() {
        return cursor;
    }

    MouseListener[] getMouseListeners() {
        return mouseListeners;
    }

    MouseMotionListener[] getMouseMotionListeners() {
        return mouseMotionListeners;
    }
}
