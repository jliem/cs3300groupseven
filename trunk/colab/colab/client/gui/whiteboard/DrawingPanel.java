package colab.client.gui.whiteboard;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import colab.client.gui.whiteboard.WhiteboardChannelPanel.ToolType;
import colab.common.channel.whiteboard.draw.Drawable;
import colab.common.channel.whiteboard.draw.Point;

/**
 * Drawing panel for whiteboard.
 */
public class DrawingPanel extends JPanel implements MouseInputListener {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private static final int BOUNDS_MARGIN = 5;
    private static final Paint PEN_PAINT = Color.BLACK;
    private static final Color BACKGROUND_COLOR = Color.WHITE;

    private static final int ARRAYLIST_INITIAL_CAPACITY = 800;
    private static final int DRAWABLELIST_INITIAL_CAPACITY = 50;

    private int penThickness = 3;

    private BufferedImage imgBuffer = null;
    private Point oldPoint = null;
    private boolean drawingNewSegment, dirtyBounds, isActive;

    private Vector<Drawable> drawables;

    private ArrayList<Point> list;
    private int left, right, top, bottom;

    private ToolType tool;

    public DrawingPanel() {
        this(null);
    }

    public DrawingPanel(final BufferedImage image) {
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(BACKGROUND_COLOR);

        isActive = false;
        list = new ArrayList<Point>(ARRAYLIST_INITIAL_CAPACITY);
        drawables = new Vector<Drawable>(DRAWABLELIST_INITIAL_CAPACITY);
        dirtyBounds = true;
        drawingNewSegment = true;

        tool = ToolType.PATH;

        imgBuffer = image;

        if (imgBuffer != null) {
            setPreferredSize(new Dimension(
                    imgBuffer.getWidth(), imgBuffer.getHeight()));
        }

    }

    public void setToolType(final ToolType type) {
        this.tool = type;
    }

    /**
     * <P>Set the pen thickness.</P>
     * @param thickness the pen thickness value
     */
    public void setThickness(final int thickness) {
        penThickness = thickness;
    }

    /**
     * Clear the drawing field and all saved points.
     *
     */
    public void clear() {
        imgBuffer = null;
        oldPoint = null;
        dirtyBounds = true;
        isActive = false;
        list.clear();
        repaint();
    }

    /**
     * Load a new image to be displayed in this Drawing Field.
     *
     * @param image the image to load
     */
    public void loadImage(final BufferedImage image) {
        imgBuffer = image;
    }

    /**
     * <P>Scales a BufferedImage.</P>
     * <P>To maintain the original aspect ratio, make either width
     * or height a negative number.</P>
     * @param source the original image to scale
     * @param width the new width
     * @param height the new height
     * @return a new BufferedImage which is the original source image scaled
     */
    private BufferedImage scale(final BufferedImage source, final int width,
            final int height) {

        return toBufferedImage(source.getScaledInstance(width, height,
                Image.SCALE_SMOOTH));

    }

    /**
     * Scales a BufferedImage with an option to preserve the original aspect
     * ratio.
     *
     * This method guarantees that if aspect ratio preservation is selected, the
     * returned image will have a smaller or equal width and a smaller or equal
     * height than the parameters. If preservation is not selected, the returned
     * image will have width and height equal to the parameters specified.
     *
     * @param source the original image to scale
     * @param width the new maximum width
     * @param height the new maximum height
     * @param preserveAspectRatio whether the aspect ratio should be preserved
     *                            when scaling
     * @return a BufferedImage which is the original source image scaled to the
     *         specified width and height or smaller, and whose aspect ratio is
     *         preserved. If the image did not need to be scaled because it was
     *         smaller than the given width and height already, a reference to
     *         the same image is returned. In all other cases a new
     *         BufferedImage object will be created.
     */
    public BufferedImage scale(final BufferedImage source, final int width,
            final int height, final boolean preserveAspectRatio) {

        if (preserveAspectRatio) {
            /* Resizing algorithm as follows:
             *
             * 1) Check whether the image is within the bounds. If so, return.
             * 2) Find the larger of width and height.
             *  2a) If the larger dimension is outside the bounds, scale using
             *      that.
             *  2b) Else, scale using the smaller dimension (one of these must
             *      be true, or case 1 would have applied.)
             * 3) It is now possible the dimension not scaled is outside the
             *    bounds (demonstrate by drawing a square), so check both and
             *    repeat the process if needed. */

            // First figure out whether we'll need to scale at all
            if (source.getWidth() <= width && source.getHeight() <= height) {
                return source;
            }

            BufferedImage result;


            if ((source.getWidth() > source.getHeight()
                    && source.getWidth() > width)
                    || (source.getHeight() <= height)) {
                result = scale(source, width, -1);
            } else {
                result = scale(source, -1, height);
            }

            // Check that the bounds of this image are ok; if not, send it back
            if (result.getWidth() > width || result.getHeight() > height) {
                return scale(result, width, height, preserveAspectRatio);
            } else {
                return result;
            }
        } else {
            return scale(source, width, height);
        }
    }

