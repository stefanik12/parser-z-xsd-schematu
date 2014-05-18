package parser;

import java.util.List;
import org.w3c.dom.Node;

/**
 *
 * @author Marek Burda
 */
public interface parserInterface {
    
    void makeParser();
    
    /**Controls if the node is simple type
     * 
     * @param node
     * @return True if node is simple type
     */
    boolean isSimpleType(Node node);
    
    
    /**Controls if the node is complex
     * 
     * @param node
     * @return True if node is complex type
     */
    boolean isComplexType(Node node);
    
    /**Gets attributes of the node
     * 
     * @param node
     * @return List of attributes of parameter node
     */
    List<String> getAttributes(Node node);
    
    /**Gets under elements of node
     * 
     * @param node
     * @return List of nodes,that are under elements of parameter node
     */
    List<Node> getUnderElements(Node node);
}
