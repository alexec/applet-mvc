package com.alexecollins.appletmvc.core;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * A base class for all models.
 * Provides support for emitting events.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public abstract class AbstractModel implements Model {
    
    private final Set<ModelChangeListener> listeners = new CopyOnWriteArraySet<ModelChangeListener>();

    public void addListener(ModelChangeListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ModelChangeListener listener) {
        listeners.remove(listener);
    }

    protected void fireModelChanged() {
        for (ModelChangeListener listener : listeners) {
            listener.modelChanged(this);
        }
    }
}
