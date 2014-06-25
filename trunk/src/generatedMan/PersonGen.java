/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package generatedMan;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class PersonGen implements ComplexInterface {
    public final boolean complex = true;
    //dynamic IDs - to be generated
    //id references to SimpleType or ComplexType node in Collection.java
    private final List<Integer> subElements = new ArrayList<>(Arrays.asList(1,2,3));
    
    //dynamic data type - affects also getter
    //dynamic parent ID
    private final int parent = 1;
    //dynamic data type - affects also getter
    private Object content;
    
    //static data type
    private final Map<String, String> attributes = new HashMap<>();
    
    @Override
    public List<Integer> getSubElements() {
        return subElements;
    }
    public void setAttribute(String attribute, String value) {
        attributes.put(attribute, value);
    }
    @Override
    public String getContent() {
        return (String) content;
    }
    @Override
    public Map<String, String> getAttributes() {
        return attributes;
    }
    @Override
    public int getParent() {
        return parent;
    }
}
