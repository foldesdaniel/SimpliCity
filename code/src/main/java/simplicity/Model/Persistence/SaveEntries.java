package simplicity.Model.Persistence;

import java.io.Serializable;
import java.util.ArrayList;

public class SaveEntries implements Serializable {

    private static SaveEntries instance;
    private final ArrayList<SaveEntry> saveEntries = new ArrayList<>();

    public static SaveEntries getInstance() {
        if (instance == null) {
            instance = new SaveEntries();
        }
        return instance;
    }

    public static void loadEntries() {
        try {
            SaveEntries entries = Persistence.loadEntries();
            if (entries != null) instance = entries;
        } catch (Exception ex) {
            System.err.println("Unable to load the entry!");
        }
    }

    public void add(SaveEntry s) {
        getInstance().saveEntries.add(s);
    }

    public void remove(SaveEntry s) {
        getInstance().saveEntries.remove(s);
    }

    public ArrayList<SaveEntry> getSaveEntries() {
        return (ArrayList<SaveEntry>) getInstance().saveEntries.clone();
    }

}
