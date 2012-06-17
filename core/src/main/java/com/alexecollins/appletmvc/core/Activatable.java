package com.alexecollins.appletmvc.core;

/**
 * A object that has can be activated or passivated.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public interface Activatable {
    void activate() throws Exception;
    void passivate() throws Exception;
}
