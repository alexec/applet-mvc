package com.alexecollins.appletmvc.example.menu;

import com.alexecollins.appletmvc.core.AbstractView;
import com.alexecollins.appletmvc.core.Controller;
import com.alexecollins.appletmvc.core.DispatcherApplet;
import com.alexecollins.appletmvc.example.beep.BeepView;
import com.alexecollins.appletmvc.example.carousel.CarouselModel;
import com.alexecollins.appletmvc.example.carousel.CarouselView;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author alexec (alex.e.c@gmail.com)
 */
public class MenuView extends AbstractView implements MouseListener, MouseMotionListener {

    private class MenuOption implements Comparable {
        final String text;
        final Controller controller;
        Rectangle rectangle;

        private MenuOption(String text, Controller controller) {
            this.text = text;
            this.controller = controller;
        }

        public int compareTo(Object o) {
            return text.compareTo(((MenuOption)o).text);
        }
    }


    private final DispatcherApplet applet;
    private final MediaTracker mediaTracker;
    private final Image background;
    /** Option on the menu, ordered. */
    private final Set<MenuOption> menuOptions = new TreeSet<MenuOption>();
    /** The option the user is hovering on. */
    private MenuView.MenuOption highlighted;

    public MenuView(DispatcherApplet applet1) {
        this.applet = applet1;

        mediaTracker = new MediaTracker(applet);

        background = applet.getImage(getClass().getResource("/menu_bg.jpg"));

        mediaTracker.addImage(background, 0);

        menuOptions.add(new MenuOption("Carousel", new Controller() {
            public void execute() throws Exception {
                applet.setView(new CarouselView(applet, new CarouselModel()));
            }
        }));

        menuOptions.add(new MenuOption("Beep", new Controller() {
            public void execute() throws Exception {
                applet.setView(new BeepView(applet));
            }
        }));
    }

    public void draw(Graphics graphics) throws Exception {

        if (graphics instanceof  Graphics2D) {
            ((Graphics2D) graphics).setRenderingHint(
                    RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        graphics.setFont(new Font("Arial Black", Font.PLAIN, 24));

        graphics.drawImage(background, 0, 0, null);

        final int padding = 20;
        // options
        {
            Rectangle last = null;
            int i = 1;
            for (MenuOption option : menuOptions) {
                final String str = i + ". " + option.text;  // prepend the number
                option.rectangle = getTextRectangle(graphics, str, padding, last != null ?
                    last.y + last.height + 1 : padding);
                graphics.setColor(highlighted == option ? Color.WHITE : Color.BLACK);
                graphics.drawString(str, option.rectangle.x, option.rectangle.y + graphics.getFontMetrics().getAscent());
                last = option.rectangle;
                i++;
            }
        }

        // title
        {
            final String title = "Menu";
            final int w = graphics.getFontMetrics().stringWidth(title);
            graphics.setColor(Color.WHITE);
            graphics.drawString(title, applet.getWidth() - w - padding, 250);
        }
    }

    /**
     * Get the rect containing text drawn at x,y.
     */
    private static Rectangle getTextRectangle(Graphics graphics, String str, int x, int y) {
        final FontMetrics metrics = graphics.getFontMetrics();
        final int w = metrics.stringWidth(str);
        final int h = metrics.getHeight();
        return new Rectangle(x, y, w, h);
    }

    public void activate() throws Exception {
        mediaTracker.waitForAll();
        applet.addMouseListener(this);
        applet.addMouseMotionListener(this);
    }

    public void passivate() throws Exception {}

    public void mouseClicked(MouseEvent e) {
        // choose an option
        for (MenuOption option : menuOptions) {
            if (option.rectangle.contains(e.getX(), e.getY())) {
                try {
                    option.controller.execute();
                } catch (Exception e1) {
                    throw new RuntimeException(e1);
                }
                return;
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        // nop.
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

    public void mouseDragged(MouseEvent e) {
        // nop
    }

    public void mouseMoved(MouseEvent e) {
        updateHighlighted(e.getPoint());
    }

    /** Update highlighted option. */
    private void updateHighlighted(final Point point) {
        // figure out which one is highlighted
        highlighted = null;
        for (MenuOption option : menuOptions) {
            if (option.rectangle.contains(point)) {
                highlighted = option;
                break;
            }
        }
        applet.setCursor(Cursor.getPredefinedCursor(highlighted != null ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        applet.repaint();
    }
}
