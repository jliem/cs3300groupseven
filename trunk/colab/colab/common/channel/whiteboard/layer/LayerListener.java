package colab.common.channel.whiteboard.layer;

import colab.common.channel.whiteboard.draw.Figure;

/**
 * An object which observes a Layer.
 */
public interface LayerListener {

    /**
     * Called when the layer's name changes.
     *
     * @param newLabel the name layer name
     */
    void onLabelChange(String newLabel);

    /**
     * Called when a figure is added to a layer.
     *
     * @param figure the new figure
     */
    void onFigureAdded(Figure figure);

}
