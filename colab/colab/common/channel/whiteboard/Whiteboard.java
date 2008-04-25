package colab.common.channel.whiteboard;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.whiteboard.draw.Figure;
import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;

public class Whiteboard implements Drawable, Iterable<Layer> {

    private List<Layer> layers;

    private List<WhiteboardListener> whiteboardListeners;

    /**
     * Constructs an empty Whiteboard.
     */
    public Whiteboard() {

        this.layers = Collections
            .synchronizedList(new ArrayList<Layer>());
        this.whiteboardListeners = new ArrayList<WhiteboardListener>();

    }

    public void addFigure(final LayerIdentifier layerId,
            final Figure figure) {

        boolean layerFound = false;

        // Find the right layer
        for (Layer l : layers) {
            if (l.getId().equals(layerId)) {
                l.addFigure(figure);
                layerFound = true;
                break;
            }
        }

        if (!layerFound) {
            throw new IllegalArgumentException(
                    "No layer with id=" + layerId + " found!");
        }

        // Fire listener
        this.fireOnEdit(layerId);
    }

    public void insert(final LayerIdentifier previous,
            final Layer layer) throws NotApplicableException {
        synchronized(layers) {

            int offset = 0;
            if (previous != null) {
                boolean previousLayerFound = false;
                for (Layer l : layers) {
                    offset++;
                    if (l.getId().equals(previous)) {
                        previousLayerFound = true;
                        break;
                    }
                }
                if (!previousLayerFound) {
                    throw new NotApplicableException("No previous layer with id=" + previous);
                }
            }
            insert(offset, layer);
        }
    }

    public void insert(final int offset, final Layer layer) {

        if (layer == null) {
            throw new IllegalArgumentException("Can't insert a null layer");
        }

        synchronized(layers) {

            if (offset <= layers.size() && offset >= 0) {
                layers.add(offset, layer);
                fireOnInsert(offset, layer);
            }
        }
    }

    public void shift(final LayerIdentifier id, final int offset) {

        if(offset != 0) {
            synchronized(layers) {
                int index;
                Layer layer = null;

                for(index = 0; index<layers.size(); index++) {
                    if(layers.get(index).getId().equals(id)) {
                        layer = layers.get(index);
                        break;
                    }
                }

                if (layer != null) {
                    int newIndex = index + offset;

                    if (newIndex >= 0 && newIndex < layers.size()
                            && offset != 0) {
                        if (offset<0) {
                            layers.add(newIndex, layers.remove(index));
                        } else {
                            layers.add(newIndex-1, layers.remove(index));
                        }

                        fireOnShift(id, offset);
                    }
                }
            }
        }
    }

    public void delete(final LayerIdentifier id) {
        Layer toDelete = null;

        synchronized(layers){

            for(Layer l : layers) {
                if(l.getId().equals(id)) {
                    toDelete = l;
                    break;
                }
            }

            if(toDelete!=null) {
                layers.remove(toDelete);
                fireOnDelete(id);
            }
        }
    }

    public Whiteboard copy() {
        Whiteboard board = new Whiteboard();

        for(Layer layer : layers) {
            board.layers.add(layer.copy());
        }
        return board;
    }

    public Layer get(final LayerIdentifier id) {
        Layer layer = null;
        synchronized(layers) {

            Iterator<Layer> iter = layers.iterator();
            while(iter.hasNext()) {
                Layer next = iter.next();
                if(next.getId().equals(id)) {
                    layer = next;
                    break;
                }
            }
        }
        return layer;
    }

    public void addWhiteboardListener(final WhiteboardListener listener) {

        whiteboardListeners.add(listener);

    }

    public void removeWhiteboardListener(final WhiteboardListener listener) {
        whiteboardListeners.remove(listener);
    }

    protected void fireOnInsert(final int offset,
            final Layer layer) {

        for (final WhiteboardListener listener : whiteboardListeners) {
            listener.onInsert(offset, layer);
        }

    }

    protected void fireOnEdit(final LayerIdentifier id) {

        for (final WhiteboardListener listener : whiteboardListeners) {
            listener.onEdit(id);
        }

    }

    protected void fireOnDelete(final LayerIdentifier id) {

        for (final WhiteboardListener listener : whiteboardListeners) {
            listener.onDelete(id);
        }

    }

    protected void fireOnShift(final LayerIdentifier id, final int offset) {

        for (final WhiteboardListener listener : whiteboardListeners) {
            listener.onShift(id, offset);
        }

    }

    public void draw(final Graphics graphIn) {
        synchronized(layers) {
            for(int i = 0; i<layers.size(); i++) {
                layers.get(i).draw(graphIn);
            }
        }
    }

    public void drawLayer(final Graphics graphIn, final LayerIdentifier id) {
        get(id).draw(graphIn);
    }

    public BufferedImage exportImage() {
        java.awt.Rectangle bounds = null;
        for(Layer layer : layers) {
            if(bounds == null) {
                java.awt.Rectangle orig = layer.getBounds();
                bounds = new java.awt.Rectangle(
                        orig.x, orig.y, orig.width, orig.height);
            } else {
                bounds.add(layer.getBounds());
            }
        }

        BufferedImage image = new BufferedImage(
                bounds.width, bounds.height, BufferedImage.TYPE_INT_RGB);

        Graphics graphics = image.getGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, bounds.width, bounds.height);

        for(Layer layer : layers) {
            layer.draw(graphics);
        }

        return image;
    }

    /** {@inheritDoc} */
    public Iterator<Layer> iterator() {
        return layers.iterator();
    }

}
