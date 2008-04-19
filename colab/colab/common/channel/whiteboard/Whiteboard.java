package colab.common.channel.whiteboard;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;

public class Whiteboard implements Drawable {

    private List<Layer> layers;

    private List<WhiteboardListener> whiteboardListeners;

    public Whiteboard() {

        this.layers = Collections
            .synchronizedList(new ArrayList<Layer>());
        this.whiteboardListeners = new ArrayList<WhiteboardListener>();

    }

    public void insert(final LayerIdentifier previous,
            final Layer layer) throws NotApplicableException {
        synchronized(layers) {

            int offset = 0;
            if (previous != null) {
                boolean offsetFound = false;
                for (Layer l : layers) {
                    offset++;
                    if (l.getId().equals(previous)) {
                        offsetFound = true;
                        break;
                    }
                }
                if (!offsetFound) {
                    throw new NotApplicableException();
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
        // TODO deep copy
        return null;
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

    public void addDocumentListener(final WhiteboardListener listener) {

        whiteboardListeners.add(listener);

    }

    public void removeDocumentListener(final WhiteboardListener listener) {
        whiteboardListeners.remove(listener);
    }

    protected void fireOnInsert(final int offset,
            final Layer layer) {

        for (final WhiteboardListener listener : whiteboardListeners) {
            listener.onInsert(offset, layer);
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
}
