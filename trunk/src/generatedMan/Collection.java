/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedMan;

import java.util.Map;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * 
 *  gathers collection of nodes bound from Schema
 */
public class Collection{
    private Map<Integer, Object> boundNodes;
    private Integer idCounter = 0;
    
    public Map<Integer, Object> getIDmap() {
        return boundNodes;
    }

    public Integer add(Object newO) {
        idCounter++;
        boundNodes.put(idCounter, newO);
        return idCounter;
    }
    
    public Object getNodesById(Integer id){
        return boundNodes.get(id);
    }
    
}
