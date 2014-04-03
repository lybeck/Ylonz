
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import restaurants.Restaurant;
import restaurants.Serialiser;

/**
 *
 * @author Hedd
 */
public class ResultsCombiner {

    private void updateMap(String filename, HashMap<String, Restaurant> restaurantMap) throws IOException, ClassNotFoundException {
        ArrayList<Restaurant> restaurantList = new Serialiser<ArrayList<Restaurant>>().deserialize(filename);
        for (Restaurant restaurant : restaurantList) {
            String key = restaurant.getName().toLowerCase().trim().replaceAll("[^A-Za-z0-9 ]", "");
            restaurantMap.putIfAbsent(key, restaurant);
        }
    }

    public void combine() throws IOException, ClassNotFoundException {
        HashMap<String, Restaurant> restaurantMap = new HashMap<>();
        updateMap("eatFiRestaurants.ser", restaurantMap);
        updateMap("fonectaRestaurants.ser", restaurantMap);
        updateMap("yelp-restaurants.ser", restaurantMap);
        updateMap("foursquare-restaurants.ser", restaurantMap);

        ArrayList<Restaurant> restaurantList = new ArrayList<>(restaurantMap.values());
        Collections.sort(restaurantList);

        String filename = "restaurants.txt";
        PrintWriter pw = new PrintWriter(new File(filename));
        for (Restaurant restaurant : restaurantList) {
            pw.println(restaurant);
        }
        pw.close();

        String serFilename = "restaurants.ser";
        new Serialiser<ArrayList<Restaurant>>().serialize(restaurantList, serFilename);

        System.out.println("Saved " + restaurantMap.size() + " entries.");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        new ResultsCombiner().combine();
    }
}
