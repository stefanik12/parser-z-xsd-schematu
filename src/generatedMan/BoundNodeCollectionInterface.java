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
 */
public interface BoundNodeCollectionInterface {
    
    /**
     *
     * @return map of the elements from Schema identified by their id
     */
    Map<Integer, Object> getIDmap();
    
    void addNode(Object newO);
    
    
}