    /**
     * Converts an Image to a BufferedImage.
     *
     * From http://javaalmanac.com/egs/java.awt.image/Image2Buf.html
     *
     * @param image the Image to convert to a BufferedImage
     * @return a buffered image with the contents of an image
     */
    private BufferedImage toBufferedImage(final Image inputImage) {
        if (inputImage instanceof BufferedImage) {
            return (BufferedImage) inputImage;
        }

        // This code ensures that all the pixels in the image are loaded
        Image image = new ImageIcon(inputImage).getImage();

        // Create a buffered image with a format that's compatible with
        // the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge =
            GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (final HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            bimage = new BufferedImage(
                    image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
    }

    /**
     * <P>Calculate the image bounds by determining the outermost points.</P>
     * <P>Sets global variables containing bounds values.</P>
     *
     * @return true if the bounds were successfully calculated, false otherwise
     * (i.e. the list of points is empty because the user has not drawn
     * anything)
     *
     */
    public boolean calculateBounds() {

        // Determine the bounds
        // We need to find the left, top, right, bottom

        if (list.size() <= 0) {
            return false;
        }

        // If the bounds do not need to be re-calculated, exit at once
        if (!dirtyBounds) {
            return true;
        }

        left = getWidth();
        right = 0;
        top = getHeight();
        bottom = 0;

        for (int i=0; i<list.size(); i++) {
            Point p = (Point)list.get(i);

            if (p.x < left) {
                left = p.x;
            }

            if (p.x > right) {
                right = p.x;
            }

            if (p.y < top) {
                top = p.y;
            }

            if (p.y > bottom) {
                bottom = p.y;
            }
        }

        // Adjust the bounds to allow for a margin on all sides
        left -= BOUNDS_MARGIN;
        right += BOUNDS_MARGIN;
        top -= BOUNDS_MARGIN;
        bottom += BOUNDS_MARGIN;

        // Adjust for pen thickness, probably unnecessary
        left -= penThickness/2;
        right += penThickness/2;
        top -= penThickness/2;
        bottom += penThickness/2;

        // Check that bounds are not outside entire picture
        if (left < 0) {
            left = 0;
        }
        if (right > getWidth()) {
            right = getWidth();
        }
        if (top < 0) {
            top = 0;
        }
        if (bottom > getHeight()) {
            bottom = getHeight();
        }

        dirtyBounds = false;
        return true;
    }

    /**
     * Crops the image based on the global bounds.
     *
     * assumes calcBounds() has already been successfully called.
     *
     * @return the cropped image
     */
    public BufferedImage cropImage() {

        BufferedImage result = new BufferedImage(
                right-left, bottom-top, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = result.createGraphics();

        try {
            g2.drawImage((getImage()).getSubimage(
                    left, top, right-left, bottom-top),
                    null, 0, 0);
        } catch (RasterFormatException re) {
            JOptionPane.showMessageDialog(this,
                    "Bad raster\n"
                    + re.getStackTrace(),
                    "Invalid Arguments", JOptionPane.WARNING_MESSAGE);
            re.printStackTrace();
        }

        g2.dispose();

        return result;
    }

    public BufferedImage getImage() {

        if ((imgBuffer == null)
                || ((imgBuffer.getWidth() != getWidth()
                        || imgBuffer.getHeight() != getHeight()))) {

            BufferedImage old = imgBuffer;
            //imgBuffer = new BufferedImage(
            //      getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB_PRE);
            imgBuffer = new BufferedImage(
                    getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

            Graphics g = imgBuffer.getGraphics();
            g.setColor(BACKGROUND_COLOR);
            g.fillRect(0, 0, getWidth(), getHeight());

            if (old != null) {
                ((Graphics2D) imgBuffer.getGraphics()).drawRenderedImage(old,
                    AffineTransform.getTranslateInstance(0, 0));
            }
        }

        return imgBuffer;
    }

    protected void paintComponent(final Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRenderedImage(getImage(),
                AffineTransform.getTranslateInstance(0, 0));
    }

    public void mouseClicked(final MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON1) {
            isActive = true;
            mouseDragged(me);
            mouseReleased(me);
        }
    }

    public void mousePressed(final MouseEvent me) {
        if (me.getButton() == MouseEvent.BUTTON1) {
            // Whenever the user clicks, save the point
            oldPoint = new Point(me.getPoint());
            drawingNewSegment = true;
            dirtyBounds = true;
            isActive = true;
        }
    }

    public void mouseReleased(final MouseEvent me) {
        // When the user lifts the mouse button, release the old saved data
        if (me.getButton() == MouseEvent.BUTTON1) {
            oldPoint = null;
            isActive = false;
        }

        // Refresh the entire screen if the user pauses while drawing
        repaint();
    }

    public void mouseMoved(final MouseEvent me) {}

    public void mouseEntered(final MouseEvent me) {}

    public void mouseExited(final MouseEvent me) {
        // Refresh if the mouse leaves the field
        repaint();
    }

    public void mouseDragged(final MouseEvent me) {
        if (isActive) {
            Point p = new Point(me.getPoint());
            //System.out.println(p);
            // Check if there is an original point from which to start drawing
            if (oldPoint == null) {
                oldPoint = p;
            }

            Graphics g = getImage().getGraphics();

            ((Graphics2D) g).setPaint(PEN_PAINT);
            ((Graphics2D) g).setStroke(new BasicStroke(penThickness));

            g.drawLine(oldPoint.x, oldPoint.y, p.x, p.y);


            if (drawingNewSegment) {
                list.add(new Point(oldPoint.x, oldPoint.y));
            }

            list.add(new Point(p.x, p.y));

            /*
             * Repaint only from the left- and top-bound points.
             * (More efficient than repainting the entire
             * region with repaint()).
             */

            // Component#repaint(x, y, width, height)
             repaint(Math.min(p.x, oldPoint.x)-penThickness,
                    Math.min(p.y, oldPoint.y)-penThickness,
                    Math.max(p.x, oldPoint.x)+penThickness,
                    Math.max(p.y, oldPoint.y)+penThickness);


            // Save this point
            oldPoint = p;
            drawingNewSegment = false;

        }
    }

}
