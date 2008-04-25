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

    /**
     * Constructs an empty LockLayer.
     */
    public LockLayer() {
    }

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

        Layer layer = whiteboard.get(getLayerId());

        if (layer == null) {
            throw new NotApplicableException(
                    "Layer " + getLayerId().toString()
                    + " was not found in the whiteboard.");
        }

        if(lockHolder != null) {
            if (!layer.isUnlocked()) {
                throw new NotApplicableException(
                        "Layer " + getLayerId().toString()
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

    public LockLayer copy() {
        UserName username = null;
        if (super.getCreator() != null) {
            username = new UserName(super.getCreator().getValue());
        }

        UserName lockHolder = null;
        if (this.lockHolder != null) {
            lockHolder = new UserName(this.lockHolder.getValue());
        }

        LayerIdentifier li = null;
        if (getLayerId() != null) {
            li = new LayerIdentifier(getLayerId().getValue());
        }

        LockLayer copy = new LockLayer(username, super.getTimestamp(),
                li, lockHolder);

        return copy;
    }

}
