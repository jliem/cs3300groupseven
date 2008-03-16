package colab.common.xml;

/**
 * An XmlSerializable object can convert its data to an XmlNode.
 */
public interface XmlSerializable {

    /**
     * @return an xml node containing all of the data required
     *         to serialize this object.
     */
    XmlNode toXml();

}
