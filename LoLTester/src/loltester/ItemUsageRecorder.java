/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loltester;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Jake Angell
 */
public class ItemUsageRecorder {

    /**
     * A hashmap containing the name of an item and an integer representation of the items usage in game.
     */
    private HashMap<String, Integer> itemUsageMap;

    /**
     * Constructor to initialise the itemUsageMap hashmap.
     */
    ItemUsageRecorder() {
        itemUsageMap = new HashMap<>();
    }

    /**
     * Populates the hashmap with the names of items within a String array. Each item usage count is set to 0.
     * @param items A String array containing item names.
     */
    public void populateItems(String[] items) {
        for (String i : items) {
            itemUsageMap.put(i, 0);
        }
    }

    /**
     * Adds a usage to an item in the itemUsageMap.
     * @param itemName The item name to record a usage on.
     */
    public void addUsage(String itemName) {
        itemUsageMap.put(itemName, itemUsageMap.get(itemName) + 1);
    }
    
    /**
     * Clears the itemUsageMap.
     */
    public void clearItemRecorder() {
        itemUsageMap.clear();
    }

    /**
     * Writes the contents of the HashMap to a file.
     * @param fileName The file name of the resulting file.
     */
    public void writeContentsToFile(String fileName) {
        FileWriter fStream;
        BufferedWriter out;
        try {
            fStream = new FileWriter(fileName);
            out = new BufferedWriter(fStream);
            Set keys = itemUsageMap.keySet();
            Iterator keyIterator = keys.iterator();
            
            for (int i = 0; i < itemUsageMap.size(); i++) {
                if (keyIterator.hasNext()) {
                    String nextKey = (String) keyIterator.next();
                    out.write(nextKey + " : ");
                    out.write(itemUsageMap.get(nextKey) + "\n");
                }
            }
            out.close();
        } catch (IOException ex) {
            System.out.println("ItemUsageRecorder file writing error.");
        }
    }

}
