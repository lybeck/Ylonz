package restaurants;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author Lasse Lybeck
 */
public class Restaurant implements Comparable<Restaurant>, Serializable {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Restaurant: " + name + ", address: " + address;
    }

    @Override
    public int compareTo(Restaurant o) {
        return name.compareToIgnoreCase(o.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Restaurant)) {
            return false;
        }
        Restaurant r = (Restaurant) obj;
        return name.equalsIgnoreCase(r.name);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.name);
        return hash;
    }
}
