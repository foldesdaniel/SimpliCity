package simplicity.Model.Persistence;

import lombok.Getter;
import simplicity.Model.GameModel;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;

/**
 * The class storing additional
 * information about a saved game
 */
public class SaveEntry implements Serializable {

    @Getter
    private final String cityName;
    @Getter
    private final long saveDate;
    @Getter
    private long modifyDate;
    @Getter
    private int numberOfSaves;
    @Getter
    private final String fileName;

    private SaveEntry(String cityName) {
        this.cityName = cityName;
        this.saveDate = (new java.util.Date()).getTime();
        this.modifyDate = this.saveDate;
        this.numberOfSaves = 1;
        GameModel model = GameModel.getInstance();
        String trimmedName = model.getCityName().replaceAll("[^A-Za-z0-9()\\[\\]]", "").replace("(", "").replace(")", "").toLowerCase();
        this.fileName = trimmedName + "_" + md5(model.getCityName() + this.saveDate) + ".txt";
    }

    /**
     * Creates a new SaveEntry with the given parameters
     *
     * @param cityName unique city name
     * @param data     current game data
     * @throws IOException when persistence fails us
     */
    private static void createEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = new SaveEntry(cityName);
        SaveEntries.getInstance().add(entry);
        Persistence.save(data, entry.fileName);
    }

    /**
     * Removes a SaveEntry
     *
     * @param cityName unique city name
     */
    public static void removeEntry(String cityName) {
        SaveEntry entry = findEntry(cityName);
        if (entry == null) return;
        new java.io.File("saves/" + entry.getFileName()).delete();
        SaveEntries.getInstance().remove(entry);
        try {
            Persistence.saveEntries();
        } catch (IOException ex) {
        }
    }

    /**
     * Updates an existing SaveEntry
     *
     * @param cityName unique city name
     * @param data     current game data
     * @throws IOException when persistence fails us
     */
    private static void updateEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = findEntry(cityName);
        if (entry != null) {
            entry.numberOfSaves++;
            entry.modifyDate = (new java.util.Date()).getTime();
            Persistence.save(data, entry.fileName);
        }
    }

    /**
     * If a SaveEntry exists with the given name then
     * updates it, creates a new one if it doesn't
     *
     * @param cityName unique city name
     * @param data     current game data
     * @throws IOException when persistence fails us
     */
    public static void createOrUpdateEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = findEntry(cityName);
        if (entry == null) {
            createEntry(cityName, data);
        } else {
            updateEntry(cityName, data);
        }
    }

    /**
     * Tries to find a SaveEntry with the given name
     *
     * @param cityName the given city name
     * @return the SaveEntry if successful, null if not
     */
    public static SaveEntry findEntry(String cityName) {
        for (SaveEntry i : SaveEntries.getInstance().getSaveEntries()) {
            if (i.cityName.equals(cityName)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Hashes the given string
     *
     * @param str string to be hashed
     * @return md5 hash
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return String.format("%032X", new java.math.BigInteger(1, md.digest())).toLowerCase();
        } catch (Exception ex) {
            return "00000000000000000000000000000000";
        }
    }

    @Override
    public String toString() {
        return "(" + this.cityName + "," + this.fileName + ")";
    }
}
