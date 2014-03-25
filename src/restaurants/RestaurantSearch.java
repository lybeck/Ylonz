package restaurants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Lasse Lybeck
 */
public class RestaurantSearch {

    private static final String filename = "restaurants.ser";
    private final ArrayList<Restaurant> restaurantList;

    public RestaurantSearch() throws IOException, ClassNotFoundException {
        restaurantList = new Serialiser().deserialize(filename);
    }

    public List<Restaurant> search(String s) {
        String[] split = s.trim().split("\\s");
        List<Set<Restaurant>> sets = new ArrayList<>();
        for (String string : split) {
            Set<Restaurant> set = new HashSet<>();
            String regex = ".*" + string.toLowerCase() + ".*";
            for (Restaurant restaurant : restaurantList) {
                if (restaurant.getName().toLowerCase().matches(regex)
                        || restaurant.getAddress().toLowerCase().matches(regex)) {
                    set.add(restaurant);
                }
            }
            sets.add(set);
        }
        return intersect(sets);
    }

    private List<Restaurant> intersect(List<Set<Restaurant>> sets) {
        Set<Restaurant> rSet = sets.get(0);
        for (int i = 1; i < sets.size(); i++) {
            rSet.retainAll(sets.get(i));
        }
        List<Restaurant> list = new ArrayList<>(rSet);
        Collections.sort(list);
        return list;
    }
}
