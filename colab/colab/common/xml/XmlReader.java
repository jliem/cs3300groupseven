package colab.common.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Parses an xml file.
 */
public final class XmlReader {

    /**
     * The scanner used to read in the file.
     */
    private final Scanner in;

    /**
     * Constructs a new XmlReader.
     *
     * @param file the file to read
     * @throws IOException if an I/O exception occurs
     */
    public XmlReader(final File file) throws IOException {
        this.in = new Scanner(file);
    }

    /**
     * Constructs a new XmlReader.
     *
     * @param str the string to parse
     */
    public XmlReader(final String str) {
        this.in = new Scanner(str);
    }

    /**
     * Parses the content.
     *
     * @return a list of xml nodes represented by the string content
     * @throws XmlParseException if the xml content is improperly formatted
     */
    public List<XmlNode> getXml() throws XmlParseException {
        Stack<XmlNode> stack = new Stack<XmlNode>();
        stack.push(new XmlNode(""));
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
            if (line.length() == 0) {
                continue;
            }
            if (line.charAt(0) != '<') {
                throw new XmlParseException();
            } else if (line.charAt(1) == '/') {
                stack.pop();
            } else {
                Scanner lineScanner = new Scanner(line.replaceAll(">", " >"));
                lineScanner.useDelimiter(" ");
                String type = lineScanner.next().trim().substring(1);
                XmlNode node = new XmlNode(type);
                stack.peek().addChild(node);
                lineScanner.useDelimiter(">");
                String attributes = lineScanner.next();
                parseAttributes(attributes, node);
                if (lineScanner.hasNext()) {
                    lineScanner.useDelimiter("<");
                    String body = lineScanner.next().substring(1);
                    node.setContent(XmlNode.unescapeXmlContent(body));
                } else {
                    stack.push(node);
                }
            }
        }
        return stack.pop().getChildren();
    }

    /**
     * Parses a serialized attribute string.
     *
     * @param attributes a string representing an attribute mapping
     * @param node the mapping represented by the string
     */
    static void parseAttributes(final String attributes, final XmlNode node) {
        Scanner scanner = new Scanner(attributes.trim());
        while (scanner.hasNext()) {
            scanner.useDelimiter("=");
            String key = scanner.next().trim();
            scanner.useDelimiter("\"");
            scanner.next();
            String value = scanner.next();
            scanner.skip("\"");
            node.setAttribute(key, XmlNode.unescapeXmlAttribute(value));
        }
    }

}
