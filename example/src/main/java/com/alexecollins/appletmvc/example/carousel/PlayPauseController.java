package com.alexecollins.appletmvc.example.carousel;

import com.alexecollins.appletmvc.core.Controller;

/**
 * This controller stop/starts the rotating of the applet.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class PlayPauseController implements Controller {
    private final CarouselModel model;

    public PlayPauseController(final CarouselModel model) {
        this.model = model;
    }

    public void execute() throws Exception {
        model.playPause();
    }
}
