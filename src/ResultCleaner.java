
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import restaurants.Restaurant;
import restaurants.Serialiser;

/**
 *
 * @author Lasse Lybeck
 */
public class ResultCleaner {

    private Serialiser serialiser;

    public ResultCleaner() {
        serialiser = new Serialiser();
    }

    public void clean(String filename) throws IOException, FileNotFoundException, ClassNotFoundException {
        ArrayList<Restaurant> list = new ArrayList<>();
        list.addAll(serialiser.deserialize(filename));

        int nullNames = 0;
        int nullAddresses = 0;
        for (Restaurant restaurant : list) {
            if (restaurant.getName() == null) {
                ++nullNames;
                restaurant.setName("");
            }
            if (restaurant.getAddress() == null) {
                ++nullAddresses;
                restaurant.setAddress("");
            }
        }
        
        serialiser.serialize(list, filename);
        
        System.out.println("File: " + filename);
        System.out.println("Total entries = " + list.size());
        System.out.println("null names = " + nullNames);
        System.out.println("null addresses = " + nullAddresses);
        System.out.println("Now ok! :)");
        System.out.println();

    }

    public static void main(String[] args) throws IOException, FileNotFoundException, ClassNotFoundException {
        ResultCleaner cleaner = new ResultCleaner();
        cleaner.clean("eatFiRestaurants.ser");
        cleaner.clean("fonectaRestaurants.ser");
        cleaner.clean("yelp-restaurants.ser");
    }
}
