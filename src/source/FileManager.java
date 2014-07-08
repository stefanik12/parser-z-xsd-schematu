/*
 Project for PV138 as tought on Faculty of Informatics on Masaryk University in 2014
 */
package source;

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
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Michal Štefánik 422237 <https://is.muni.cz/auth/osoba/422237>
 * Class for saving text files and both saving and loading binary data
 * structures (used to store Collection state)
 */
public class FileManager {

    //Object management
    public static void insertIntoFile(Object object, String filename) throws IOException {
        FileOutputStream fos = new FileOutputStream(filename);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream file = new ObjectOutputStream(bos);
        try {
            file.writeObject(object);
        } catch (Exception e) {
            System.out.println("write object does not work");
        }
        file.close();
    }

    //Object managemenr
    public static Object selectFromFile(String filename) throws IOException, ClassNotFoundException {
        Object variable = null;
        FileInputStream fis = new FileInputStream(filename);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream file = new ObjectInputStream(bis);
        variable = file.readObject();
        file.close();

        return variable;
    }

    //Text files management
    public static void save(String filename, String classContent, File outputDir) throws IOException {
        boolean success = false;

        if (!outputDir.mkdir() && !outputDir.exists()) {
            System.out.println("path " + outputDir.getAbsolutePath() + " was unable to be created");
        }

        File file = new File(outputDir.getAbsolutePath() + "/" + filename);

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

    // Text file management
    public void copy(String source, String target) throws IOException {
        File in = new File(getProjectHome() + source);
        File out = new File(target + "/" + in.getName());
        if (!out.exists()) {
            out.createNewFile();
        }
        Files.copy(in.toPath(), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
    }

    public String getProjectHome() throws IOException {
        try {
            URL url = getClass().getResource("").toURI().toURL();

            String applicationDir = url.getPath() + "/../../../";
            if (url.getProtocol().equals("jar")) {
                applicationDir = new File(((JarURLConnection) url.openConnection()).getJarFileURL().getFile()).getParent() + "/../";
            }
            
            return applicationDir;
        } catch (MalformedURLException | URISyntaxException e) {
            System.out.println("Error when determining this project location: " + e.toString());
            throw new IOException(e.toString());
        }
    }
}
