package colab.common.xml;

/**
 * An XmlSerializable object can convert its data to and from an XmlNode.
 */
public interface XmlSerializable {

    String xmlNodeName();

    /**
     * @return an xml node containing all of the data required
     *         to serialize this object.
     */
    XmlNode toXml();

    /**
     * Fills this object with data from an xml serialization.
     *
     * The intended usage of this method is to build an object
     * of class T by constructing a blank instance of T and then
     * applying and xml node:
     *
     *     (new T()).fromXml(node);
     *
     * @param node an xml node containing data to populate the object
     * @throws XmlParseException if the xml node is not appropriately
     *                           formatted for this object
     */
    void fromXml(XmlNode node) throws XmlParseException;

}
