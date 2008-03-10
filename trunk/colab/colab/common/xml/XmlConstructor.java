package colab.common.xml;

import java.text.ParseException;


public interface XmlConstructor<T> {

    T fromXml(XmlNode node) throws ParseException;

}
