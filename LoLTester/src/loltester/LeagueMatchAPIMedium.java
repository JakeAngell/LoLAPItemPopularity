package loltester;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Jake Angell
 */
public class LeagueMatchAPIMedium {

    /**
     * The apiKey used in the request to the League of Legends API.
     */
    String apiKey;

    /**
     * A default constructor incase the caller does not provide an API key which would cause a null pointer exception.
     */
    public LeagueMatchAPIMedium() {
        apiKey = "";
    }

    /**
     * Constructor for LeagueMatchAPIMedium.
     * @param apiKey The API key to be used in all subsequent requests to the API.
     */
    public LeagueMatchAPIMedium(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Setter for the apiKey field.
     * @param apiKey The new API key.
     */
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * Gets match details from the LoL API, extracts participant item information and records uses of said items.
     * @param matchID The id for a specific match.
     * @param region The region in which the match has taken place.
     * @param includeTimeline If true, timeline data will be included in the response.
     * @param itemLookup An ItemLookup which provides a lookup which will respond with an item name when given its id.
     * @param itemRecorder An ItemUsageRecorder which will record all usages of items within the game.
     */
    public void getMatch(Long matchID, String region, boolean includeTimeline, ItemLookup itemLookup, ItemUsageRecorder itemRecorder) {
        URL myURL;
        try {
            myURL = new URL("https://" + region + ".api.pvp.net/api/lol/" + region + "/v2.2/match/" + matchID + "?includeTimeline=" + includeTimeline
                    + "&api_key=" + apiKey);

            URLConnection myURLConnection = myURL.openConnection();
            myURLConnection.connect();

            try {
                JSONObject jsonResponse = new JSONObject(getResponse(myURLConnection));
                JSONArray participants = jsonResponse.getJSONArray("participants");

                for (int i = 0; i < participants.length(); i++) {
                    JSONObject player = participants.getJSONObject(i);
                    JSONObject playerStats = player.getJSONObject("stats");
                    for (int x = 0; x < 7; x++) {
                        String item = playerStats.get("item" + x).toString();

                        if (item != null && itemLookup.findItemById(item) != null) {
                            itemRecorder.addUsage(itemLookup.findItemById(item));
                        }
                    }
                }

            } catch (JSONException ex) {
                System.out.println("getMatch JSON Exception.");
            }
        } catch (MalformedURLException ex) {
            System.out.println("Malformed URL.");

        } catch (IOException ex) {
            System.out.println("openConnection failed");
        }
    }

    /**
     * Gets the response from a URLConnection and returns it in a String.
     * @param myURLConnection The URLConnection that the response from the API is held in.
     * @return A String containing the response from the URLConnection.
     * @throws IOException 
     */
    private String getResponse(URLConnection myURLConnection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(
                myURLConnection.getInputStream()));

        String inputLine = "";
        String response = "";
        while ((inputLine = in.readLine()) != null) {
            response += inputLine;
        }
        in.close();

        return response;
    }

}
