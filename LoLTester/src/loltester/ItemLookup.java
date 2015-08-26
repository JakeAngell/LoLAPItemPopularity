package loltester;

import java.util.HashMap;

/**
 *
 * @author Jake Angell
 */
public class ItemLookup {
    
    /**
     * A HashMap that will act as an item lookup with the key being item ids and
     * the value being the name of the item.
     */
    private HashMap<String, String> itemLookup;

    /**
     * Default constructor to initialise the itemlookup hashmap.
     */
    ItemLookup() {
        itemLookup = new HashMap<>();
    }

    /**
     * Adds an item to the hashmap.
     * @param itemId The id of the item.
     * @param itemName The name of the item.
     */
    public void addItem(String itemId, String itemName) {
        itemLookup.put(itemId, itemName);
    }

    /**
     * Finds an item by its id.
     * @param itemId The items id.
     * @return The name of the item that has the supplied id.
     */
    public String findItemById(String itemId) {
        return itemLookup.get(itemId);
    }

    /**
     * Gets all of the items name in the hashmap and puts it in a string array.
     * @return A string array containing all of the item names in the itemlookup hashmap.
     */
    public String[] getItems() {
        return itemLookup.values().toArray(new String[0]);
    }
    
    /**
     * Clears all of the items in the itemlookup hashmap.
     */
    public void clearItemLookup() {
        itemLookup.clear();
    }
}
