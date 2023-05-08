package simplicity.Model.Persistence;

import lombok.Getter;

import java.util.ArrayList;

public class SaveEntry {

    private static final ArrayList<SaveEntry> saveEntries = new ArrayList<>();

    private final String cityName;
    private final long saveDate;
    @Getter private int numberOfSaves;
    private Object data;

    private SaveEntry(String cityName, Object data){
        this.cityName = cityName;
        this.saveDate = (new java.util.Date()).getTime();
        this.numberOfSaves = 1;
        this.data = data;
    }

    public static void increaseSaveNumber(String cityName) throws Exception {
        SaveEntry entry = findEntry(cityName);
        if(entry == null){
            throw new Exception("Save not found");
        }
        entry.numberOfSaves++;
    }

    public static SaveEntry createEntry(String cityName, Object data){
        SaveEntry entry = new SaveEntry(cityName, data);
        try{ // removes save if a city exists with the same name
            removeEntry(cityName);
        }catch(Exception ex){}
        saveEntries.add(entry);
        return entry;
    }

    public static void removeEntry(String cityName) throws Exception {
        SaveEntry entry = findEntry(cityName);
        if(entry == null){
            throw new Exception("Save not found");
        }
        saveEntries.remove(entry);
    }

    public static SaveEntry findEntry(String cityName){
        for(SaveEntry i : saveEntries){
            if(i.cityName.equals(cityName)){
                return i;
            }
        }
        return null;
    }

}
