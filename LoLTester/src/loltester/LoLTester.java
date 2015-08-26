
package loltester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jake Angell
 */
public class LoLTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LeagueMatchAPIMedium leajServiceLayer = new LeagueMatchAPIMedium();

        ItemLookup itemLookup = new ItemLookup();
        initItemLookup(itemLookup, "511Items.txt");

        ItemUsageRecorder itemRecorder = new ItemUsageRecorder();
        itemRecorder.populateItems(itemLookup.getItems());

        HashMap<String, String> matchFiles = new HashMap<>();

        enter511NormalFiles(matchFiles);
        enter511RankedFiles(matchFiles);
        enter514NormalFiles(matchFiles);
        enter514RankedFiles(matchFiles);

        Set keys = matchFiles.keySet();
        Iterator matchFilesKeys = keys.iterator();

        for (int i = 0; i < matchFiles.size(); i++) {
            String fileName = matchFilesKeys.next().toString();

            if (fileName.startsWith("5_14")) {
                itemLookup.clearItemLookup();
                initItemLookup(itemLookup, "514Items.txt");
            }
            itemRecorder.populateItems(itemLookup.getItems());
            
            ArrayList<String> matchIds = loadMatchInfo(fileName);

            String region = matchFiles.get(fileName);

            for (String matchId : matchIds) {
                leajServiceLayer.getMatch(Long.parseLong(matchId), region, false, itemLookup, itemRecorder);
                try {
                    Thread.sleep(1010);
                } catch (InterruptedException ex) {
                    Logger.getLogger(LoLTester.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            itemRecorder.writeContentsToFile(fileName + "_Results.txt");
            itemRecorder.clearItemRecorder();
        }

    }

    /**
     * Creates an entry of file names and regions into the matchFiles hashmap.
     * @param matchFiles A HashMap containing match files and their regions.
     */
    private static void enter514RankedFiles(HashMap<String, String> matchFiles) {
        matchFiles.put("5_14\\RANKED_SOLO\\BR.json", "BR");
        matchFiles.put("5_14\\RANKED_SOLO\\EUNE.json", "EUNE");
        matchFiles.put("5_14\\RANKED_SOLO\\EUW.json", "EUW");
        matchFiles.put("5_14\\RANKED_SOLO\\KR.json", "KR");
        matchFiles.put("5_14\\RANKED_SOLO\\LAN.json", "LAN");
        matchFiles.put("5_14\\RANKED_SOLO\\LAS.json", "LAS");
        matchFiles.put("5_14\\RANKED_SOLO\\NA.json", "NA");
        matchFiles.put("5_14\\RANKED_SOLO\\OCE.json", "OCE");
        matchFiles.put("5_14\\RANKED_SOLO\\RU.json", "RU");
        matchFiles.put("5_14\\RANKED_SOLO\\TR.json", "TR");
    }

     /**
     * Creates an entry of file names and regions into the matchFiles hashmap.
     * @param matchFiles A HashMap containing match files and their regions.
     */
    private static void enter514NormalFiles(HashMap<String, String> matchFiles) {
        matchFiles.put("5_14\\NORMAL_5X5\\BR.json", "BR");
        matchFiles.put("5_14\\NORMAL_5X5\\EUNE.json", "EUNE");
        matchFiles.put("5_14\\NORMAL_5X5\\EUW.json", "EUW");
        matchFiles.put("5_14\\NORMAL_5X5\\KR.json", "KR");
        matchFiles.put("5_14\\NORMAL_5X5\\LAN.json", "LAN");
        matchFiles.put("5_14\\NORMAL_5X5\\LAS.json", "LAS");
        matchFiles.put("5_14\\NORMAL_5X5\\NA.json", "NA");
        matchFiles.put("5_14\\NORMAL_5X5\\OCE.json", "OCE");
        matchFiles.put("5_14\\NORMAL_5X5\\RU.json", "RU");
        matchFiles.put("5_14\\NORMAL_5X5\\TR.json", "TR");
    }

     /**
     * Creates an entry of file names and regions into the matchFiles hashmap.
     * @param matchFiles A HashMap containing match files and their regions.
     */
    private static void enter511RankedFiles(HashMap<String, String> matchFiles) {
        matchFiles.put("5_11\\RANKED_SOLO\\BR.json", "BR");
        matchFiles.put("5_11\\RANKED_SOLO\\EUNE.json", "EUNE");
        matchFiles.put("5_11\\RANKED_SOLO\\EUW.json", "EUW");
        matchFiles.put("5_11\\RANKED_SOLO\\KR.json", "KR");
        matchFiles.put("5_11\\RANKED_SOLO\\LAN.json", "LAN");
        matchFiles.put("5_11\\RANKED_SOLO\\LAS.json", "LAS");
        matchFiles.put("5_11\\RANKED_SOLO\\NA.json", "NA");
        matchFiles.put("5_11\\RANKED_SOLO\\OCE.json", "OCE");
        matchFiles.put("5_11\\RANKED_SOLO\\RU.json", "RU");
        matchFiles.put("5_11\\RANKED_SOLO\\TR.json", "TR");
    }

     /**
     * Creates an entry of file names and regions into the matchFiles hashmap.
     * @param matchFiles A HashMap containing match files and their regions.
     */
    private static void enter511NormalFiles(HashMap<String, String> matchFiles) {
        matchFiles.put("5_11\\NORMAL_5X5\\BR.json", "BR");
        matchFiles.put("5_11\\NORMAL_5X5\\EUNE.json", "EUNE");
        matchFiles.put("5_11\\NORMAL_5X5\\EUW.json", "EUW");
        matchFiles.put("5_11\\NORMAL_5X5\\KR.json", "KR");
        matchFiles.put("5_11\\NORMAL_5X5\\LAN.json", "LAN");
        matchFiles.put("5_11\\NORMAL_5X5\\LAS.json", "LAS");
        matchFiles.put("5_11\\NORMAL_5X5\\NA.json", "NA");
        matchFiles.put("5_11\\NORMAL_5X5\\OCE.json", "OCE");
        matchFiles.put("5_11\\NORMAL_5X5\\RU.json", "RU");
        matchFiles.put("5_11\\NORMAL_5X5\\TR.json", "TR");
    }

    /**
     * Loads a JSON file containing match ids into a String ArrayList.
     * @param filePath The file path that points towards a JSON file containing match ids.
     * @return The match ids in an ArrayList<String>.
     */
    private static ArrayList<String> loadMatchInfo(String filePath) {
        BufferedReader reader = null;
        ArrayList<String> matchIds = new ArrayList<>();
        try {
            File matchFile = new File(filePath);
            reader = new BufferedReader(new FileReader(matchFile));
            JSONArray matchArray = new JSONArray(readFileIntoString(filePath));
            for (int i = 0; i < matchArray.length(); i++) {
                matchIds.add(matchArray.get(i).toString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException ex) {
            Logger.getLogger(LoLTester.class.getName()).log(Level.SEVERE, null, ex);
        }
        return matchIds;
    }

    /**
     * Initiates the itemLookup hashmap by parsing a JSON file containing item information.
     * @param itemLookup A HashMap that item information will be added to.
     * @param fileName A file path pointing to a JSON file containing item information.
     */
    private static void initItemLookup(ItemLookup itemLookup, String fileName) {
        JSONObject fiveElevenItems;
        try {
            fiveElevenItems = new JSONObject(readFileIntoString(fileName));

            JSONObject jObject = new JSONObject(fiveElevenItems.getJSONObject("data").toString());
            Iterator<?> keys = jObject.keys();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                if (jObject.get(key) instanceof JSONObject) {
                    JSONObject item = (JSONObject) jObject.get(key);
                    itemLookup.addItem(key, item.get("name").toString());
                }
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Reads a file and returns it in a String.
     * @param fileName Points to a file, the contents of this file will be read and placed into a string to be returned.
     * @return The contents of the file.
     */
    public static String readFileIntoString(String fileName) {
        BufferedReader reader = null;
        String result = "";
        try {
            File file = new File(fileName);
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                result += line;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
