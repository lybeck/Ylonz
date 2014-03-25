package ylonz;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * @author Lasse Lybeck
 */
public class RegTest {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("asdasd hjk");
        list.add("hjk asd");
        list.add("asd");
        list.add("kakka");

        String regex = ".*as.*";
        for (String s : list) {
            if (s.matches(regex)) {
                System.out.println(s);
            }
        }
    }
}
