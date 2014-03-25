package restaurants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Lasse Lybeck
 */
public class Serialiser {

    public void serialize(Serializable s, String filename) throws FileNotFoundException, IOException {
        File file = new File(filename);
        if (file.exists())
            file.delete();
        file.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(s);
        fileOutputStream.close();
    }

    public <T extends Serializable> T deserialize(String filename) throws FileNotFoundException, IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        T object = (T) objectInputStream.readObject();
        return object;
    }
}
