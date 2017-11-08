package mam_wadim_sokolowski.mam_lab3;

import java.util.Comparator;

public class POI {
    private String pointName;
    private String description;
    private double pointLat;
    private double pointLon;
    private double distanceTo;

    public POI(String pointName, String description, double newLatitude, double newLongitude) {
        this.pointName = pointName;
        this.description = description;
        this.pointLat = newLatitude;
        this.pointLon = newLongitude;
        this.distanceTo = 0;
    }

    public static final Comparator<POI> POI_COMPARATOR =
            new Comparator<POI>() {
                public int compare(POI POI1, POI POI2) {
                    return Double.compare(POI1.getDistanceTo(), POI2.getDistanceTo());
                }
            };


    public void setDistanceTo(double distance) {
        this.distanceTo = distance;
    }

    public String getPointName() {
        return pointName;
    }

    public double getPointLat() {
        return pointLat;
    }

    public double getPointLon() {
        return pointLon;
    }

    public String getDescription() {
        return description;
    }

    public double getDistanceTo() {
        return distanceTo;
    }
}
