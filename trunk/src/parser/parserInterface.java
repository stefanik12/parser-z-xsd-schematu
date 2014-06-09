package parser;

import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 *
 * @author Marek Burda
 */
public interface parserInterface {
    
    void makeParser() throws ParserConfigurationException, SAXException, IOException, XPathExpressionException;
    
    /**Controls if the node is simple type
     * 
     * @param node
     * @return True if node is simple type
     */
    boolean isSimpleType(Node node) throws XPathExpressionException;
    
    
    /**Controls if the node is complex
     * 
     * @param node
     * @return True if node is complex type
     */
    boolean isComplexType(Node node)  throws XPathExpressionException;
    
    /**Gets attributes of the node
     * 
     * @param node
     * @return List of attributes of parameter node
     */
    List<String> getAttributes(Node node)  throws XPathExpressionException;
    
    /**Gets under elements of node
     * 
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    List<Node> getUnderElements(Node node);
    
    /**
     *
     * @param node
     * @return type of the content (?)
     * @throws XPathExpressionException
     */
    String getType(Node node) throws XPathExpressionException;
    
}
