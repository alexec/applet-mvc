package com.alexecollins.appletmvc.core;

/**
 * Uses the command pattern to create a controller for an event.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public interface Controller  {

    /**
     * Execute the controller's action.
     */
    void execute() throws Exception;
}
