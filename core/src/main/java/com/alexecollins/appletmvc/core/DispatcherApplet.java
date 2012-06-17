package com.alexecollins.appletmvc.core;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * This applet abstract applet tasks away from the views.
 * The default controller is executed on start, which should set a view, and we go from there.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class DispatcherApplet extends Applet implements ViewChangedListener {

    /** For double-buffering. */
    private Image buffer;
    /** The current view. */
    private View view;
    /** A snapshot of the applet when it is clean. */
    private AppletSnapshot cleanSnapshot;

    /** Get the starting view for the the applet. */
    private Controller getDefaultController() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, InstantiationException {
        final String parameter = getParameter("defaultController");

        if (parameter == null) {throw new IllegalStateException("defaultController not defined");}

        final Class<?> defaultClass = Class.forName(parameter);
        final Constructor<?>[] defaultConstructors = defaultClass.getConstructors();

        if (defaultConstructors.length != 1) {throw new IllegalStateException("ambiguous constructor");}

        final Constructor<?> constructor = defaultConstructors[0];

        final List<Object> initargs = new ArrayList<Object>();

        for (Class<?> clazz : constructor.getParameterTypes()) {
            if (Applet.class.isAssignableFrom(clazz)) {
                initargs.add(this);
            } else {
                throw new IllegalArgumentException("unknown constructor parameter type");
            }
        }

        return (Controller) constructor.newInstance(initargs.toArray());
    }

    @Override
    public void start() {
        super.start();

        try {
            getDefaultController().execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void paint(Graphics g) {
        // no buffer -> no view
        if (buffer == null) {return;}

        // double-buffering
        buffer.getGraphics().clearRect(0, 0, getWidth(), getHeight());

        if (view != null) {
            try {
                view.draw(buffer.getGraphics());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        g.drawImage(buffer, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        // double-buffering
        paint(g);
    }

    @Override
    public void stop() {
        setView(null);
        super.stop();
    }

    /**
     * Change to a new view.
     * The old view will be deactivated, and the new view activated.
     */
    public void setView(View view) {
        if (this.view == view) {return;}

        if (this.view != null) {
            try {
                this.view.passivate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            this.view.removeViewChangedListener(this);
        }

        this.view = null;

        // clean of dirt
        removeAll();
        if (cleanSnapshot != null) {
            restoreFromSnapshot(cleanSnapshot);
        } else {
            cleanSnapshot = new AppletSnapshot(this);
        }

        this.view = view;

        if (this.view != null) {
            this.view.addViewChangedListener(this);
            try {
                this.view.activate();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // fresh buffer with defaults
        buffer = createImage(getWidth(), getHeight());

        repaint();
    }

    /**
     * Restore the applet from a snapshot.
     * TODO - complete this code.
     * */
    private void restoreFromSnapshot(final AppletSnapshot snapshot) {

        setCursor(snapshot.getCursor());

        for (MouseListener mouseListener : getMouseListeners()) {
            removeMouseListener(mouseListener);
        }
        for (MouseListener mouseListener : snapshot.getMouseListeners()) {
            addMouseListener(mouseListener);
        }

        for (MouseMotionListener mouseMotionListener : getMouseMotionListeners()) {
            removeMouseMotionListener(mouseMotionListener);
        }
        for (MouseMotionListener mouseMotionListener : snapshot.getMouseMotionListeners()) {
            addMouseMotionListener(mouseMotionListener);
        }
    }

    public void viewChanged(View view) {
        if (view == this.view) {
            repaint();
        }
    }
}
