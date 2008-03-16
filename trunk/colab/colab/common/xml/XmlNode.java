package colab.common.xml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import colab.common.util.StringUtils;

public class XmlNode {

    private final String type;

    private String stringContent;

    private List<XmlNode> xmlContent;

    private final Map<String, String> attributes;

    public XmlNode(final String type) {
        this.type = type;
        this.attributes = new HashMap<String, String>();
    }

    public String getType() {
        return type;
    }

    public String getBody() {
        return stringContent;
    }

    public List<XmlNode> getChildren() {
        if (xmlContent == null) {
            xmlContent = new ArrayList<XmlNode>();
        }
        return xmlContent;
    }

    public void setContent(final String content) {
        if (xmlContent != null) {
            throw new IllegalStateException("Content type is xml");
        }
        this.stringContent = content;
    }

    public void addChild(final XmlNode node) {
        if (stringContent != null) {
            throw new IllegalStateException("Content type is string");
        }
        getChildren().add(node);
    }

    public String getAttribute(final String key) {
        return attributes.get(key);
    }

    public void setAttribute(final String key, final String value) {
        attributes.put(key, value);
    }

    protected String openingTag() {
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

    protected String closingTag() {
        return "</" + type + ">";
    }

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

    public String serialize(final String indentation) {
        if (xmlContent == null) {
            return serializeSingleLine(indentation);
        } else {
            return serializeMultiLine(indentation);
        }
    }

    public String serialize() {
        return serialize("");
    }

    public static String escapeXmlAttribute(final String str) {
        return str.replaceAll("&", "&amp;")
                  .replaceAll("\"", "&quot;");
    }

    public static String unescapeXmlAttribute(final String str) {
        return str.replaceAll("&quot;", "\"")
                  .replaceAll("&amp;", "&");
    }

    public static String escapeXmlContent(final String str) {
        return str.replaceAll("&", "&amp;")
                  .replaceAll("<", "&lt;")
                  .replaceAll(">", "&gt;")
                  .replaceAll("\r", "&cr;")
                  .replaceAll("\n", "&br;");
    }

    public static String unescapeXmlContent(final String str) {
        return str.replaceAll("&lt;", "<")
                  .replaceAll("&gt;", ">")
                  .replaceAll("&cr;", "\r")
                  .replaceAll("&br;", "\n")
                  .replaceAll("&amp;", "&");
    }

}
