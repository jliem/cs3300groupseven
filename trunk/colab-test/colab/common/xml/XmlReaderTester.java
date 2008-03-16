package colab.common.xml;

import java.util.List;

import junit.framework.TestCase;

public final class XmlReaderTester extends TestCase {

    public void testEmptyString() throws Exception {

        XmlReader reader = new XmlReader("");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(0, nodes.size());

    }

    public void testSingleEmptyNode() throws Exception {

        XmlReader reader = new XmlReader("<nodeType></nodeType>");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 1);

        XmlNode node = nodes.get(0);

        assertEquals("nodeType", node.getType());
        assertEquals("", node.getBody());

    }

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

    public void testSingleNodeWithBody() throws Exception {

        XmlReader reader = new XmlReader(
                  "<food>"
                + XmlNode.escapeXmlContent("Steak <b>Sandwich</b>")
                + "</food>");
        List<XmlNode> nodes = reader.getXml();

        assertEquals(nodes.size(), 1);

        XmlNode node = nodes.get(0);

        assertEquals("food", node.getType());
        assertEquals("Steak <b>Sandwich</b>", node.getBody());

    }

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
