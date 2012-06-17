package com.alexecollins.appletmvc.example.beep;

import com.alexecollins.appletmvc.core.AbstractView;
import com.alexecollins.appletmvc.core.Controller;
import com.alexecollins.appletmvc.core.DispatcherApplet;

import java.applet.AudioClip;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class BeepView extends AbstractView implements MouseListener {
    private final DispatcherApplet applet;
    private final MediaTracker mediaTracker;
    private final Image background;
    private final AudioClip beep;
    private final Controller beepController = new Controller() {
        public void execute() throws Exception {
            beep.play();
        }
    };

    public BeepView(DispatcherApplet applet1) {
        this.applet = applet1;

        mediaTracker = new MediaTracker(applet);

        background = applet.getImage(getClass().getResource("/beep_bg.jpg"));
        mediaTracker.addImage(background, 0);

        beep = applet.getAudioClip(getClass().getResource("/beep.wav"));
    }

    public void draw(Graphics graphics) throws Exception {

        if (graphics instanceof  Graphics2D) {
            ((Graphics2D) graphics).setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        graphics.setFont(new Font("Arial Black", Font.BOLD, 48));
        graphics.setColor(Color.WHITE);

        // background
        {
            graphics.drawImage(background, 0, 0, null);
        }

        // draw "beep" text - centred
        {
            final String str = "Beep!";
            final FontMetrics metrics = graphics.getFontMetrics();
            final int w = metrics.stringWidth(str);
            final int h = metrics.getHeight();
            graphics.drawString(str, (applet.getWidth() - w) / 2, (applet.getHeight() - h) / 2 + metrics.getAscent());
        }
    }

    public void activate() throws Exception {
        applet.addMouseListener(this);
        applet.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        mediaTracker.waitForAll();
    }

    public void passivate() throws Exception {
    }

    public void mouseClicked(MouseEvent e) {
        try {
            beepController.execute();
        } catch (Exception e1) {
            throw new RuntimeException(e1);
        }
    }

    public void mousePressed(MouseEvent e) {
        // nop
    }

    public void mouseReleased(MouseEvent e) {
        // nop
    }

    public void mouseEntered(MouseEvent e) {
        // nop
    }

    public void mouseExited(MouseEvent e) {
        // nop
    }
}
