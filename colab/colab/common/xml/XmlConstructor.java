package colab.common.xml;

import java.text.ParseException;

/**
 * An XmlConstructor can construct an object of
 * type T from the data in an XmlNode.
 *
 * @param <T> the type of object which can be constructed
 */
public interface XmlConstructor<T extends XmlSerializable> {

    /**
     * Constructs an object from an XmlNode.
     *
     * @param node a node containing the data with which to construct
     *             the object; this node should be properly formatted
     *             if it was built by T's toXml() method
     * @return a new instance of T constructed using the data in the xml node
     * @throws ParseException e if unabled to construct the object because
     *                          the xml data is improperly formatted
     */
    T fromXml(XmlNode node) throws ParseException;

}
