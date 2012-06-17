package com.alexecollins.appletmvc.example.carousel;

import com.alexecollins.appletmvc.core.AbstractModel;
import com.alexecollins.appletmvc.core.AbstractView;
import com.alexecollins.appletmvc.core.Model;
import com.alexecollins.appletmvc.core.ModelChangeListener;

import java.applet.Applet;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

/**
 * This view displays a rotating carousel of images.
 *
 * @author alexec (alex.e.c@gmail.com)
 */
public class CarouselView extends AbstractView implements MouseListener, ModelChangeListener {

    private final CarouselModel model;
    /** Used for loading images. */
    private final MediaTracker mediaTracker;
    /** The containing applet. */
    private final Applet applet;
    private final Map<Integer, Image> indexToImage = new HashMap<Integer, Image>();
    private final PlayPauseController playPauseController;
    private boolean mouseOver;

    public CarouselView(final Applet applet, final CarouselModel model) throws InterruptedException {
        this.applet = applet;
        this.model = model;
        mediaTracker = new MediaTracker(applet);
        playPauseController = new PlayPauseController(this.model);
    }

    public void draw(final Graphics graphics) throws Exception {

        if (graphics instanceof  Graphics2D) {
            ((Graphics2D) graphics).setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        graphics.setFont(new Font("Arial", Font.PLAIN, 12));
        graphics.setColor(Color.BLACK);

        mediaTracker.waitForID(model.getCurrentIndex());

        final String currentImageName = model.getImageNames().get(model.getCurrentIndex());

        // draw current image
        {
            final double t = model.getT();
            // 1 dimensional cubic bezier curve
            final int x = -(int)(
                    (
                            Math.pow(1 - t, 3) * 0.0 +
                                    3 * Math.pow(1 - t, 2) * t * 0 +
                                    3 * (1 - t) * Math.pow(t, 2) * 1.0 +
                                    Math.pow(t, 3) * 1.0
                    ) * applet.getWidth()
            );

            graphics.drawImage(getImage(model.getCurrentIndex()), x, 0, null);

            // draw next image
            {
                final int nextIndex = model.getNextIndex();

                graphics.drawImage(getImage(nextIndex), x + applet.getWidth(), 0, null);
            }
        }

        // draw image title
        {
            final int p = 2; // padding
            final int m = 2; // margin
            final int h = graphics.getFontMetrics().getHeight() + 2 * p;
            final int w = graphics.getFontMetrics().stringWidth(currentImageName) + 2 * p;
            final int x = m;
            final int y = applet.getHeight() - m - h;
            graphics.setColor(new Color(0xff, 0xff, 0xff, 0x40));
            graphics.fillRect(x, y, w, h);
            graphics.setColor(new Color(0x00, 0x00, 0x00, 0xa0));
            graphics.drawString(currentImageName, x + p, y + p + graphics.getFontMetrics().getAscent());
        }

        // draw play/pause image
        if (mouseOver) {
            final String m = model.isAnimated() ? "Pause" : "Play";
            final int p = 2; // padding
            final int h = graphics.getFontMetrics().getHeight() + 2 * p;
            final int w = graphics.getFontMetrics().stringWidth(m) + 2 * p;
            final int x = (applet.getWidth() - w) / 2;
            final int y = (applet.getHeight() - h) / 2;
            graphics.setColor(new Color(0xff, 0xff, 0xff, 0x80));
            graphics.fillRect(x, y, w, h);
            graphics.setColor(Color.BLACK);
            graphics.drawString(m, x + p, y + p + graphics.getFontMetrics().getAscent());
        }
    }

    /** Get the image at the index. */
    private Image getImage(int index) throws InterruptedException {
        synchronized (indexToImage) {
            if (!indexToImage.containsKey(index)) {
                final Image image = applet.getImage(getClass().getResource("/photo" + index + ".jpg"));
                mediaTracker.addImage(image, index);
                indexToImage.put(index, image);
                mediaTracker.waitForID(index);
            }
            return indexToImage.get(index);
        }
    }

    public void activate() throws Exception {
        model.addListener(this);
        model.activate();
        applet.addMouseListener(this);
        applet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mouseOver = applet.contains(MouseInfo.getPointerInfo().getLocation());
    }

    public void passivate() throws Exception {
        model.passivate();
        model.removeListener(this);
    }

    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        fireViewChanged();
    }

    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
        fireViewChanged();
    }

    public void mouseReleased(MouseEvent e) {
        //nop
    }

    public void mousePressed(MouseEvent e) {
        //nop
    }

    public void mouseClicked(MouseEvent e) {
        // turn the animation on/off
        try {
            playPauseController.execute();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    public void modelChanged(Model model) {
        fireViewChanged();
    }
}
