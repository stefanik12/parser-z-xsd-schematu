/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package generatedMan;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class PositionGen {
    public final boolean complex = true;
    //dynamic data type - affects also getter
    //dynamic parent ID
    private int parent = 1;
    //dynamic data type - affects also getter
    private Object content;
    public int getParent() {
        return parent;
    }
    public Object getContent() {
        return content;
    }
    public void setContent(Object content) {
        this.content = content;
    }
    
    
}
