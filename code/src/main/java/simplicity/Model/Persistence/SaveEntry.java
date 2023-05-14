package simplicity.Model.Persistence;

import lombok.Getter;
import simplicity.Model.GameModel;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;

public class SaveEntry implements Serializable {

    @Getter private final String cityName;
    @Getter private final long saveDate;
    @Getter private long modifyDate;
    @Getter private int numberOfSaves;
    @Getter private String fileName;

    private SaveEntry(String cityName){
        this.cityName = cityName;
        this.saveDate = (new java.util.Date()).getTime();
        this.modifyDate = this.saveDate;
        this.numberOfSaves = 1;
        GameModel model = GameModel.getInstance();
        String trimmedName = model.getCityName().replaceAll("[^A-Za-z0-9()\\[\\]]","").replace("(","").replace(")","").toLowerCase();
        this.fileName = trimmedName + "_" + md5(model.getCityName()+this.saveDate) + ".txt";
    }

    private static SaveEntry createEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = new SaveEntry(cityName);
        SaveEntries.getInstance().add(entry);
        Persistence.save(data, entry.fileName);
        return entry;
    }

    private static void removeEntry(String cityName) throws Exception {
        SaveEntry entry = findEntry(cityName);
        if(entry == null){
            throw new Exception("Save not found");
        }
        SaveEntries.getInstance().remove(entry);
    }

    private static void updateEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = findEntry(cityName);
        entry.numberOfSaves++;
        long before = entry.modifyDate;
        entry.modifyDate = (new java.util.Date()).getTime();
        System.out.println("updated " + cityName + ", modified from " + before + " to " + entry.modifyDate);
        Persistence.save(data, entry.fileName);
    }

    public static void createOrUpdateEntry(String cityName, GameModel data) throws IOException {
        SaveEntry entry = findEntry(cityName);
        if(entry == null){
            createEntry(cityName, data);
        }else{
            updateEntry(cityName, data);
        }
    }

    private static SaveEntry findEntry(String cityName){
        for(SaveEntry i : SaveEntries.getInstance().getSaveEntries()){
            if(i.cityName.equals(cityName)){
                return i;
            }
        }
        return null;
    }

    public static String md5(String str) {
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            String hash = String.format("%032X", new java.math.BigInteger(1, md.digest())).toLowerCase();
            return hash;
        }catch(Exception ex){
            return "00000000000000000000000000000000";
        }
    }

    @Override
    public String toString() {
        return "(" + this.cityName + "," + this.fileName + ")";
    }
}
