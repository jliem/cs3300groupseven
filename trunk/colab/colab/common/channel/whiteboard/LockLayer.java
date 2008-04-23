package colab.common.channel.whiteboard;

import java.util.Date;

import colab.common.channel.whiteboard.layer.Layer;
import colab.common.channel.whiteboard.layer.LayerIdentifier;
import colab.common.exception.NotApplicableException;
import colab.common.naming.UserName;

public class LockLayer extends WhiteboardChannelData {

    /** Serialization version number. */
    public static final long serialVersionUID = 1L;

    private UserName lockHolder;

    public LockLayer(final UserName creator, final Date timestamp,
            final LayerIdentifier layerId,
            final UserName lockHolder) {
        super(creator, timestamp, layerId);
        this.lockHolder = lockHolder;
    }

    /** {@inheritDoc} */
    @Override
    public void apply(final Whiteboard whiteboard)
            throws NotApplicableException {

        Layer layer = whiteboard.get(super.layerId);

        if (layer == null) {
            throw new NotApplicableException(
                    "Layer " + super.layerId.toString()
                    + " was not found in the whiteboard.");
        }

        if(lockHolder != null) {
            if (!layer.isUnlocked()) {
                throw new NotApplicableException(
                        "Layer " + super.layerId.toString()
                        + " is not unlocked.");
            } else {
                layer.lock(lockHolder);
            }
        } else {
            layer.unlock();
        }
    }

    /** {@inheritDoc} */
    public String xmlNodeName() {
        return "Lock";
    }

}
