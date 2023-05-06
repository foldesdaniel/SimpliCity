package simplicity.Model.Persistence;

import java.io.*;

public class Persistence {
    public static void save(Object obj, String filename) throws IOException {
        String pathToSavedGamesDir = "code/SavedGames/";
        String fullPathToFile = pathToSavedGamesDir + filename;

        FileOutputStream fileOutputStream = new FileOutputStream(fullPathToFile);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(obj);
        objectOutputStream.close();
        System.out.println("---------------");
        System.out.println("GAME DATA SAVED");
        System.out.println("---------------");
    }

    public static Object load(String filename) throws IOException, ClassNotFoundException {
        String pathToSavedGamesDir = "code/SavedGames/";
        String fullPathToFile = pathToSavedGamesDir + filename;

        FileInputStream fileInputStream = new FileInputStream(fullPathToFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        Object obj = objectInputStream.readObject();
        objectInputStream.close();
        return obj;
    }

    public static void delete(String filename) {
        File file = new File(filename);
        file.delete();
    }
}

