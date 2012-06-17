package com.alexecollins.appletmvc.example.carousel;

import com.alexecollins.appletmvc.core.AbstractModel;
import com.alexecollins.appletmvc.core.Activatable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A model of the carousel, which emits events when the carousel has moved (event just a small amount).
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class CarouselModel extends AbstractModel implements Activatable {

    private final List<String> imageNames = new ArrayList<String>();
    /** the current image's index. */
    private int currentIndex = 0;
    /** 0..1, where we are in the animation loop. */
    private double t = 0.0;
    /** animation thread */
    private Thread thread;
    /** Whether we should keep animating. */
    private boolean animated = true;
    /** a lock for notifying when animated has changed */
    private final Object animateLock = new Object();

    public CarouselModel() throws InterruptedException {
        for (int i = 0; i < 3; i++) {
            imageNames.add("/photo" + i + ".jpg");
        }
    }

    public void activate() {
        thread = new Thread(new Runnable() {
            public void run() {
                try {
                    while (true) {
                        // show the full image for 1s
                        if (t == 0.0) {
                            Thread.sleep(1000);
                        }

                        Thread.sleep(50);

                        synchronized (animateLock) {
                            while (!animated) {
                                animateLock.wait();
                            }
                        }

                        if (animated) {
                            t += 0.05;
                        }

                        // if
                        if (t >= 1.0) {
                            currentIndex = (currentIndex + 1) % imageNames.size();
                            t = 0.0;
                        }

                        fireModelChanged();
                    }
                } catch (InterruptedException e) {
                    // nop
                }
            }
        });
        thread.start();
    }

    public void passivate() {
        thread.interrupt();
        thread = null;
    }

    public int getNextIndex() {
        return (currentIndex + 1) % imageNames.size();
    }

    public void playPause() {
        animated = !animated;
        synchronized (animateLock) {
            animateLock.notifyAll();
        }
        fireModelChanged();
    }

    public boolean isAnimated() {
        return animated;
    }

    public List<String> getImageNames() {
        return Collections.unmodifiableList(imageNames);
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public double getT() {
        return t;
    }
}
