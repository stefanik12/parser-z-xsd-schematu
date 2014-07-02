/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package parser;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 */
public class FileManager {
    //Object management
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
    
    //Object managemenr
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
    
    //Text files management
    public static void save(String filename, String classContent) throws IOException {
        boolean success = false;
        String path = "src/generated";
        File folder = new File(path);
        folder.mkdir();
        File file = new File(path + "/" + filename);

        file.createNewFile();
        try (OutputStream os = new FileOutputStream(file)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
            bw.write(classContent);
            bw.flush();

            success = true;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Binder.class.getName()).log(Level.SEVERE, null, ex);

        }
        if (success) {
            System.out.println(filename + " successfully generated");
        }

    }
}