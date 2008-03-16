package colab.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import colab.common.util.StringUtils;

/**
 * XmlNode is a structural object used in a psuedo-xml implementation.
 */
public final class XmlNode {

    /**
     * The node type.
     */
    private final String type;

    /**
     * The body contents in a leaf node.
     */
    private String stringContent;

    /**
     * Children of this node.
     */
    private List<XmlNode> xmlContent;

    /**
     * Attributes of this node; a map from attribute name to attribute value.
     */
    private final Map<String, String> attributes;

    /**
     * Constructs a new XmlNode.
     *
     * @param type the node type
     */
    public XmlNode(final String type) {
        this.type = type;
        this.attributes = new HashMap<String, String>();
    }

    /**
     * @return the node type
     */
    public String getType() {
        return type;
    }

    /**
     * @return the body contents
     */
    public String getBody() {
        return stringContent;
    }

    /**
     * @return this node's children
     */
    public List<XmlNode> getChildren() {
        if (xmlContent == null) {
            xmlContent = new ArrayList<XmlNode>();
        }
        return xmlContent;
    }

    /**
     * Sets the body text of this node.
     *
     * @param content the body of this node
     */
    public void setContent(final String content) {
        if (xmlContent != null) {
            throw new IllegalStateException("Content type is xml");
        }
        this.stringContent = content;
    }

    /**
     * Adds a child node.
     *
     * @param node the node to add as a child
     */
    public void addChild(final XmlNode node) {
        if (stringContent != null) {
            throw new IllegalStateException("Content type is string");
        }
        getChildren().add(node);
    }

    /**
     * Retrieves the value of an attribute.
     *
     * @param key the name of the attribute
     * @return the value of the attribute
     */
    public String getAttribute(final String key) {
        return attributes.get(key);
    }

    /**
     * Sets the value of an attribute.
     *
     * @param key the name of the attribute
     * @param value the value of the attribute
     */
    public void setAttribute(final String key, final String value) {
        attributes.put(key, value);
    }

    /**
     * @return the opening tag for the string representation of this node
     */
    private String openingTag() {
        StringBuilder str = new StringBuilder();
        str.append("<");
        str.append(type);
        for (String key : attributes.keySet()) {
            str.append(" ");
            str.append(key);
            str.append("=\"");
            str.append(escapeXmlAttribute(attributes.get(key)));
            str.append("\"");
        }
        str.append(">");
        return str.toString();
    }

    /**
     * @return the closing tag for the string representation of this node
     */
    protected String closingTag() {
        return "</" + type + ">";
    }

    /**
     * @param indentation whitespace to append to the beginning of each line
     * @return a string representation of this leaf node
     */
    private String serializeSingleLine(final String indentation) {
        StringBuilder str = new StringBuilder();
        str.append(indentation);
        str.append(openingTag());
        if (stringContent != null) {
            String content = StringUtils.emptyIfNull(stringContent);
            str.append(escapeXmlContent(content));
        } else {
           str.append("");
        }
        str.append(closingTag());
        return str.toString();
    }

    /**
     * @param indentation whitespace to append to the beginning of each line
     * @return a string representation of this parent node
     */
    private String serializeMultiLine(final String indentation) {
        StringBuilder str = new StringBuilder();
        str.append(indentation);
        str.append(openingTag());
        for (XmlNode child : xmlContent) {
            str.append("\n");
            str.append(child.serialize(indentation + "\t"));
        }
        str.append("\n");
        str.append(indentation);
        str.append(closingTag());
        return str.toString();
    }

    /**
     * @param indentation whitespace to append to the beginning of each line
     * @return a string representation of this node
     */
    public String serialize(final String indentation) {
        if (xmlContent == null) {
            return serializeSingleLine(indentation);
        } else {
            return serializeMultiLine(indentation);
        }
    }

    /**
     * @return a string representation of this node
     */
    public String serialize() {
        return serialize("");
    }

    /**
     * @param str an attribute value
     * @return the string, safe for use in serialization
     */
    public static String escapeXmlAttribute(final String str) {
        return str.replaceAll("&", "&amp;")
                  .replaceAll("\"", "&quot;");
    }

    /**
     * @param str an escaped attribute value
     * @return the string, unescaped
     */
    public static String unescapeXmlAttribute(final String str) {
        return str.replaceAll("&quot;", "\"")
                  .replaceAll("&amp;", "&");
    }

    /**
     * @param str a body string
     * @return the string, safe for use in serialization
     */
    public static String escapeXmlContent(final String str) {
        return str.replaceAll("&", "&amp;")
                  .replaceAll("<", "&lt;")
                  .replaceAll(">", "&gt;")
                  .replaceAll("\r", "&cr;")
                  .replaceAll("\n", "&br;");
    }

    /**
     * @param str an escaped body string
     * @return the string, unescaped
     */
    public static String unescapeXmlContent(final String str) {
        return str.replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .replaceAll("&cr;", "\r")
                  .replaceAll("&br;", "\n")
                  .replaceAll("&amp;", "&");
    }

}
