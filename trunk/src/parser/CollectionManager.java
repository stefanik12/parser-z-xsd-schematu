/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class CollectionManager {
    public static void insertIntoFile(Object object, String filename) throws IOException 
    {
        FileOutputStream fos = new FileOutputStream(filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream file = new ObjectOutputStream(bos);
    try
    {
        file.writeObject(object);
    }
    catch(Exception e)
    {
        System.out.println("write object does not work");
    }
        file.close();  
    }
    public static Object selectFromFile(String filename) throws IOException, ClassNotFoundException 
    {
        Object variable = null;
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream file = new ObjectInputStream(bis);
        variable = file.readObject();
        file.close();

        return variable;
    }
}