package colab.common.channel.whiteboard;

import java.awt.Graphics;

/**
 * Anything that can draw itself onto a graphics object.
 */
public interface Drawable {

    /**
     * Draws the contents of the object onto the given graphics.
     *
     * @param graphIn the graphics object to draw on
     */
    void draw(Graphics graphIn);

}
