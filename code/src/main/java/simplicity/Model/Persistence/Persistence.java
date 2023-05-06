package simplicity.Model.Persistence;

import java.io.*;

public class Persistence {

    private static final String pathToSavedGamesDir = "code/SavedGames/";

    /**
     * used to serialize a specific object
     *
     * @param obj      object to be serialized
     * @param filename where we save the byte stream
     */
    public static void save(Object obj, String filename) throws IOException {
        String fullPathToFile = pathToSavedGamesDir + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(fullPathToFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
    }

    /**
     * used to deserialize an object
     *
     * @param filename file which contains a serialized object
     */
    public static Object load(String filename) throws IOException, ClassNotFoundException {
        String fullPathToFile = pathToSavedGamesDir + filename;

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

        File file = new File(fullPathToFile);
        if (!file.delete()) System.err.println("Unable to delete file: " + filename);
    }
}

