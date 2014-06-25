package generated;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap; 
import java.util.List;
import java.util.Map;
public class Gencompany {
private final List<Integer> subElements = new ArrayList<>(Arrays.asList(2, 3, 4));
private final int parent = 1;
public final boolean complex = true;
private Object content = null;
private final Map<String, Object> attributes = new HashMap<>();
public Gencompany(){

    }
    public List<Integer> getSubElements() {
        return subElements;
    }
    public void setAttribute(String attribute, Object value) {
        attributes.put(attribute, value);
    }

    public Object getContent() {
        return content;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public int getParent() {
        return parent;
    }}