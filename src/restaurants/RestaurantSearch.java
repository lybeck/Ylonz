package restaurants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
        restaurantList = new Serialiser<ArrayList<Restaurant>>().deserialize(filename);
    }
    
    public List<Restaurant> crossWordMatch(String s) {
        String regex = ".*" + s.replace('*', '.').toLowerCase() + ".*";
        List<Restaurant> matchingRestaurants = new ArrayList<>();
        
        for (Restaurant restaurant : restaurantList) {
            String name = restaurant.getName().toLowerCase();
            if (name.matches(regex))
                matchingRestaurants.add(restaurant);
        }
        
        return matchingRestaurants;
    }

    public List<Restaurant> anagramMatch(String s) {
        String sLower  = s.toLowerCase();
        int windowSize = s.length();
        List<Restaurant> matchingRestaurants = new ArrayList<>();
        
        for (Restaurant restaurant : restaurantList) {
            String name = restaurant.getName().toLowerCase();
            
            int limit = name.length() - windowSize;
            for (int i = 0; i <= limit; ++i) {
                String sub = name.substring(i, i + windowSize);
                if (isAnagram(sub, sLower)) {
                    matchingRestaurants.add(restaurant);
                    break;
                }
            }
        }
        
        return matchingRestaurants;
    }
    
    private boolean isAnagram(String a, String b) {
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        
        Arrays.sort(aArray);
        Arrays.sort(bArray);
        
        return Arrays.equals(aArray, bArray);
    }
    
    public List<Restaurant> partialMatch(String s) {
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
