
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import restaurants.Restaurant;
import restaurants.Serialiser;

/**
 *
 * @author Hedd
 */
public class CombineRestaurants {
    
    private static void updateMap(String filename, HashMap<String, Restaurant> restaurantMap) throws IOException, ClassNotFoundException {
        ArrayList<Restaurant> restaurantList = new Serialiser().deserialize(filename);
        restaurantList.stream().forEach((restaurant) -> {
            String key = restaurant.getName().toLowerCase().trim().replaceAll("[^A-Za-z0-9 ]", "");
            restaurantMap.putIfAbsent(key, restaurant);
        });
    }
    
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        HashMap<String, Restaurant> restaurantMap = new HashMap<>();
        updateMap("eatFiRestaurants.ser", restaurantMap);
        updateMap("fonectaRestaurants.ser", restaurantMap);
        
        String filename = "restaurants.txt";
        PrintWriter pw = new PrintWriter(new File(filename));
        restaurantMap.values().stream().forEach((restaurant) -> {
            pw.println(restaurant);
        });
        pw.close();

        String serFilename = "restaurants.ser";
        ArrayList<Restaurant> restaurantList = new ArrayList<>(restaurantMap.values());
        new Serialiser().serialize(restaurantList, serFilename);
    }
}
