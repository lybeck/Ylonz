package restaurants;

import com.sun.javafx.geom.Vec2d;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Verb;

/**
 *
 * @author Lasse Lybeck
 */
public class FourSquare {

    private final JSONParser jsonParser;
    private final String clientId;
    private final String clientSecret;
    private static final int LIMIT = 50;

    public FourSquare(String clientId, String clientSecret) {
        this.jsonParser = new JSONParser();
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public List<Restaurant> search(String term, double latitude, double longitude, int radius) throws
            MalformedURLException, IOException {

        String urlString = "https://api.foursquare.com/v2/venues/search"
                + "?client_id=" + clientId
                + "&client_secret=" + clientSecret
                + "&v=20130815"
                + "&ll=" + latitude + "," + longitude
                + "&limit=" + LIMIT
                //                + "&query=" + term
                + "&categoryId=" + "4d4b7105d754a06376d81259" // nightlife spot
                + "&radius=" + radius;

        List<Restaurant> restaurantList = new ArrayList<>();

        URL url = new URL(urlString);
        String responseString = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream())).readLine();
//        System.out.println("responseString = " + responseString);

        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
            jsonObject = (JSONObject) jsonObject.get("response");
            JSONArray list = (JSONArray) jsonObject.get("venues");
            if (list != null) {
                Iterator it = list.iterator();
                int num = 0;
                while (it.hasNext()) {
                    ++num;
                    JSONObject next = (JSONObject) it.next();
                    Restaurant restaurant = new Restaurant();
                    String name = (String) next.get("name");
                    JSONObject location = (JSONObject) next.get("location");
                    String city = (String) location.get("city");
                    String address = (String) location.get("address");
                    restaurant.setName(name);
                    if (address != null && !address.isEmpty()) {
                        restaurant.setAddress(address + ", " + city);
                    } else {
                        restaurant.setAddress("");
                    }
                    boolean newEntry = restaurantList.add(restaurant);
//                    if (newEntry) {
//                        System.out.println("New bar found!");
//                        System.out.println(restaurant);
//                    }
                }
            }
        } catch (ParseException ex) {
            System.out.println("Goddammit!");
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return restaurantList;
    }

    // CLI
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

        File file = new File("foursquare.pass");
        Scanner scanner = new Scanner(file);

        String filename = "foursquare-restaurants.ser";

        String clientId = scanner.nextLine();
        String clientSecret = scanner.nextLine();

        FourSquare fs = new FourSquare(clientId, clientSecret);

        Vec2d start = new Vec2d(60.156927, 24.925997);
        Vec2d end = new Vec2d(60.176547, 24.961317);

        double start1 = start.x;
        double start2 = start.y;
        double end1 = end.x;
        double end2 = end.y;

        double d1 = (end1 - start1) / 30;
        double d2 = (end2 - start2) / 30;
        double a = CoordinateHelpers.metersFromWGS84(start1, start2, start1 + d1, start2);
        double b = CoordinateHelpers.metersFromWGS84(start1, start2, start1, start2 + d2);
        int radius = (int) Math.round(.5 * Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));

        Serialiser serialiser = new Serialiser();
        Set<Restaurant> set = new HashSet<>(serialiser.deserialize(filename));
        int newResults = 0;

        loop:
        for (double x1 = start1; x1 <= end1; x1 += d1) {
            for (double x2 = start2; x2 <= end2; x2 += d2) {
                List<Restaurant> results = fs.search("bar", x1, x2, radius);
                if (results.size() >= LIMIT) {
                    System.out.println(LIMIT + " results found at (" + x1 + ", " + x2 + ")!");
                }

//                for (Restaurant restaurant : results) {
//                    System.out.println(restaurant);
//                }
                int before = set.size();
                set.addAll(results);
                int after = set.size();
                int diff = after - before;
                if (diff > 0) {
                    newResults += diff;
                }
                System.out.println("(" + x1 + ", " + x2 + ")");
                System.out.println("result.size() = " + results.size());
                System.out.println("New results so far: " + newResults);
            }
        }

        System.out.println("New results = " + newResults);

        ArrayList<Restaurant> restaurants = new ArrayList<>(set);

        serialiser.serialize(restaurants, filename);
        Collections.sort(restaurants);

//        for (Restaurant restaurant : restaurants) {
//            System.out.println(restaurant);
//        }
//
//        System.out.println();
//        System.out.println("Total: " + restaurants.size());
    }
}
