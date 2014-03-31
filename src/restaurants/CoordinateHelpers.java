/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package restaurants;

import javax.vecmath.Point2d;

/**
 *
 * @author Hedd
 */
public class CoordinateHelpers {
    public static Point2d parseKKJ(String coordinateText) {
        String[] split = coordinateText.split(",");
        double lon = Double.parseDouble(split[0]) / 100000.0;
        double lat = Double.parseDouble(split[1]) / 100000.0;
        return new Point2d(lon, lat);
    }
    
    public static String formatKKJ(Point2d coordinatesKKJ) {
        int lon = (int)(coordinatesKKJ.getX() * 100000);
        int lat = (int)(coordinatesKKJ.getY() * 100000);
        return lon + "," + lat;
    }
    
    private static final double EARTH_RADIUS_KM = 6371;
    public static double distanceKMFromWGS84(Point2d aWGS84, Point2d bWGS84) {
        Point2d delta = new Point2d(aWGS84);
        delta.sub(bWGS84);
        delta.scale(0.5);
        
        double latSine = Math.sin(Math.toRadians(delta.getX()));
        double lonSine = Math.sin(Math.toRadians(delta.getY()));
          
        double a = latSine * latSine;
        double b = Math.cos(Math.toRadians(aWGS84.getX())) * 
                   Math.cos(Math.toRadians(bWGS84.getX())) *
                   lonSine * lonSine;
        return 2 * EARTH_RADIUS_KM * Math.asin(Math.sqrt(a + b));
    }
    
    public static double metersFromWGS84(double a1, double a2, double b1, double b2) {
        return 1000 * distanceKMFromWGS84(new Point2d(a1, a2), new Point2d(b1, b2));
    }
}