package restaurants;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Lasse Lybeck
 */
public class EatFiParser {

    private static final int pages = 1000;
    private static final int tries = 5;

    public static void main(String[] args) throws IOException {
        ArrayList<Restaurant> restaurantList = new EatFiParser().getAllRestaurants();

        String filename = "eatFiRestaurants.txt";
        PrintWriter pw = new PrintWriter(new File(filename));
        for (Restaurant restaurant : restaurantList) {
            pw.println(restaurant);
        }
        pw.close();

        String serFilename = "eatFiRestaurants.ser";
        new Serialiser().serialize(restaurantList, serFilename);

    }

    public ArrayList<Restaurant> getAllRestaurants() throws IOException {

        Document doc;
        String url;

        ArrayList<Restaurant> rList = new ArrayList<>();

        Set<String> names = new HashSet<>();
        int prevSize = -1;
        for (int i = 1; i <= pages; i++) {

            if (names.size() == prevSize) {
                break;
            }
            prevSize = names.size();

            url = "http://eat.fi/fi/helsinki/restaurants?page=" + i;
            doc = null;
            for (int j = 0; j < tries; j++) {
                try {
                    doc = Jsoup.connect(url).get();
                    break;
                } catch (SocketTimeoutException e) {
                    System.err.println("Timed out... Trying again. Tries: " + (j + 1));
                }
            }
            if (doc == null) {
                throw new SocketTimeoutException("Tried " + tries + " times, still no answer... :(");
            }
            Elements restaurantDivs = doc.select(".restaurant-entry*");
            for (Element rDiv : restaurantDivs) {
                Elements nameHrefs = rDiv.select("a[href]");
                if (nameHrefs.size() != 1) {
                    System.err.println("Hmmm... Not one name href...");
                }
                String name = Jsoup.parse(nameHrefs.get(0).html()).text();
                if (names.add(name)) {
                    String address = null;
                    Elements contactDivs = rDiv.select(".restaurant-contact-entry-value");
                    if (!contactDivs.isEmpty()) {
                        address = Jsoup.parse(contactDivs.get(0).html()).text();
                    }
                    Restaurant restaurant = new Restaurant();
                    restaurant.setName(name);
                    restaurant.setAddress(address);
                    rList.add(restaurant);
                }
            }

            System.out.println("Names after page nr " + i + ": " + names.size());

        }
        Collections.sort(rList);
        return rList;
    }
}
