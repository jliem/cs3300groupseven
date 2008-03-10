package colab.common.xml;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class XmlReader {

    private final Scanner in;

    public XmlReader(final File file) throws IOException {
        this.in = new Scanner(file);
    }

    public XmlReader(final String str) {
        this.in = new Scanner(str);
    }

    public List<XmlNode> getXml() throws IOException {
        Stack<XmlNode> stack = new Stack<XmlNode>();
        stack.push(new XmlNode(""));
        while (in.hasNextLine()) {
            String line = in.nextLine().trim();
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
                    node.setContent(lineScanner.next().substring(1));
                } else {
                    stack.push(node);
                }
            }
        }
        return stack.pop().getChildren();
    }

    static void parseAttributes(final String attributes, final XmlNode node) {
        Scanner scanner = new Scanner(attributes.trim());
        while (scanner.hasNext()) {
            scanner.useDelimiter("=");
            String key = scanner.next().trim();
            scanner.useDelimiter("\"");
            scanner.next();
            String value = scanner.next();
            scanner.skip("\"");
            node.setAttribute(key, value);
        }
    }

}
