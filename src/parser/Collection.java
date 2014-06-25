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
 *  gathers collection of nodes bound from Schema
 */
public class Collection implements Serializable{
    private Map<Integer, Object> elements = new HashMap<>();
    private Map<Integer, String> identifiers = new HashMap<>();
    
    private Integer idCounter = 0;
    
    public Map<Integer, Object> getIDmap() {
        return elements;
    }

    public Integer add(String identifier, Node newO) {
        idCounter++;
        elements.put(idCounter, newO);
        identifiers.put(idCounter, identifier);
        
        return idCounter;
    }
    
    public Object getNodesById(Integer id){
        return elements.get(id);
    }
    
    public String getNameById(Integer id){
        return identifiers.get(id);
    }
    
    public Integer getIdByName(String name){
        
        for(Map.Entry<Integer, String> entry : identifiers.entrySet()){
            if(entry.getValue().equals(name)){
                return entry.getKey();
            }
        }
        return null;
    }
    
    public List<String> getNameList(){
        return (ArrayList) Collections.unmodifiableCollection(identifiers.values());
    }
    
    public boolean contains(String name){
        for(Map.Entry<Integer, String> entry : identifiers.entrySet()){
            if(entry.getValue().equals(name)){
                return true;
            }
        }
        return false;
    }
    /**
     *  saves the collection with binded elements
     * @return file where saved collection is located
     */
    public String save(){
        return null;
    }
    
}
