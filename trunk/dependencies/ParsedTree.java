/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */

package generated;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class ParsedTree {
    private final List<Node> tree = new ArrayList<>();
    
    public ParsedTree addNode(Node n){
        tree.add(n);
        return this;
    }
    public Node getNode(String ID){
        for(Node n : tree){
            if(n.getID().equals(ID)){
                return n;
            }
        }
        return null;
    }
}
