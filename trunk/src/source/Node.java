/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package source;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class Node {
    private String ID;
    private Object content;

    public Object getContent() {
        return content;
    }

    public Node setContent(Object content) {
        this.content = content;
        return this;
    }
    private List<Node> subElements = new ArrayList<>();

    public String getID() {
        return ID;
    }

    public Node setID(String ID) {
        this.ID = ID;
        return this;
    }

    public List<Node> getSubElements() {
        return subElements;
    }

    public Node setSubElement(Node subElement) {
        subElements.add(subElement);
        return this;
    }
    
}

