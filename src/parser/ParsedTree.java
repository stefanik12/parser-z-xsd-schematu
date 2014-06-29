/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.util.List;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class ParsedTree {
    private List<Node> tree;
    
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
