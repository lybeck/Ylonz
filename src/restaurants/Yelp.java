package restaurants;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
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
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.oauth.OAuthService;

/**
 *
 * @author Lasse Lybeck
 */
public class Yelp {

    OAuthService service;
    Token accessToken;
    JSONParser jsonParser;

    /**
     * Setup the Yelp API OAuth credentials.
     *
     * OAuth credentials are available from the developer site, under Manage API access (version 2 API).
     *
     * @param consumerKey Consumer key
     * @param consumerSecret Consumer secret
     * @param token Token
     * @param tokenSecret Token secret
     */
    public Yelp(String consumerKey, String consumerSecret, String token, String tokenSecret) {
        this.service = new ServiceBuilder().provider(YelpApi2.class).apiKey(consumerKey).apiSecret(consumerSecret).build();
        this.accessToken = new Token(token, tokenSecret);
        this.jsonParser = new JSONParser();
    }

    /**
     * Search with term and location.
     *
     * @param term Search term
     * @param latitude Latitude
     * @param longitude Longitude
     * @param radius Radius
     * @return JSON string response
     */
    public List<Restaurant> search(String term, double latitude, double longitude, int radius) {
        OAuthRequest request = new OAuthRequest(Verb.GET, "http://api.yelp.com/v2/search");
        request.addQuerystringParameter("term", term);
        request.addQuerystringParameter("limit", "20");
        request.addQuerystringParameter("sort", "1");
        request.addQuerystringParameter("radius_filter", "" + radius);
        request.addQuerystringParameter("ll", latitude + "," + longitude);
        this.service.signRequest(this.accessToken, request);
        Response response = request.send();
        String responseString = response.getBody();
        List<Restaurant> restaurantList = new ArrayList<>();
        try {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(responseString);
            JSONArray list = (JSONArray) jsonObject.get("businesses");
            Iterator it = list.iterator();
            while (it.hasNext()) {
                JSONObject next = (JSONObject) it.next();
                Restaurant restaurant = new Restaurant();
                String name = (String) next.get("name");
                restaurant.setName(name);
                JSONObject addressObject = (JSONObject) next.get("location");
                String city = (String) addressObject.get("city");
                JSONArray arr = (JSONArray) addressObject.get("address");
                String address;
                if (arr != null && !arr.isEmpty()) {
                    address = (String) arr.get(0);
                    restaurant.setAddress(address + ", " + city);
                } else {
                    restaurant.setAddress("");
                }
                boolean newEntry = restaurantList.add(restaurant);
//                if (newEntry) {
//                    System.out.println("New bar found!");
//                    System.out.println(restaurant);
//                }
            }
        } catch (ParseException ex) {
            Logger.getLogger(Yelp.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        return restaurantList;
    }

    // CLI
    public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

        File file = new File("yelp.pass");
        Scanner scanner = new Scanner(file);

        // Update tokens here from Yelp developers site, Manage API access.
        String consumerKey = scanner.nextLine();
        String consumerSecret = scanner.nextLine();
        String token = scanner.nextLine();
        String tokenSecret = scanner.nextLine();

        double start1 = 60.130735;
        double start2 = 24.76244;
        double end1 = 60.288514;
        double end2 = 25.203953;

        double d1 = (end1 - start1) / 33;
        double d2 = (end2 - start2) / 33;
        double a = CoordinateHelpers.metersFromWGS84(start1, start2, start1 + d1, start2);
        double b = CoordinateHelpers.metersFromWGS84(start1, start2, start1, start2 + d2);
        int radius = (int) Math.round(.5 * Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2)));

//        System.out.println("radius = " + radius);
        Yelp yelp = new Yelp(consumerKey, consumerSecret, token, tokenSecret);
        Serialiser<ArrayList<Restaurant>> serialiser = new Serialiser();
        Set<Restaurant> set = new HashSet<>(serialiser.deserialize("yelp-restaurants.ser"));
        int yelpSize = set.size();
        int newResults = 0;

        loop:
        for (double x1 = start1; x1 <= end1; x1 += d1) {
            for (double x2 = start2; x2 <= end2; x2 += d2) {
                List<Restaurant> results = yelp.search("bar", x1, x2, radius);
                if (results.size() >= 20) {
                    System.out.println("20 results found at (" + x1 + ", " + x2 + ")!");
                }
                int before = set.size();
                set.addAll(results);
                int after = set.size();
                int diff = after - before;
                if (diff > 0) {
                    newResults += diff;
                }
//                System.out.println("(" + x1 + ", " + x2 + ")");
//                System.out.println("result.size() = " + results.size());
//                System.out.println("Size so far: " + (set.size() - yelpSize));
            }
        }

        System.out.println("New results = " + newResults);

        ArrayList<Restaurant> restaurants = new ArrayList<>(set);

        serialiser.serialize(restaurants, "yelp-restaurants.ser");
        Collections.sort(restaurants);
        
//        for (Restaurant restaurant : restaurants) {
//            System.out.println(restaurant);
//        }
//
//        System.out.println();
//        System.out.println("Total: " + restaurants.size());
    }
}
