package com.alexecollins.appletmvc.core;

/**
 * A an object that can listen to a view.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public interface ViewChangedListener {
    /** The view is changed. */
    void viewChanged(View view);
}
