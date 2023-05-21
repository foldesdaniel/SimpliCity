package simplicity.Model.Persistence;

import java.io.*;

public class Persistence {

    private static final String pathToSavedGamesDir = "saves/";

    /**
     * used to serialize a specific object
     *
     * @param obj      object to be serialized
     * @param filename where we save the byte stream
     */
    public static void save(Object obj, String filename) throws IOException {
        File f = new File(pathToSavedGamesDir);
        if (!f.exists()) f.mkdirs();
        String fullPathToFile = pathToSavedGamesDir + filename;
        if (filename.startsWith("__test")) fullPathToFile = filename;

        FileOutputStream fileOutputStream = new FileOutputStream(fullPathToFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();

        saveEntries();
    }

    /**
     * used to serialize SaveEntries
     */
    public static void saveEntries() throws IOException {
        File f = new File(pathToSavedGamesDir);
        if (!f.exists()) f.mkdirs();

        FileOutputStream fileOutputStream2 = new FileOutputStream(pathToSavedGamesDir + "saveentries.txt");
        ObjectOutputStream objectOutputStream2 = new ObjectOutputStream(fileOutputStream2);
        objectOutputStream2.writeObject(SaveEntries.getInstance());
        objectOutputStream2.close();
    }

    /**
     * used to deserialize SaveEntries
     */
    public static SaveEntries loadEntries() {
        try {
            String fullPathToFile = pathToSavedGamesDir + "saveentries.txt";
            //if (filename.startsWith("__test")) fullPathToFile = filename;
            FileInputStream fileInputStream = new FileInputStream(fullPathToFile);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            SaveEntries obj = (SaveEntries) objectInputStream.readObject();
            objectInputStream.close();
            return obj;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * used to deserialize an object
     *
     * @param filename file which contains a serialized object
     */
    public static Object load(String filename) throws IOException, ClassNotFoundException {
        String fullPathToFile = pathToSavedGamesDir + filename;
        if (filename.startsWith("__test")) fullPathToFile = filename;

        FileInputStream fileInputStream = new FileInputStream(fullPathToFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object obj = objectInputStream.readObject();
        objectInputStream.close();
        return obj;
    }

    /**
     * used to delete a specific file
     *
     * @param filename name of the file
     */
    public static void delete(String filename) {
        String fullPathToFile = pathToSavedGamesDir + filename;
        if (filename.startsWith("__test")) fullPathToFile = filename;

        File file = new File(fullPathToFile);
        if (!file.delete()) System.err.println("Unable to delete file: " + filename);
    }

}

