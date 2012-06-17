package com.alexecollins.appletmvc.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * Provides a skeleton of a view.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractView implements View {

    private final Set<ViewChangedListener> listeners = new CopyOnWriteArraySet<ViewChangedListener>();

    public void addViewChangedListener(ViewChangedListener listener) {
        listeners.add(listener);
    }

    public void removeViewChangedListener(ViewChangedListener listener) {
        listeners.remove(listener);
    }

    /**
     * Call this when the view has changed.
     */
    protected void fireViewChanged() {
        for (ViewChangedListener listener : listeners) {
            listener.viewChanged(this);
        }
    }
}
