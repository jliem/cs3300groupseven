package colab.common.channel.whiteboard.layer;

import colab.common.channel.whiteboard.draw.Figure;

public interface LayerListener {

    void onLabelChange(String newLabel);

    void onFigureAdded(Figure figure);

}
