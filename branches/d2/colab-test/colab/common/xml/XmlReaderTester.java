package colab.common.xml;

import java.util.List;

import junit.framework.TestCase;

/**
 * Test cases for {@link XmlReader}.
 */
public final class XmlReaderTester extends TestCase {

    /**
     * Gives the reader an empty string to parse.
     * It should return a list without any nodes.
     *
     * @throws Exception if any exception is thrown
     */
    public void testEmptyString() throws Exception {

        XmlReader reader = new XmlReader("");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(0, nodes.size());

    }

    /**
     * Gives the reader some xml with a single empty node.
     *
     * @throws Exception if any exception is thrown
     */
    public void testSingleEmptyNode() throws Exception {

        XmlReader reader = new XmlReader("<nodeType></nodeType>");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 1);

        XmlNode node = nodes.get(0);

        assertEquals("nodeType", node.getType());
        assertEquals("", node.getBody());

    }

    /**
     * Gives the reader a string with a single xml node,
     * having two attributes.
     *
     * @throws Exception if any exception is thrown
     */
    public void testSingleEmptyNodeWithAttributes() throws Exception {

        XmlReader reader = new XmlReader(
                "<person name=\"Chuck\" age=\"27\"></person>");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 1);

        XmlNode node = nodes.get(0);

        assertEquals("person", node.getType());
        assertEquals("Chuck", node.getAttribute("name"));
        assertEquals("27", node.getAttribute("age"));
        assertEquals("", node.getBody());

    }

    /**
     * Gives the reader a string with a single node, no attributes,
     * with text in the body.
     *
     * @throws Exception if any exception is thrown
     */
    public void testSingleNodeWithBody() throws Exception {

        String type = "food";
        String body = "Steak <b>Sandwich</b>";

        XmlReader reader = new XmlReader(
                  "<" + type + ">"
                + XmlNode.escapeXmlContent(body)
                + "</" + type + ">");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 1);

        XmlNode node = nodes.get(0);

        assertEquals(type, node.getType());
        assertEquals(body, node.getBody());

    }

    /**
     * Gives the reader a string containing two xml nodes,
     * both empty and without attributes.
     *
     * @throws Exception if any exception is thrown
     */
    public void testTwoEmptyNodes() throws Exception {

        XmlReader reader = new XmlReader(
                "<elephant></elephant>\n<tiger></tiger>\n");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 2);

        XmlNode elephant = nodes.get(0);
        assertEquals("elephant", elephant.getType());

        XmlNode tiger = nodes.get(1);
        assertEquals("tiger", tiger.getType());

    }

}
