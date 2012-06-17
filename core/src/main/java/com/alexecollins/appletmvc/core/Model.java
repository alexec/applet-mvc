package com.alexecollins.appletmvc.core;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public interface Model {
     void addListener(ModelChangeListener listener);
     void removeListener(ModelChangeListener listener);
}
