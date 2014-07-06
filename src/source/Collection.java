/*
Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.w3c.dom.Node;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 *
 *  Gathers collection of nodes bound from Schema
 *  Contains only simple setters and getters for contained data
 */
public class Collection implements Serializable {

    private final Map<Integer, Node> elements = new HashMap<>();
    private final Map<Integer, String> identifiers = new HashMap<>();
    private final Map<Integer, List<Integer>> tree = new HashMap<>();
    private final Map<Integer, List<String>> attributes = new HashMap<>();

    private Integer idCounter = -1;

    public Map<Integer, Node> getIDmap() {
        return elements;
    }

    public Integer add(String identifier, Node newO) {
        return add(identifier, newO, null, null);
    }

    public Integer add(String identifier, Node newO, List<Integer> subElements, List<String> attribute) {
        idCounter++;

        elements.put(idCounter, newO);
        identifiers.put(idCounter, identifier);
        tree.put(idCounter, subElements);
        attributes.put(idCounter, attribute);

        return idCounter;
    }

    public void set(int ID, String identifier, Node newO, List<Integer> subElements, List<String> attribute) {
        if (identifier != null) {
            identifiers.put(ID, identifier);
        }
        if (newO != null) {
            elements.put(ID, newO);
        }
        if (subElements != null) {
            tree.put(ID, subElements);
        }
        if (attribute != null) {
            attributes.put(ID, attribute);
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

    public List<Integer> getSubElements(int PID) {
        List<Integer> out = tree.get(PID);
        if(out == null){
            return new ArrayList<>();
        }
        return tree.get(PID);
    }

    public List<String> getAllAttributes(int ID) {
        return attributes.get(ID);
    }

    public Integer getParent(int child) {
        for (Entry e : tree.entrySet()) {
            if (e.getValue() != null) {
                if (((List) e.getValue()).contains(child)) {
                    return (Integer) e.getKey();
                }
            }
        }
        return -1;
    }
    
    public Integer getParent(){
        for (Integer id : getIDmap().keySet()) {
            if (getParent(id) == -1) {
                return id;
            }
        }
        return null;
    }
}
