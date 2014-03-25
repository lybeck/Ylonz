package ylonz;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import restaurants.Restaurant;
import restaurants.RestaurantSearch;
import restaurants.Serialiser;

/**
 *
 * @author Lasse Lybeck
 */
public class Ylonz {

    private static RestaurantSearch restaurantSearch;

    static {
        try {
            restaurantSearch = new RestaurantSearch();
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("DAMN!");
            System.exit(1);
        }
    }

    static void testDeSerialize() throws IOException, ClassNotFoundException {
        String filename = "restaurants.ser";
        ArrayList<Restaurant> deserialize = new Serialiser().deserialize(filename);

        for (Restaurant restaurant : deserialize) {
            System.out.println(restaurant);
        }
    }

    static void testSearch(String searchString) throws IOException, ClassNotFoundException {
        List<Restaurant> results = restaurantSearch.search(searchString);
        System.out.println("Results with search '" + searchString + "':");
        for (Restaurant restaurant : results) {
            System.out.println(restaurant);
        }
        System.out.println("");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        testSearch("will");
        testSearch("will iam");
        testSearch("will iam anna");
    }

}
