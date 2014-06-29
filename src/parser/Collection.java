/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Node;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 *
 * gathers collection of nodes bound from Schema
 */
public class Collection implements Serializable {

    private final Map<Integer, Node> elements = new HashMap<>();
    private final Map<Integer, String> identifiers = new HashMap<>();
    private final Map<Integer, List<Integer>> tree = new HashMap<>();

    private Integer idCounter = -1;

    public Map<Integer, Node> getIDmap() {
        return elements;
    }

    public Integer add(String identifier, Node newO) {
        return add(identifier, newO, null);
    }

    public Integer add(String identifier, Node newO, List<Integer> subElements) {
        idCounter++;

        elements.put(idCounter, newO);
        identifiers.put(idCounter, identifier);
        tree.put(idCounter, subElements);

        return idCounter;
    }

    public void set(int ID, String identifier, Node newO, List<Integer> subElements) {
        if (identifier != null) {
            identifiers.put(ID, identifier);
        }
        if (newO != null) {
            elements.put(ID, newO);
        }
        if (subElements != null) {
            tree.put(ID, subElements);
        }
    }

    public Node getNodesById(Integer id) {
        return elements.get(id);
    }

    public String getNameById(Integer id) {
        return identifiers.get(id);
    }

    public Integer getIdByName(String name) {

        for (Map.Entry<Integer, String> entry : identifiers.entrySet()) {
            if (entry.getValue().equals(name)) {
                return entry.getKey();
            }
        }
        return null;
    }

    public List<String> getNameList() {
        return (ArrayList) Collections.unmodifiableCollection(identifiers.values());
    }

    public boolean contains(String name) {
        for (Map.Entry<Integer, String> entry : identifiers.entrySet()) {
            if (entry.getValue().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int length() {
        return idCounter + 1;
    }

    public Node getParent(int IDchild) {

        return null;
    }

    public List<Integer> getSubElements(int PID) {
        return tree.get(PID);
    }

}
